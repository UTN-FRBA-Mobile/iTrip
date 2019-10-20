package com.android.itrip.models

import java.io.Serializable

data class Viaje(
    var id: Long = 0L,
    val nombre: String = "",
    var inicio: String = "",
    var fin: String = "",
    var imagen: String? = null,
    val ciudades_a_visitar: List<Ciudad> = emptyList()
) : Serializable