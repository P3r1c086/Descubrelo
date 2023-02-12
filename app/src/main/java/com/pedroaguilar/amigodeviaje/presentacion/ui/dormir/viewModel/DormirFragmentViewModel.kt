package com.pedroaguilar.amigodeviaje.presentacion.ui.dormir.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.model.SugerenciasItems
import com.pedroaguilar.amigodeviaje.servicios.ServicioFirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.dormir.viewModel
 * Create by Pedro Aguilar Fernández on 11/02/2023 at 13:50
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class DormirFragmentViewModel: ViewModel() {

    private val _state = MutableStateFlow(UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun cargarSugerencias(uidUser: String){
        viewModelScope.launch {
            val listaSugerencias = firebaseDatabase.obtenerTodasSugerencias(uidUser)
            if (listaSugerencias.isEmpty()){
                _state.update { _state.value.copy(loading = false, error = Error.NoData) }
            }else{
                _state.update { _state.value.copy(loading = false,
                    sugerencias = SugerenciasItems().defineDatas()) }
            }

        }
    }

    data class UiState(
        val loading: Boolean = false,
        val sugerencias : ArrayList<Sugerencia> = ArrayList(),
        val error: Error? = null
    )
}