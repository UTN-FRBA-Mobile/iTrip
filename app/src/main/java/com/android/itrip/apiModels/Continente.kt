package com.android.itrip.apiModels

import java.io.Serializable

data class Continente(
    var id: Long,
    val nombre: String,
    val descripcion: String,
    val paises: List<Pais>
) : Serializable