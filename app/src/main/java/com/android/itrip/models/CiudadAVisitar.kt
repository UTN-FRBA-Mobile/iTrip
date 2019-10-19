package com.android.itrip.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class CiudadAVisitar(
    var id: Long = 0L,

    var inicio: Calendar,

    var fin: Calendar,

    var detalle_ciudad: Ciudad,

    var actividades_a_realizar: List<ActividadARealizar>

) : Serializable

data class CiudadAVisitarCreator(
    var id: Long,
    var inicio: String,
    var fin: String,
    var detalle_ciudad: Ciudad,
    var actividades_a_realizar: List<ActividadARealizar>
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