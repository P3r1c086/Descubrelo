package com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.model.SugerenciasItems
import com.pedroaguilar.amigodeviaje.servicios.ServicioFirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 20:40
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class HomeFragmentViewModel: ViewModel() {

    private val _state = MutableStateFlow(UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    init {
        viewModelScope.launch {
            _state.update { _state.value.copy(loading = false,
                categorias = SugerenciasItems().defineDatas()) }
        }

    }

    fun obtenerTodasLasSugerencias(){
        viewModelScope.launch {
            val listaSugerencias = ArrayList<Sugerencia>()
            Categorias.values().forEach {
                val listaSugerenciasPorCategoria = firebaseDatabase.obtenerTodasSugerencias(it)
                if (listaSugerenciasPorCategoria.isNotEmpty()){
                    listaSugerencias.addAll(listaSugerenciasPorCategoria)
                }
            }
            _state.update { _state.value.copy(loading = false,
                sugerencias = listaSugerencias) }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val categorias : ArrayList<Sugerencia> = ArrayList(),
        val sugerencias : ArrayList<Sugerencia> = ArrayList(),
        val error: Error? = null
    )
}