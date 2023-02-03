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
        val a = Sugerencia(name = "Beef Meat", imgUrl = "https://res.cloudinary.com/fleetnation/image/private/c_fit,w_1120/g_south,l_text:style_gothic2:%C2%A9%20Lubos%20Chlubny,o_20,y_10/g_center,l_watermark4,o_25,y_50/v1451683758/x8tintzjuzmlfrcbkfbp.jpg")
        val b = Sugerencia(name = "Mutton Meat",imgUrl = "https://www.provisioneronline.com/ext/resources/images/Responsive-Default-Images/meat-science-review.jpg?1607975249")
        val cc = Sugerencia(name = "Fish",imgUrl = "https://img.freepik.com/free-photo/fish-steak-white-backgrounds_183352-882.jpg?w=2000")
        val d = Sugerencia(name = "Vegetables",imgUrl = "https://images.fineartamerica.com/images-medium-large-5/tomato-vegetables-pile-isolated-on-white-background-cutout-wanlop-sonngam.jpg")

        val c = arrayListOf(cc,d,a,b)
        return c
    }
}