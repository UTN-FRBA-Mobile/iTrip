package com.android.itrip.models

import java.io.Serializable

data class Quiz(
    var genero: Answer?,
    var genero_otro: String? = null,
    var edad: Int?,
    var estado_civil: Answer?,
    var nivel_de_estudios: Answer?,
    var ocupacion: Answer?,
    var hobbies: List<Answer> = listOf()
) : Serializable {
    constructor() : this(null, null, null, null, null, null, listOf())
}