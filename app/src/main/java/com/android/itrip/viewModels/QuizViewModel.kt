package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.Answer


class QuizViewModel(application: Application) : AndroidViewModel(application) {
    val hobbies: List<Answer>
    val genero: List<Answer>
    val estado_civil: List<Answer>
    val nivel_de_estudios: List<Answer>
    val ocupacion: List<Answer>

    init {
        genero = fillGeneros()
        estado_civil = fillEstadoCivil()
        nivel_de_estudios = fillNivelDeEstudios()
        ocupacion = fillOcupacion()
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
        Answer("D", "Desempleado@"),
        Answer("N", "Ninguna")
    )

    private fun addHobbies() = listOf(
        Answer("LEER", "Leer"),
        Answer("VER_PELICULAS_SERIES_TV", "Ver películas/series/tv"),
        Answer("VER_DEPORTES", "Ver deportes"),
        Answer("RADIO", "Escuchar la radio"),
        Answer("MUSICA", "Escuchar música"),
        Answer("CINE", "Ir al cine"),
        Answer("TEATRO", "Ir al teatro"),
        Answer("RECITALES", "Ir a recitales"),
        Answer("EVENTOS_DEPORTIVOS", "Ir a eventos deportivos"),
        Answer("PLAZA", "Ir a la plaza/parque"),
        Answer("COMER_AFUERA", "Ir a comer a fuera"),
        Answer("SHOPPING", "Ir de shopping"),
        Answer("VIDEOJUEGOS", "Videojuegos"),
        Answer("INTERNET", "Navegar en internet/redes sociales"),
        Answer("BICICLETA_SKATE_ROLLERS", "Andar en bici/skate/rollers"),
        Answer("CORRER_CAMINAR_PASEAR", "Salir a correr/caminar/pasear"),
        Answer("BAR_BOLICHE", "Salir a un bar/boliche"),
        Answer("MASCOTA", "Pasear a tu mascota"),
        Answer("INSTRUMENTO", "Tocar un instrumento"),
        Answer("BAILAR", "Bailar"),
        Answer("COCINAR", "Cocinar"),
        Answer("JARDINERIA", "Jardinería"),
        Answer("JUEGOS_DE_MESA", "Juegos de mesa"),
        Answer("JUEGOS_DE_APUESTAS", "Juegos de apuestas"),
        Answer("PINTAR_DIBUJAR", "Pintar/dibujar"),
        Answer("ARTESANIAS", "Hacer artesanías"),
        Answer("EJERCICIO_DEPORTES", "Hacer ejercicio/deportes"),
        Answer("COSER_TEJER", "Coser/tejer"),
        Answer("COLECCIONAR_COSAS", "Coleccionar cosas"),
        Answer("FOTOGRAFIA", "Fotografía"),
        Answer("MEDITACION", "Meditación"),
        Answer("AMIGOS", "Juntarse con amigos/as"),
        Answer("FAMILIA", "Pasar el tiempo con la familia")
    )

}