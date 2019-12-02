package com.android.itrip.models

import java.io.Serializable
import java.util.*


data class ActividadARealizar(
    var id: Long = 0L,
    var dia: Calendar,
    var bucket_inicio: Int,
    var detalle_actividad: Actividad
) : Serializable

