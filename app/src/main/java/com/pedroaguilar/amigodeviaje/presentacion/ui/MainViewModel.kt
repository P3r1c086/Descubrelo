package com.pedroaguilar.amigodeviaje.presentacion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.common.entities.Usuario
import com.pedroaguilar.amigodeviaje.servicios.ServicioFirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun registrarUsuarioEnFirebaseDatabase(uidUser: String, nombre: String) {
        viewModelScope.launch {
            val usuarioRecienRegistrado = firebaseDatabase.registrarUsuarioEnBdPropia(uidUser,
                Usuario(uidUser, nombre))
            if (usuarioRecienRegistrado != null) {
                _state.update {
                    _state.value.copy(
                        loading = false,
                        usuario = usuarioRecienRegistrado
                    )
                }
            } else {
                _state.update { _state.value.copy(loading = false, error = Error.Server(456)) }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val usuario: Usuario? = null,
        val error: Error? = null
    )
}