package com.android.itrip.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class CiudadAVisitar(
    val id: Long,

    val inicio: Calendar,

    val fin: Calendar,

    val detalle_ciudad: Ciudad,

    val actividades_a_realizar: List<ActividadARealizar>

) : Serializable

data class CiudadAVisitarCreator(
    val id: Long,
    val inicio: String,
    val fin: String,
    val detalle_ciudad: Ciudad,
    val actividades_a_realizar: List<ActividadARealizar>
) : Serializable {

    fun ciudadAVisitar(): CiudadAVisitar {
        return CiudadAVisitar(
            id,
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(inicio) },
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(fin) },
            detalle_ciudad,
            actividades_a_realizar
        )
    }
}