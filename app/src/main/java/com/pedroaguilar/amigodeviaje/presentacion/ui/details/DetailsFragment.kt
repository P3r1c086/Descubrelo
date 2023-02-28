package com.pedroaguilar.amigodeviaje.presentacion.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.databinding.FragmentDetailsBinding
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.SugerenciasCategoriaAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle.viewModel.CategoriaDetalleFragmentViewModelFactory
import com.pedroaguilar.amigodeviaje.presentacion.ui.details.viewModel.DetailsFragmentViewModel

class DetailsFragment : Fragment() {

    private val safeArgs: DetailsFragmentArgs by navArgs()

    private val viewModel: DetailsFragmentViewModel by viewModels{
        CategoriaDetalleFragmentViewModelFactory(requireNotNull(safeArgs.categoriaId))
    }

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
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
    }

    private fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context?.getString(R.string.connectivity_error)
        is Error.Server -> context?.getString(R.string.server_error) + error.code
        is Error.NoData -> context?.getString(R.string.sin_datos)
        else -> context?.getString(R.string.unknown_error)
    }
}