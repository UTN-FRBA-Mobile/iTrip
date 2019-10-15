package com.android.itrip.models

import java.io.Serializable

data class Quiz(
    val genero: String = "",
    val genero_otro: String = "",
    var edad: Int = 0,
    var estado_civil: String = "",
    var nivel_de_estudios: String = "",
    var ocupacion: String = "",
    var hobbies: List<String> = emptyList()
) : Serializable