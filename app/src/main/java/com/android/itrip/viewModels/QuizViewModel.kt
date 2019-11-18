package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.Answer
import com.android.itrip.models.Quiz
import com.android.itrip.services.ApiError
import com.android.itrip.services.QuizService


class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val quizService = QuizService(application.applicationContext)
    val hobbies: List<Answer>
    val generos: List<Answer>
    val estados_civil: List<Answer>
    val niveles_de_estudio: List<Answer>
    val ocupaciones: List<Answer>
    var quiz: Quiz = Quiz()

    init {
        generos = fillGeneros()
        estados_civil = fillEstadoCivil()
        niveles_de_estudio = fillNivelDeEstudios()
        ocupaciones = fillOcupacion()
        hobbies = addHobbies().toList()
    }

    private fun fillGeneros() = listOf(
        Answer("M", "Masculino"),
        Answer("F", "Femenino"),
        Answer("O", "Otro")
    )

    private fun fillEstadoCivil() = listOf(
        Answer("So", "Solter@"),
        Answer("Co", "En concubinato"),
        Answer("Ca", "Casad@"),
        Answer("D", "Divorciad@"),
        Answer("Se", "Separad@"),
        Answer("V", "Viud@")
    )

    private fun fillNivelDeEstudios() = listOf(
        Answer("SI", "Secundario incompleto"),
        Answer("SC", "Secundario completo"),
        Answer("TI", "Terciario incompleto"),
        Answer("TC", "Terciario completo"),
        Answer("UI", "Universitario incompleto"),
        Answer("UC", "Universitario completo"),
        Answer("P", "Postgrado/master/doctorado")
    )

    private fun fillOcupacion() = listOf(
        Answer("E", "Estudiante"),
        Answer("T", "Trabajador"),
        Answer("ET", "Estudiante y trabajador"),
        Answer("J", "Jubilad@"),
        Answer("D", "Desemplead@"),
        Answer("N", "Ninguna")
    )

    private fun addHobbies() = listOf(
        Answer("BICICLETA_SKATE_ROLLERS", "Andar en bici/skate/rollers"),
        Answer("BAILAR", "Bailar"),
        Answer("COCINAR", "Cocinar"),
        Answer("COLECCIONAR_COSAS", "Coleccionar cosas"),
        Answer("COSER_TEJER", "Coser/tejer"),
        Answer("RADIO", "Escuchar la radio"),
        Answer("MUSICA", "Escuchar música"),
        Answer("FOTOGRAFIA", "Fotografía"),
        Answer("ARTESANIAS", "Hacer artesanías"),
        Answer("EJERCICIO_DEPORTES", "Hacer ejercicio/deportes"),
        Answer("COMER_AFUERA", "Ir a comer a fuera"),
        Answer("EVENTOS_DEPORTIVOS", "Ir a eventos deportivos"),
        Answer("PLAZA", "Ir a la plaza/parque"),
        Answer("RECITALES", "Ir a recitales"),
        Answer("CINE", "Ir al cine"),
        Answer("TEATRO", "Ir al teatro"),
        Answer("SHOPPING", "Ir de shopping"),
        Answer("JARDINERIA", "Jardinería"),
        Answer("JUEGOS_DE_APUESTAS", "Juegos de apuestas"),
        Answer("JUEGOS_DE_MESA", "Juegos de mesa"),
        Answer("AMIGOS", "Juntarse con amigos/as"),
        Answer("LEER", "Leer"),
        Answer("MEDITACION", "Meditación"),
        Answer("INTERNET", "Navegar en internet/redes sociales"),
        Answer("FAMILIA", "Pasar el tiempo con la familia"),
        Answer("MASCOTA", "Pasear a tu mascota"),
        Answer("PINTAR_DIBUJAR", "Pintar/dibujar"),
        Answer("CORRER_CAMINAR_PASEAR", "Salir a correr/caminar/pasear"),
        Answer("BAR_BOLICHE", "Salir a un bar/boliche"),
        Answer("INSTRUMENTO", "Tocar un instrumento"),
        Answer("VER_DEPORTES", "Ver deportes"),
        Answer("VER_PELICULAS_SERIES_TV", "Ver películas/series/tv"),
        Answer("VIDEOJUEGOS", "Videojuegos")
    )

    fun postAnswers(postAnswerSuccess: () -> Unit?, errorHandler: (ApiError) -> Unit) {
        quizService.postAnswers(quiz, {
            postAnswerSuccess()
        }, {
            errorHandler(it)
        })
    }

}