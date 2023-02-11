package com.pedroaguilar.amigodeviaje.servicios

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pedroaguilar.amigodeviaje.modelo.Constants
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.modelo.entities.Usuario
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Esta clase externaliza las conexiones a la base de datos y al autenticador de Firebase
 */
class ServicioFirebaseDatabase {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReferenceUsuarios: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constantes.NODO_USUARIOS)
    private val firestoreReferenceSugerencias: CollectionReference =
        FirebaseFirestore.getInstance().collection(Constants.COLL_SUGGEST)
    private lateinit var sugerenciasList: ArrayList<Sugerencia>

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
    //Zona Sugerencia
    suspend fun registrarSugerencia(firebaseAuthUsuarioId: String, sugerencia: Sugerencia,
                                    idSugerenciaUser: String): Sugerencia? =
        suspendCancellableCoroutine { continuation ->
            firestoreReferenceSugerencias
            .document(firebaseAuthUsuarioId)
                .collection("sugerenciasUser")
                .document(idSugerenciaUser)
                .set(sugerencia)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(sugerencia)
                } else {
                    continuation.resume(null)
                }
            }
        }

    suspend fun idSugerenciaUser(uidUser: String): String? =
        suspendCancellableCoroutine { continuation ->
            firestoreReferenceSugerencias
                .document(uidUser).collection("sugerenciasUser")
                .get()
                .addOnSuccessListener { task ->
                    val numSugerencias = task.size()
                    if (numSugerencias == 0) {
                        continuation.resume("Sugerencia1")
                    } else {
                        continuation.resume("Sugerencia" + (numSugerencias + 1))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }

    suspend fun obtenerTodasSugerencias(uidUser: String) : ArrayList<Sugerencia> =
        suspendCancellableCoroutine { continuation ->
            firestoreReferenceSugerencias
                .document(uidUser).collection("sugerenciasUser")
                .get()
                .addOnSuccessListener { snapshots ->
                    val numSugerencias = snapshots.size()
                    if (numSugerencias != 0) {
                        for (document in snapshots){
                            //extraer cada documento y convertirlo a sugerencia
                            val sugerencia = document.toObject(Sugerencia::class.java)
                            sugerenciasList.add(sugerencia)
                        }
                        continuation.resume(sugerenciasList)
                    }
                }
//                .addOnFailureListener {
//                    continuation.resume(null)
//                }
    }
}