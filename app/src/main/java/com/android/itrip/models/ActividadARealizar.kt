package com.android.itrip.models

import java.io.Serializable


data class ActividadARealizar(
    var id: Long = 0L,

    var dia: String = "",

    var bucket_inicio: Int = 0,

    var detalle_actividad: Actividad

) : Serializable