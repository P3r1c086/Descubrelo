package com.pedroaguilar.amigodeviaje.modelo.entities

import com.google.firebase.firestore.Exclude

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.common.entities.utils
 * Create by Pedro Aguilar Fernández on 23/01/2023 at 20:23
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
data class Sugerencia(
    var id: String? = null,
    var category: String? = null,
    var typeCategory: String? = null,
    var name: String? = null,
    var description: String? = null,
    var imgUrl: String? = null){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sugerencia

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
