package com.android.itrip.apiModels

import android.annotation.SuppressLint
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.util.calendarToString
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class ActividadARealizarApiModel(
    val id: Long,
    var dia: String,
    var bucket_inicio: Int,
    var detalle_actividad: Actividad
) : Serializable {
    @SuppressLint("SimpleDateFormat")
    fun actividadARealizar(): ActividadARealizar {
        return ActividadARealizar(
            id,
            Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd").parse(dia)!! },
            bucket_inicio,
            detalle_actividad
        )
    }

    constructor(actividadARealizar: ActividadARealizar) : this(
        actividadARealizar.id,
        calendarToString(actividadARealizar.dia, "yyyy-MM-dd"),
        actividadARealizar.bucket_inicio,
        actividadARealizar.detalle_actividad
    )

}