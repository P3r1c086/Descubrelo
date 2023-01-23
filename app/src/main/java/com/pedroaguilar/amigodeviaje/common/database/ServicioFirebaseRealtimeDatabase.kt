package com.pedroaguilar.amigodeviaje.common.database

import com.google.firebase.database.FirebaseDatabase
import com.pedroaguilar.amigodeviaje.common.entities.Sujerencia

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.common.database
 * Create by Pedro Aguilar Fernández on 23/01/2023 at 20:37
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class ServicioFirebaseRealtimeDatabase {

    private val firebaseDataBase = FirebaseDatabase.getInstance()
    private val databaseReferenceSujerencia = FirebaseDatabase.getInstance().getReference("Sujerencias")


   suspend fun crearSujerencia(firebaseAuthUsuarioId: String, sujerencia: Sujerencia?) {
        databaseReferenceSujerencia
            .child(firebaseAuthUsuarioId).setValue(sujerencia)
    }
}