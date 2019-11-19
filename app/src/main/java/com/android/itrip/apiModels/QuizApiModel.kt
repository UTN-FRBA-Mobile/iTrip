package com.android.itrip.apiModels

import java.io.Serializable

data class QuizApiModel(
    var genero: String?,
    var genero_otro: String?,
    var edad: Int?,
    var estado_civil: String?,
    var nivel_de_estudios: String?,
    var ocupacion: String?,
    var hobbies: List<String>
) : Serializable