package com.android.itrip.models

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


data class ActividadARealizar(
    var id: Long = 0L,
    var dia: Calendar,
    var bucket_inicio: Int,
    var detalle_actividad: Actividad?
) : Serializable

data class ActividadARealizarCreator(
    val id: Long,
    var dia: String,
    var bucket_inicio: Int,
    var detalle_actividad: Actividad
) : Serializable {

    @SuppressLint("SimpleDateFormat")
    fun actividadARealizar(): ActividadARealizar {
        return ActividadARealizar(
            id,
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(dia) },
            bucket_inicio,
            detalle_actividad
        )
    }
}