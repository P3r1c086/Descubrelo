package com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pedroaguilar.amigodeviaje.databinding.FragmentCategoriaDetalleBinding
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
        viewLifecycleOwner.launchAndCollect(viewModel.state){
            adapter.listaSugerencias = it.sugerencias
            binding.rv.adapter = adapter
        }
        viewModel.cargarSugerencias()
    }
}