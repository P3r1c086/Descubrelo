package com.pedroaguilar.amigodeviaje.presentacion.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pedroaguilar.amigodeviaje.databinding.SugerenciaCategoriaItemBinding
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle.CategoriaDetalleFragmentDirections

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui
 * Create by Pedro Aguilar Fernández on 11/02/2023 at 12:55
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class SugerenciasCategoriaAdapter(var listaSugerencias: ArrayList<Sugerencia>) :
    RecyclerView.Adapter<SugerenciasCategoriaAdapter.ViewHolder>(){
        private lateinit var binding: SugerenciaCategoriaItemBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = SugerenciaCategoriaItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //con esto entro a la variable sugerencia del xml
        viewHolder.view.sugerencia = listaSugerencias[position]
        viewHolder.itemView.setOnClickListener {
            val action = CategoriaDetalleFragmentDirections.actionCategoriaDestToSugerenciaDetalleDest(
                listaSugerencias[position].category?.ordinal?:-1,
                listaSugerencias[position].id?:"")
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = listaSugerencias.size

    class ViewHolder(var view: SugerenciaCategoriaItemBinding) : RecyclerView.ViewHolder(view.root)
}