package com.pedroaguilar.amigodeviaje.presentacion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pedroaguilar.amigodeviaje.databinding.SugerenciaComunItemBinding
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui
 * Create by Pedro Aguilar Fernández on 11/02/2023 at 12:55
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class SugerenciasAdapter(var listaSugerencias: ArrayList<Sugerencia>) :
    RecyclerView.Adapter<SugerenciasAdapter.ViewHolder>(){
        private lateinit var binding: SugerenciaComunItemBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = SugerenciaComunItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //con esto entro a la variable sugerencia del xml
        viewHolder.view.sugerencia = listaSugerencias[position]
        //hago todas las sugerencias invisibles
        viewHolder.itemView.visibility = View.INVISIBLE
        //todo: hacer que segun la categoria, sea visible en los distintos fragmentos
        if(listaSugerencias[position].category == Categorias.COMER) {
            viewHolder.itemView.visibility = View.VISIBLE
        } else if(listaSugerencias[position].category == Categorias.DORMIR){
            viewHolder.itemView.visibility = View.VISIBLE
        }else if(listaSugerencias[position].category == Categorias.FIESTA){
            viewHolder.itemView.visibility = View.VISIBLE
        }else if(listaSugerencias[position].category == Categorias.TURISMO){
            viewHolder.itemView.visibility = View.VISIBLE
        }else if(listaSugerencias[position].category == Categorias.AVENTURA){
            viewHolder.itemView.visibility = View.VISIBLE
        }
        else {
            Toast.makeText(viewHolder.view.root.context, "No hay Sugerencias", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount() = listaSugerencias.size

    class ViewHolder(var view: SugerenciaComunItemBinding) : RecyclerView.ViewHolder(view.root)


}