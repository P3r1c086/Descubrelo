package com.pedroaguilar.amigodeviaje.servicios

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.modelo.entities.Usuario
import com.pedroaguilar.amigodeviaje.modelo.entities.toMap
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Esta clase externaliza las conexiones a la base de datos y al autenticador de Firebase
 */
class ServicioFirebaseDatabase {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReferenceUsuarios: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(Constantes.NODO_USUARIOS)

    fun firebaseReferenceCategoria (categoria: Categorias): CollectionReference{
        return FirebaseFirestore.getInstance().collection(categoria.value)
    }

    fun obtenerIdDocumentoCategoria(categoria: Categorias) :
            String = firebaseReferenceCategoria(categoria).document().id

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
    suspend fun registrarSugerencia(sugerencia: Sugerencia): Sugerencia? =
        suspendCancellableCoroutine { continuation ->
            sugerencia.category?.let { categoria ->
                firebaseReferenceCategoria(categoria)
                    .document(sugerencia.id)
                    .set(sugerencia)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(sugerencia)
                        } else {
                            continuation.resume(null)
                        }
                    }
            } ?: continuation.resume(null)
        }

    suspend fun hacerSugerenciaFavoritaParaUser(sugerencia: Sugerencia) : Sugerencia? =
            suspendCancellableCoroutine { continuation ->
                sugerencia.category?.let { categoria ->
                    firebaseReferenceCategoria(categoria)
                        .document(sugerencia.id)
                        .update(sugerencia.toMap().apply {
                            val listaFavoritosActual = (get("listaFavoritosIdUsuarios") as ArrayList<String>)
                            listaFavoritosActual.add(FirebaseAuth.getInstance().uid?:"")
                            put("listaFavoritosIdUsuarios", listaFavoritosActual)
                        }.toMap())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(sugerencia)
                            } else {
                                continuation.resume(null)
                            }
                        }
                } ?: continuation.resume(null)
            }

    suspend fun quitarSugerenciaFavoritaParaUser(sugerencia: Sugerencia) : Sugerencia? =
        suspendCancellableCoroutine { continuation ->
            sugerencia.category?.let { categoria ->

                firebaseReferenceCategoria(categoria)
                    .document(sugerencia.id)
                    .update(sugerencia.toMap().apply {
                        val listaFavoritosActual = (get("listaFavoritosIdUsuarios") as ArrayList<String>)
                        listaFavoritosActual.remove(FirebaseAuth.getInstance().uid?:"")
                        put("listaFavoritosIdUsuarios", listaFavoritosActual)
                    }.toMap())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(sugerencia)
                        } else {
                            continuation.resume(null)
                        }
                    }
            } ?: continuation.resume(null)
        }
    suspend fun idSugerenciaUser(categoria: Categorias?): String? =
        suspendCancellableCoroutine { continuation ->
            categoria?.let { categoria ->
                firebaseReferenceCategoria(categoria)
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
            } ?: continuation.resume(null)
        }

    suspend fun obtenerTodasSugerencias(categoria: Categorias?) : ArrayList<Sugerencia> =
        suspendCancellableCoroutine { continuation ->
            categoria?.let { categoria ->
                firebaseReferenceCategoria(categoria)
                    .get()
                    .addOnSuccessListener { snapshots ->
                        val numSugerencias = snapshots.size()
                        if (numSugerencias != 0) {
                            val sugerenciasList: ArrayList<Sugerencia> = ArrayList()
                            for (document in snapshots) {
                                //extraer cada documento y convertirlo a sugerencia
                                val sugerencia = Sugerencia(
                                    id = document.data["id"] as String,
                                    perteneceAUsuario = document.data["perteneceAUsuario"] as String,
                                    category = Categorias.valueOf(document.data["category"] as String),
                                    typeCategory = document.data["typeCategory"] as String,
                                    name = document.data["name"] as String,
                                    description = document.data["description"] as String,
                                    imgUrl = document.data["imgUrl"] as String,
                                    listaFavoritosIdUsuarios = document.data["listaFavoritosIdUsuarios"] as ArrayList<String>)
                                sugerenciasList.add(sugerencia)
                            }
                            continuation.resume(sugerenciasList)
                        } else {
                            continuation.resume( ArrayList())
                        }
                    }
            } ?: ArrayList<Sugerencia>()
    }

    suspend fun obtenerSugerencia(categoria: Categorias?, id: String) : Sugerencia? =
        suspendCancellableCoroutine { continuation ->
            categoria?.let { categoria ->
                firebaseReferenceCategoria(categoria)
                    .get()
                    .addOnSuccessListener { snapshots ->
                        var sugerencia: Sugerencia? = null
                        val numSugerencias = snapshots.size()
                        if (numSugerencias != 0) {
                            for (document in snapshots) {
                                //extraer cada documento y convertirlo a sugerencia
                                if(document.data["id"] == id){
                                    sugerencia = Sugerencia(
                                        id = document.data["id"] as String,
                                        perteneceAUsuario = document.data["perteneceAUsuario"] as String,
                                        category = Categorias.valueOf(document.data["category"] as String),
                                        typeCategory = document.data["typeCategory"] as String,
                                        name = document.data["name"] as String,
                                        description = document.data["description"] as String,
                                        imgUrl = document.data["imgUrl"] as String,
                                        listaFavoritosIdUsuarios = document.data["listaFavoritosIdUsuarios"] as ArrayList<String>)
                                }
                            }
                            continuation.resume(sugerencia)
                        } else {
                            continuation.resume( null)
                        }
                    }
            } ?: ArrayList<Sugerencia>()
        }
}