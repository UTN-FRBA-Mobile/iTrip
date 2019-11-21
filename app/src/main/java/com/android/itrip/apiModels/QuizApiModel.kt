package com.android.itrip.apiModels

import com.android.itrip.models.Quiz
import java.io.Serializable

data class QuizApiModel(
    var genero: String,
    var genero_otro: String?,
    var edad: Int,
    var estado_civil: String,
    var nivel_de_estudios: String,
    var ocupacion: String,
    var hobbies: List<String>
) : Serializable {
    constructor(quiz: Quiz) : this(
        quiz.genero!!.key,
        quiz.genero_otro,
        quiz.edad!!,
        quiz.estado_civil!!.key,
        quiz.nivel_de_estudios!!.key,
        quiz.ocupacion!!.key,
        quiz.hobbies.map { it.key }
    )
}