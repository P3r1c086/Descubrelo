package com.pedroaguilar.amigodeviaje.presentacion.ui.add.viewModel

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pedroaguilar.amigodeviaje.common.Constants
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.common.entities.EventPost
import com.pedroaguilar.amigodeviaje.common.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.servicios.ServicioFirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.addModule.viewModel
 * Create by Pedro Aguilar Fernández on 27/01/2023 at 18:25
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class AddFragmentViewModel: ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun registrarSugerenciaEnFirestore(uidUser: String, category: String?, typeCategory: String?,
                                              nombre: String?, descripcion: String?,
                                              imgUrl: String?){
        viewModelScope.launch {
            val sugerenciaPorUsuario = firebaseDatabase.registrarSugerencia(uidUser,
            Sugerencia(uidUser, category, typeCategory ,nombre, descripcion, imgUrl))
            if (sugerenciaPorUsuario != null){
                _state.update {
                    _state.value.copy(
                        loading = false,
                        sugerenciaSubidaCorrectamente = true
                    )
                }
            } else {
                _state.update { _state.value.copy(loading = false, error = Error.Server(456)) }
            }
        }
    }

    fun setCategory(category: String){
        _state.update { _state.value.copy(category = category) }
    }
    fun setName(nombre: String){
        _state.update { _state.value.copy(nombre = nombre) }
    }

    fun setDescription(description: String){
        _state.update { _state.value.copy(descripcion = description) }
    }
    fun setImageUrl(imageUrl: String){
        _state.update { _state.value.copy(photoSelectedUri = imageUrl) }
    }
    fun setTypeCategory(typeCategory: String){
        _state.update { _state.value.copy(typeCategory = typeCategory) }
    }

    fun onClickAccept(){
        //creamos una nueva sugerencia. El id debe ser generado automaticamente. Aqui lo omitimos
        //y empezamos por la propiedad name
        //como binding puede ser null
        //subir imagen al storage. Recibe un callBack

        uploadImage { eventPost ->
            //si la imagen fue subida correctamente
            if (eventPost.isSuccess){
                FirebaseAuth.getInstance().uid?.let { id -> registrarSugerenciaEnFirestore(
                    id, category = _state.value.category,
                    typeCategory = _state.value.typeCategory,
                    nombre = _state.value.nombre,
                    descripcion = _state.value.descripcion,
                    imgUrl = eventPost.photoUrl) }
            } else {
                //EMITIMOS ERROR AL SUBIR LA IMAGEN: CREAR ERROR CONCRETO
                _state.update { _state.value.copy(loading = false, error = Error.NoData) }
            }
        }
    }


    /**
     * metodo para subir imagenes al storage
     */
    private fun uploadImage(callback: (EventPost)->Unit){ //que retorna Unit sig que no retorna nada
        //creamos una nueva instancia de EventPost, la cual va a contener el documento
        val eventPost = EventPost()
        //El signo de Elvis, hace que en caso de que sea null, agarre el id del nuevo documento, sino
        // que se quede con el id de la sugerencia actual.
        //Extraemos el id del document. Estamos reservando un lugar para que la imagen que subamos
        // tenga como nombre este id. Posteriormente, una vez que termine el proceso de subir vamos a
        // regresar ese documento para que la imagen que vayamos a subir sea asignada con el nombre
        // de este id y posteriormente, despues de que se suba nuestra imagen, ahora si, vamos a
        // agarrar el mismo document id para insertar un nuevo registro
        eventPost.documentId = FirebaseFirestore.getInstance().collection(Constants.COLL_SUGGEST)
            .document().id
        //hacemos una instancia a la raiz del servidor
        val storageRef = FirebaseStorage.getInstance().reference
            //ponemos como hijo una carpeta donde almacenar las imagenes
            .child(Constants.PATH_SUGGEST_IMAGES)
        //si photoSelectedUri es != de null y binding tb
        _state.value.photoSelectedUri?.let { uri ->

            //hacemos visible el progressbar
            _state.update { _state.value.copy(loading = true) }

            //creamos una nueva referencia que apunta al id de la foto
            val photoRef = storageRef.child(eventPost.documentId.toString())
            //comenzamos a subir la imagen. uri es photoSelectedUri
            photoRef.putFile(uri.toUri())
                //para la barra de progreso al subir la foto
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    val currentprogress = progress.toInt()
                    _state.update { _state.value.copy(progressLoading = currentprogress) }
                }.addOnSuccessListener {
                    //extraemos la url para descargar
                    it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        Log.i("URL", downloadUrl.toString())
                        //la imagen ya ha sido subida al storage con putFile, ahora vamos a insertarla en Firestore
                        eventPost.isSuccess = true
                        eventPost.photoUrl = downloadUrl.toString()
                        callback(eventPost)
                    }
                }
                .addOnFailureListener{
                    //Toast.makeText(activity, "Error al subir imagen.", Toast.LENGTH_SHORT).show()
                    //EMITIMOS ERROR AL SUBIR LA IMAGEN: CREAR ERROR CONCRETO
                    _state.update { _state.value.copy(loading = false, error = Error.NoData) }
                    eventPost.isSuccess = false
                    callback(eventPost)
                }

        }
    }

    data class UiState(
        val loading: Boolean = false,
        val progressLoading: Int = 0,
        val category: String? = null,
        val typeCategory: String? = null,
        val photoSelectedUri: String? = null,
        val nombre: String? = null,
        val descripcion: String? = null,
        val sugerenciaSubidaCorrectamente: Boolean = false,
        val error: Error? = null
    )
}