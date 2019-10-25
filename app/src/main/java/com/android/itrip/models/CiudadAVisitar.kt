package com.android.itrip.models

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class CiudadAVisitar(
    val id: Long,
    var inicio: Calendar,
    var fin: Calendar,
    var detalle_ciudad: Ciudad?,
    val actividades_a_realizar: List<ActividadARealizar>
) : Serializable

data class CiudadAVisitarCreator(
    val id: Long,
    val inicio: String,
    val fin: String,
    val detalle_ciudad: Ciudad,
    val actividades_a_realizar: List<ActividadARealizarCreator>
) : Serializable {

    @SuppressLint("SimpleDateFormat")
    fun ciudadAVisitar(): CiudadAVisitar {
        return CiudadAVisitar(
            id,
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(inicio) },
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(fin) },
            detalle_ciudad,
            actividades_a_realizar.map { it.actividadARealizar() }
        )
    }
}