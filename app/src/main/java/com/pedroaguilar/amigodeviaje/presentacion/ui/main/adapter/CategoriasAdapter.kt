package com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pedroaguilar.amigodeviaje.databinding.SugerenciaItemBinding
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.HomeFragmentDirections

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 19:47
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class CategoriasAdapter(var dataSet: ArrayList<Sugerencia>) :
    RecyclerView.Adapter<CategoriasAdapter.ViewHolder>() {

    private lateinit var binding: SugerenciaItemBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = SugerenciaItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sugerencia = dataSet[position]
        viewHolder.view.sugerencia = sugerencia
        viewHolder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeDestToCategoriaFragment(sugerencia.category?.ordinal ?:0)
            it.findNavController().navigate(action)
        }
    }
    override fun getItemCount() = dataSet.size

    class ViewHolder(var view: SugerenciaItemBinding) : RecyclerView.ViewHolder(view.root){

    }

}