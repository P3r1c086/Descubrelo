package com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pedroaguilar.amigodeviaje.FiestaFragment
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.databinding.SugerenciaItemBinding
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia

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
        val sugerencia = dataSet[position]
        viewHolder.itemView.setOnClickListener {
            when(dataSet[position].category){
                sugerencia.category -> it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
                sugerencia.category -> it.findNavController().navigate(R.id.action_home_dest_to_turismoFragment)
//                sugerencia -> it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
//                sugerencia -> it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
//                sugerencia -> it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
            }
            //it.findNavController().navigate(R.id.action_home_dest_to_fiestaFragment)
        }
    }
    override fun getItemCount() = dataSet.size

    class ViewHolder(var view: SugerenciaItemBinding) : RecyclerView.ViewHolder(view.root){

    }

}