package com.pedroaguilar.amigodeviaje.presentacion.ui.sugerenciaDetalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.databinding.FragmentDetailsBinding
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.sugerenciaDetalle.viewModel.DetailsFragmentViewModel
import com.pedroaguilar.amigodeviaje.presentacion.ui.sugerenciaDetalle.viewModel.DetailsFragmentViewModelFactory

class SugerenciaDetalleFragment : Fragment() {

    private val safeArgs: SugerenciaDetalleFragmentArgs by navArgs()

    private val viewModel: DetailsFragmentViewModel by viewModels{
        DetailsFragmentViewModelFactory(safeArgs.categoriaId, safeArgs.sugerenciaId)
    }

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.launchAndCollect(viewModel.state){
            binding.sugerencia = it.sugerencia
            binding.loading = it.loading
            binding.error = it.error?.let(::errorToString)
        }
        viewModel.cargarSugerencia()
        //todo: no se de donde obtener la sugerencia para pasarle a este metodo ->
        // checkFavorite(sugerencia)
    }

    private fun checkFavorite(sugerencia: Sugerencia){
        val idUser = FirebaseAuth.getInstance().currentUser?.uid
        binding.cbFavorite.setOnClickListener {
            if (binding.cbFavorite.isChecked){
                if (idUser != null){
                    sugerencia.listaFavoritosIdUsuarios.let {
                        it?.add(idUser)
                    }
                    sugerencia.id?.let { id ->
                        viewModel.actualizarSugerencia(
                            id,
                            sugerencia.category,
                            sugerencia.typeCategory,
                            sugerencia.name,
                            sugerencia.description,
                            sugerencia.imgUrl,
                            sugerencia.listaFavoritosIdUsuarios)
                    }
                }
            }else{
                sugerencia.listaFavoritosIdUsuarios.let {
                    it?.forEach{
                        if (it.contains(idUser.toString())){
                            //todo: no se como borrar el id -> it.remove(it.indexOf(idUser!!))
                        }
                    }
                    viewModel.actualizarSugerencia(
                        sugerencia.id!!,
                        sugerencia.category,
                        sugerencia.typeCategory,
                        sugerencia.name,
                        sugerencia.description,
                        sugerencia.imgUrl,
                        sugerencia.listaFavoritosIdUsuarios)
                }
            }
        }
    }

    private fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context?.getString(R.string.connectivity_error)
        is Error.Server -> context?.getString(R.string.server_error) + error.code
        is Error.NoData -> context?.getString(R.string.sin_datos)
        else -> context?.getString(R.string.unknown_error)
    }
}


