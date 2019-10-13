/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("Converter")

package com.android.itrip.util

import com.android.itrip.models.Actividad
import kotlin.math.round

fun fromTenthsToSeconds(tenths: Int): String {
    return if (tenths < 600) {
        String.format("%.1f", tenths / 10.0)
    } else {
        val minutes = (tenths / 10) / 60
        val seconds = (tenths / 10) % 60
        String.format("%d:%02d", minutes, seconds)
    }
}

fun fromStringToFloat(string: String?): Float? {
    string?.let { return string.toFloat() }
    return 0.1f
}

fun fromModelDaysToStringDays(actividad: Actividad): String {
    actividad.apply {
        val diasDeSemana =
            disponibilidad_lunes && disponibilidad_martes && disponibilidad_miercoles && disponibilidad_miercoles && disponibilidad_jueves && disponibilidad_viernes
        val finesDeSemana = disponibilidad_sabado && disponibilidad_domingo

        if (diasDeSemana && finesDeSemana) return "Todos los días"
        if (diasDeSemana && !disponibilidad_sabado && !disponibilidad_domingo) return "Días de Semana"
        if (finesDeSemana && !disponibilidad_lunes && !disponibilidad_martes && !disponibilidad_miercoles && !disponibilidad_jueves && !disponibilidad_viernes) return "Fines de Semana"
        val str = StringBuilder()
        if (disponibilidad_lunes) str.append(", Lun")
        if (disponibilidad_martes) str.append(", Mar")
        if (disponibilidad_miercoles) str.append(", Mie")
        if (disponibilidad_jueves) str.append(", Jue")
        if (disponibilidad_viernes) str.append(", Vie")
        if (disponibilidad_sabado) str.append(", Sab")
        if (disponibilidad_domingo) str.append(", Dom")

        str.delete(0, 2)
        str.replace(str.lastIndexOf(", "), str.lastIndexOf(", ") + 1, " y")
        return str.toString()
    }
}

fun fromModelTimeToStringTime(actividad: Actividad): String {
    actividad.apply {
        if (disponibilidad_manana && disponibilidad_tarde && disponibilidad_noche) return "Todo el día"
        if (disponibilidad_manana && !disponibilidad_tarde && !disponibilidad_noche) return "Solo por la mañana"
        if (!disponibilidad_manana && disponibilidad_tarde && !disponibilidad_noche) return "Solo por la tarde"
        if (!disponibilidad_manana && disponibilidad_tarde && disponibilidad_noche) return "Tarde y noche"
        if (disponibilidad_manana && disponibilidad_tarde && !disponibilidad_noche) return "Mañana y tarde"
        if (disponibilidad_manana && !disponibilidad_tarde && disponibilidad_noche) return "Mañana y noche"
    }
    return "Cerrado"
}


fun fromStringToInt(string: String?): Int? {
    return string?.let { string.toInt() }
}

fun cleanSecondsString(seconds: String): Int {
    // Remove letters and other characters
    val filteredValue = seconds.replace(Regex("""[^\d:.]"""), "")
    if (filteredValue.isEmpty()) return 0
    val elements: List<Int> = filteredValue.split(":").map { it -> round(it.toDouble()).toInt() }

    var result: Int
    return when {
        elements.size > 2 -> 0
        elements.size > 1 -> {
            result = elements[0] * 60
            result += elements[1]
            result * 10
        }
        else -> elements[0] * 10
    }
}
