package com.android.itrip.models

import com.android.itrip.util.calendarToString
import java.io.Serializable
import java.util.*

data class Bucket(
    var ciudadAVisitar: CiudadAVisitar,
    var dia: String,
    var bucket_inicio: Int
) : Serializable {
    constructor(ciudadAVisitar: CiudadAVisitar, dia: Calendar, bucket_inicio: Int) : this(
        ciudadAVisitar,
        calendarToString(dia,"yyyy-MM-dd"),
        bucket_inicio
    )
}
