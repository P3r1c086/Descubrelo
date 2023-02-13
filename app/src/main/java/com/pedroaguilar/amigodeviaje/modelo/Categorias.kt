package com.pedroaguilar.amigodeviaje.modelo

import com.pedroaguilar.amigodeviaje.servicios.Constantes

enum class Categorias(val value: String) {
    COMER(Constantes.COMER),
    DORMIR(Constantes.DORMIR),
    FIESTA(Constantes.FIESTA),
    TURISMO(Constantes.TURISMO),
    AVENTURA(Constantes.AVENTURA)
}
