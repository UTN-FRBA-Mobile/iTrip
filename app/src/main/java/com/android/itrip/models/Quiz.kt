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

    fun toQuizApiModel(): QuizApiModel {
        return QuizApiModel(
            genero?.key,
            genero_otro,
            edad,
            estado_civil?.key,
            nivel_de_estudios?.key,
            ocupacion?.key,
            hobbies.map { it.key })
    }
}

data class QuizApiModel(
    var genero: String?,
    var genero_otro: String? = null,
    var edad: Int?,
    var estado_civil: String?,
    var nivel_de_estudios: String?,
    var ocupacion: String?,
    var hobbies: List<String> = listOf()
) : Serializable