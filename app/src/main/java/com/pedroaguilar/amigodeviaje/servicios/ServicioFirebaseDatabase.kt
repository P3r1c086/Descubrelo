package com.pedroaguilar.amigodeviaje.servicios

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedroaguilar.amigodeviaje.common.entities.Usuario
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Esta clase externaliza las conexiones a la base de datos y al autenticador de Firebase
 */
class ServicioFirebaseDatabase {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReferenceUsuarios: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constantes.NODO_USUARIOS)

    //Zona Usuario
    /**
     * Crea el usuario en la zona de autentificación de Firebase
     */
    suspend fun registrarAuthUsuario(email: String, password: String): String? =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(FirebaseAuth.getInstance().currentUser?.uid)
                } else {
                    continuation.resume(null)
                }
            }
        }

    /**
     * Regista al usuario en la FirebaseDatabase (para poder asociarle más información al usuario)
     */
    suspend fun registrarUsuarioEnBdPropia(firebaseAuthUsuarioId: String, usuario: Usuario): Usuario? =
        suspendCancellableCoroutine { continuation ->
            databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId).setValue(usuario).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(usuario)
                    } else {
                        continuation.resume(null)
                    }
                }
        }
}