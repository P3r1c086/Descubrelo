package com.pedroaguilar.amigodeviaje.presentacion.ui.dormir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pedroaguilar.amigodeviaje.databinding.FragmentDormirBinding
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.SugerenciasAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.dormir.viewModel.DormirFragmentViewModel


class DormirFragment : Fragment() {

    private val viewModel: DormirFragmentViewModel by viewModels()
    private lateinit var binding: FragmentDormirBinding
    private val adapter: SugerenciasAdapter = SugerenciasAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentDormirBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.launchAndCollect(viewModel.state){
            adapter.listaSugerencias = it.sugerencias
            binding.rvDormir.adapter = adapter
        }
        viewModel.cargarSugerencias()
    }
}