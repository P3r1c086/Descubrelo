package com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroaguilar.amigodeviaje.modelo.entities.Sugerencia
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.model.SugerenciasItems
import kotlinx.coroutines.launch

/**
 * Proyect: Amigo De Viaje
 * From: com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel
 * Create by Pedro Aguilar Fernández on 02/02/2023 at 20:40
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2023
 **/
class HomeFragmentViewModel: ViewModel() {

    private val _state : MutableLiveData<ArrayList<Sugerencia>> = MutableLiveData()
    val state : MutableLiveData<ArrayList<Sugerencia>> = _state
    val c = SugerenciasItems()

    init {
        viewModelScope.launch {
            _state.postValue(c.defineDatas())
        }

    }
}