package com.android.itrip.models

import java.io.Serializable
import java.util.*


data class CiudadAVisitar(
    val id: Long,
    var inicio: Calendar,
    var fin: Calendar,
    var detalle_ciudad: Ciudad?,
    val actividades_a_realizar: List<ActividadARealizar>
) : Serializable