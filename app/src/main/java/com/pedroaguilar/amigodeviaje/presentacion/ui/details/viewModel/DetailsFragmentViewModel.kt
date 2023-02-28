package com.pedroaguilar.amigodeviaje.presentacion.ui.details.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.categoriaDetalle.viewModel.CategoriaDetalleFragmentViewModel
import com.pedroaguilar.amigodeviaje.servicios.ServicioFirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.details.viewModel
 * Create by Pedro Aguilar Fernández on 27/02/2023 at 11:37
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class DetailsFragmentViewModel (val categoriaId: Int): ViewModel(){
    private val _state = MutableStateFlow(DetailsFragmentViewModel.UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun cargarSugerencia(){
        //todo: Cargar una sugerencia en concreto
        val categoria = Categorias.values()[categoriaId]
        viewModelScope.launch {
            val idSugerencia = firebaseDatabase.idSugerenciaUser(categoria)
            if (idSugerencia != null){
                val sugerenciaAmostrar = firebaseDatabase.obtenerSugerencia(categoria, idSugerencia)
                if (sugerenciaAmostrar == null){
                    _state.update { _state.value.copy(loading = false, error = Error.NoData) }
                }else{
                    _state.update { _state.value.copy(loading = false,
                        sugerencia = sugerenciaAmostrar) }
                }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val sugerencia : Sugerencia = Sugerencia(),
        val error: Error? = null
    )
}
