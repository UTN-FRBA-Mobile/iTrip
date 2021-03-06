package com.android.itrip.apiModels

import android.annotation.SuppressLint
import com.android.itrip.models.Viaje
import com.android.itrip.util.calendarToString
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class ViajeApiModel(
    var id: Long?,
    val nombre: String?,
    var inicio: String?,
    var fin: String?,
    var imagen: String?,
    val ciudades_a_visitar: List<CiudadAVisitarApiModel>
) : Serializable {
    @SuppressLint("SimpleDateFormat")
    fun viaje(): Viaje {
        return Viaje(
            id,
            nombre,
            Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd").parse(inicio!!)!!
            },
            Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd").parse(fin!!)!!
            },
            imagen,
            ciudades_a_visitar.map { it.ciudadAVisitar() }
        )
    }

    constructor(viaje: Viaje) : this(
        viaje.id,
        viaje.nombre,
        calendarToString(viaje.inicio, "yyyy-MM-dd"),
        calendarToString(viaje.fin, "yyyy-MM-dd"),
        viaje.imagen,
        viaje.ciudades_a_visitar.map { CiudadAVisitarApiModel(it) }
    )
}