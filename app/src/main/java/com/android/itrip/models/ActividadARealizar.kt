package com.android.itrip.models

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class ActividadARealizar(
    var id: Long = 0L,
    var dia: Calendar,
    var bucket_inicio: Int,
    var detalle_actividad: Actividad
) : Serializable

