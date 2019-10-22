package com.android.itrip.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Viaje(
    var id: Long,

    val nombre: String,

    var inicio: Calendar,

    var fin: Calendar,

    var imagen: String? = null,

    val ciudades_a_visitar: List<CiudadAVisitar> = emptyList()

) : Serializable


data class ViajeCreator(
    var id: Long,
    val nombre: String,
    var inicio: String,
    var fin: String,
    var imagen: String,
    val ciudades_a_visitar: List<CiudadAVisitarCreator> = emptyList()
) : Serializable {
    fun viaje(): Viaje {
        return Viaje(
            id,
            nombre,
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(inicio) },
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(fin) },
            imagen,
            ciudades_a_visitar.map { it.ciudadAVisitar() }
        )
    }
}