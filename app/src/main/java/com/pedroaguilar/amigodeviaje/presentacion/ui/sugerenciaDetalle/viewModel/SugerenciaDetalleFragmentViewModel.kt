package com.pedroaguilar.amigodeviaje.presentacion.ui.sugerenciaDetalle.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.common.Error
import com.pedroaguilar.amigodeviaje.modelo.Categorias
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
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
class DetailsFragmentViewModel (val categoriaId: Int, val sugerenciaId: String): ViewModel(){
    private val _state = MutableStateFlow(UiState(loading = true))
    val state = _state.asStateFlow()

    private val firebaseDatabase: ServicioFirebaseDatabase = ServicioFirebaseDatabase()

    fun cargarSugerencia(){
        val categoria = Categorias.values()[categoriaId]
        viewModelScope.launch {
            val sugerenciaAmostrar = firebaseDatabase.obtenerSugerencia(categoria, sugerenciaId)
            if (sugerenciaAmostrar == null){
                _state.update { _state.value.copy(loading = false, error = Error.NoData) }
            }else{
                _state.update { _state.value.copy(loading = false,
                    sugerencia = sugerenciaAmostrar) }
            }
        }
    }

    fun hacerSugerenciaFavorita(){
        viewModelScope.launch {
            _state.value.sugerencia?.let { sugerencia ->
                _state.update { _state.value.copy(loading = true) }
                //Todo proteger sugerencia devuelta como null
                val sugerenciaActualizada = firebaseDatabase.hacerSugerenciaFavoritaParaUser(sugerencia)
                _state.update { _state.value.copy(loading = false, sugerencia = sugerenciaActualizada) }
            }
        }
    }

    fun deshacerSugerenciaFavorita(){
        viewModelScope.launch {
            _state.value.sugerencia?.let { sugerencia ->
                _state.update { _state.value.copy(loading = true) }
                //Todo proteger sugerencia devuelta como null
                _state.update { _state.value.copy(loading = false, sugerencia = firebaseDatabase.quitarSugerenciaFavoritaParaUser(sugerencia)) }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val sugerencia : Sugerencia? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class DetailsFragmentViewModelFactory(private val categoriaId: Int, private val sugerenciaId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsFragmentViewModel(categoriaId, sugerenciaId) as T
    }
}
