package com.android.itrip.apiModels

import com.android.itrip.models.Ciudad
import java.io.Serializable

data class Pais(
    var id: Long,
    val nombre: String,
    val descripcion: String,
    val ciudades: List<Ciudad>
) : Serializable