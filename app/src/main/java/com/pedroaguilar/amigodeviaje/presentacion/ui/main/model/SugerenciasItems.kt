package com.pedroaguilar.amigodeviaje.presentacion.ui.main.model

import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.model
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 20:30
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class SugerenciasItems {
    fun defineDatas(): ArrayList<Sugerencia> {
        val a = Sugerencia(name = "Comer", imgUrl = "https://okdiario.com/img/2017/12/20/-sabias-que-comer-deprisa-es-perjudicial-para-la-salud-1.jpg")
        val b = Sugerencia(name = "Dormir", imgUrl = "https://i.blogs.es/9e2ecb/1d9341efe35b4ac6f0c9fcfdb3c7318a74ad9903_beddel006blu_uk_delia_double_bed_seafoam_blue_velvet_ar3_2_lb02_ls/original.jpeg")
        val cc = Sugerencia(name = "Fiesta", imgUrl = "https://revistahsm.com/wp-content/uploads/2018/08/Fiestas.png")
        val d = Sugerencia(name = "Turismo", imgUrl = "https://humanidades.com/wp-content/uploads/2018/09/turismo-e1579550648680.jpg")
        val e = Sugerencia(name = "Aventura", imgUrl = "https://www.isotools.org/wp-content/uploads/2013/06/small_turismo-aventura.jpg")

        val c = arrayListOf(cc,d,a,b,e)
        return c
    }
}