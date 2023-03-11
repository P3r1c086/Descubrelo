package com.pedroaguilar.amigodeviaje.presentacion.ui.sugerenciaDetalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.checkbox.MaterialCheckBox.STATE_UNCHECKED
import com.google.firebase.auth.FirebaseAuth
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.databinding.FragmentDetailsBinding
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
        setCheckListener()
        viewLifecycleOwner.launchAndCollect(viewModel.state){
            binding.sugerencia = it.sugerencia
            val checked = !it.sugerencia?.listaFavoritosIdUsuarios?.find { element ->
                element == FirebaseAuth.getInstance().uid
            }.isNullOrEmpty()
            binding.favorite = if (!checked) 0  else 1
            binding.loading = it.loading
            binding.error = it.error?.let(::errorToString)
        }
        viewModel.cargarSugerencia()
    }

    private fun setCheckListener(){
        binding.cbFavorite.setOnClickListener {
            if (binding.cbFavorite.checkedState == STATE_UNCHECKED){
                viewModel.deshacerSugerenciaFavorita()
            }else{
                viewModel.hacerSugerenciaFavorita()
            }
            //TODO: INDETERMINATE STATE - LOADING - ENABLE FALSE DE CHECKBOX
        }
    }

    private fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context?.getString(R.string.connectivity_error)
        is Error.Server -> context?.getString(R.string.server_error) + error.code
        is Error.NoData -> context?.getString(R.string.sin_datos)
        else -> context?.getString(R.string.unknown_error)
    }
}


