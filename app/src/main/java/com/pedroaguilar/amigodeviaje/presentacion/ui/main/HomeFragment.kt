package com.pedroaguilar.amigodeviaje.presentacion.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pedroaguilar.amigodeviaje.databinding.FragmentHomeBinding
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.SugerenciasCategoriaAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter.CategoriasAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel.HomeFragmentViewModel


class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val categoriasAdapter: CategoriasAdapter = CategoriasAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
        Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.carousel3d.adapter = categoriasAdapter
        binding.carousel3d.apply {
            set3DItem(true)
            setAlpha(true)
        }
        viewModel.obtenerTodasLasSugerencias()
        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            categoriasAdapter.dataSet = it.categorias
            binding.rvTodasSugerencias.adapter = SugerenciasCategoriaAdapter(it.sugerencias)
        }
    }
}