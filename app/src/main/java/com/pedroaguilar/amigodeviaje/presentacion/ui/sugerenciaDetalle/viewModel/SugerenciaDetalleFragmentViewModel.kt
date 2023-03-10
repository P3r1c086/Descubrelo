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
    fun actualizarSugerencia(uidUser: String, category: Categorias?, typeCategory: String?,
                             nombre: String?, descripcion: String?,
                             imgUrl: String?, listIdUser: ArrayList<String>?){
        viewModelScope.launch {
            val idSugerencia = firebaseDatabase.idSugerenciaUser(category)
            if (idSugerencia == null){
                _state.update { _state.value.copy(
                    loading = false,
                    error = Error.Server(456),
                    cbFavorite = false)
                }
            } else {
                val sugerenciaPorUsuario = firebaseDatabase.actualizarSugerencia(
                    Sugerencia(
                        id = idSugerencia,
                        perteneceAUsuario = uidUser,
                        category = category,
                        typeCategory = typeCategory,
                        name = nombre,
                        description = descripcion,
                        imgUrl = imgUrl,
                        listaFavoritosIdUsuarios = listIdUser
                    ),
                    idSugerencia
                )
                if (sugerenciaPorUsuario != null) {
                    _state.update {
                        _state.value.copy(
                            loading = false,
                            cbFavorite = true
                        )
                    }
                } else {
                    _state.update { _state.value.copy(
                        loading = false,
                        error = Error.Server(456),
                        cbFavorite = false)
                    }
                }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val sugerencia : Sugerencia? = null,
        val cbFavorite : Boolean? = null,
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
