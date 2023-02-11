package com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.databinding.SugerenciaItemBinding
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.dormir.viewModel.DormirFragmentViewModel

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 19:47
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class SugerenciaAdapter(var dataSet: ArrayList<Sugerencia>) :
    RecyclerView.Adapter<SugerenciaAdapter.ViewHolder>() {

    private lateinit var binding: SugerenciaItemBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = SugerenciaItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.sugerencia = dataSet[position]
        viewHolder.itemView.setOnClickListener {
            when(dataSet[position].category){
                Categorias.COMER -> it.findNavController().navigate(R.id.action_home_dest_to_comerFragment)
                Categorias.DORMIR -> it.findNavController().navigate(R.id.action_home_dest_to_dormirFragment)
                Categorias.FIESTA -> it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
                Categorias.TURISMO ->  it.findNavController().navigate(R.id.action_home_dest_to_turismoFragment)
                Categorias.AVENTURA -> it.findNavController().navigate(R.id.action_home_dest_to_aventuraFragment)
                null -> Toast.makeText(viewHolder.view.root.context, "Click No implementado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount() = dataSet.size

    class ViewHolder(var view: SugerenciaItemBinding) : RecyclerView.ViewHolder(view.root){

    }

}