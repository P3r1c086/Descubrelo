package com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.databinding.FragmentCategoriaDetalleBinding
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.SugerenciasCategoriaAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle.viewModel.CategoriaDetalleFragmentViewModel
import com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle.viewModel.CategoriaDetalleFragmentViewModelFactory


class CategoriaDetalleFragment : Fragment() {
    private val safeArgs: CategoriaDetalleFragmentArgs by navArgs()

    private val viewModel: CategoriaDetalleFragmentViewModel by viewModels {
        CategoriaDetalleFragmentViewModelFactory(requireNotNull(safeArgs.categoriaId))
    }
    private lateinit var binding: FragmentCategoriaDetalleBinding
    private val adapter: SugerenciasCategoriaAdapter = SugerenciasCategoriaAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentCategoriaDetalleBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //activity?.findViewById<Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            Categorias.values()[safeArgs.categoriaId].value.lowercase().replaceFirstChar { it.uppercase() }
        viewLifecycleOwner.launchAndCollect(viewModel.state){
            adapter.listaSugerencias = it.sugerencias
            binding.rv.adapter = adapter
            binding.sugerencias = it.sugerencias.toList()
            binding.loading = it.loading
            binding.error = it.error?.let(::errorToString)
        }
        viewModel.cargarSugerencias()
    }

    private fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context?.getString(R.string.connectivity_error)
        is Error.Server -> context?.getString(R.string.server_error) + error.code
        is Error.NoData -> context?.getString(R.string.sin_datos)
        else -> context?.getString(R.string.unknown_error)
    }
}