package com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 19:42
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
@BindingAdapter("url")
fun ImageView.loadImage(url: String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}