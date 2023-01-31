package com.pedroaguilar.amigodeviaje.presentacion.ui.add.viewModel

import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
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
    private val _state = MutableStateFlow(UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun registrarSugerenciaEnFirestore(uidUser: String, category: String, typeCategory: String,
                                              nombre: String, descripcion: String,
                                              imgUrl: String){
        viewModelScope.launch {
            val sugerenciaPorUsuario = firebaseDatabase.registrarSugerencia(uidUser,
            Sugerencia(uidUser, category, typeCategory ,nombre, descripcion, imgUrl))
            if (sugerenciaPorUsuario != null){
                _state.update {
                    _state.value.copy(
                        loading = false,
                        sugerencia = sugerenciaPorUsuario
                    )
                }
            } else {
                _state.update { _state.value.copy(loading = false, error = Error.Server(456)) }
            }
        }
    }


    data class UiState(
        val loading: Boolean = false,
        val sugerencia: Sugerencia? = null,
        val error: Error? = null
    )
}