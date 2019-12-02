package com.android.itrip.models

import java.io.Serializable
import java.util.*

data class Viaje(
    var id: Long?,
    var nombre: String?,
    var inicio: Calendar?,
    var fin: Calendar?,
    var imagen: String?,
    var ciudades_a_visitar: List<CiudadAVisitar> = emptyList()
) : Serializable