package com.android.itrip.apiModels

import android.annotation.SuppressLint
import com.android.itrip.models.Ciudad
import com.android.itrip.models.CiudadAVisitar
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class CiudadAVisitarApiModel(
    val id: Long,
    val inicio: String,
    val fin: String,
    val detalle_ciudad: Ciudad,
    val actividades_a_realizar: List<ActividadARealizarApiModel>
) : Serializable {

    @SuppressLint("SimpleDateFormat")
    fun ciudadAVisitar(): CiudadAVisitar {
        return CiudadAVisitar(
            id,
            Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd").parse(inicio)
            },
            Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd").parse(fin)
            },
            detalle_ciudad,
            actividades_a_realizar.map { it.actividadARealizar() }
        )
    }
}