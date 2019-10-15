package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.Answer
import com.android.itrip.models.Quiz
import com.android.itrip.services.QuizService


class QuizViewModel(
    application: Application
) : AndroidViewModel(application) {
    val hobbies: List<Answer>
    val genero: List<Answer>
    var genero_otro: String = ""
    var edad: Int = 0
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

    private fun fillGeneros(): MutableList<Answer> {
        val mutableList: MutableList<Answer> = mutableListOf()
        mutableList.add(Answer("M", "Masculino"))
        mutableList.add(Answer("F", "Femenino"))
        mutableList.add(Answer("O", "Otro"))
        return mutableList
    }

    private fun fillEstadoCivil(): MutableList<Answer> {
        val mutableList: MutableList<Answer> = mutableListOf()
        mutableList.add(Answer("So", "Solter@"))
        mutableList.add(Answer("Co", "En concubinat@"))
        mutableList.add(Answer("Ca", "Casad@"))
        mutableList.add(Answer("CD", "Divorciad@"))
        mutableList.add(Answer("Se", "Separad@"))
        mutableList.add(Answer("V", "Viud@"))
        return mutableList
    }

    private fun fillNivelDeEstudios(): MutableList<Answer> {
        val mutableList: MutableList<Answer> = mutableListOf()
        mutableList.add(Answer("SI", "Secundario incompleto"))
        mutableList.add(Answer("SC", "Secundario completo"))
        mutableList.add(Answer("TI", "Terciario incompleto"))
        mutableList.add(Answer("TC", "Terciario completo"))
        mutableList.add(Answer("UI", "Universitario incompleto"))
        mutableList.add(Answer("UC", "Universitario completo"))
        mutableList.add(Answer("P", "Postgrado/master/doctorado"))
        return mutableList
    }


    private fun fillOcupacion(): MutableList<Answer> {
        val mutableList: MutableList<Answer> = mutableListOf()
        mutableList.add(Answer("E", "Estudiante"))
        mutableList.add(Answer("T", "Trabajador"))
        mutableList.add(Answer("ET", "Estudiante y trabajador"))
        mutableList.add(Answer("J", "Jubilad@"))
        mutableList.add(Answer("D", "Desempleado@"))
        mutableList.add(Answer("N", "Ninguna"))
        return mutableList
    }


    private fun addHobbies(): MutableList<Answer> {
        val mutableHobbies: MutableList<Answer> = mutableListOf()
        mutableHobbies.add(Answer("LEER", "Leer"))
        mutableHobbies.add(Answer("VER_PELICULAS_SERIES_TV", "Ver películas/series/tv"))
        mutableHobbies.add(Answer("VER_DEPORTES", "Ver deportes"))
        mutableHobbies.add(Answer("RADIO", "Escuchar la radio"))
        mutableHobbies.add(Answer("MUSICA", "Escuchar música"))
        mutableHobbies.add(Answer("CINE", "Ir al cine"))
        mutableHobbies.add(Answer("TEATRO", "Ir al teatro"))
        mutableHobbies.add(Answer("RECITALES", "Ir a recitales"))
        mutableHobbies.add(Answer("EVENTOS_DEPORTIVOS", "Ir a eventos deportivos"))
        mutableHobbies.add(Answer("PLAZA", "Ir a la plaza/parque"))
        mutableHobbies.add(Answer("COMER_AFUERA", "Ir a comer a fuera"))
        mutableHobbies.add(Answer("SHOPPING", "Ir de shopping"))
        mutableHobbies.add(Answer("VIDEOJUEGOS", "Videojuegos"))
        mutableHobbies.add(Answer("INTERNET", "Navegar en internet/redes sociales"))
        mutableHobbies.add(Answer("BICICLETA_SKATE_ROLLERS", "Andar en bici/skate/rollers"))
        mutableHobbies.add(Answer("CORRER_CAMINAR_PASEAR", "Salir a correr/caminar/pasear"))
        mutableHobbies.add(Answer("BAR_BOLICHE", "Salir a un bar/boliche"))
        mutableHobbies.add(Answer("MASCOTA", "Pasear a tu mascota"))
        mutableHobbies.add(Answer("INSTRUMENTO", "Tocar un instrumento"))
        mutableHobbies.add(Answer("BAILAR", "Bailar"))
        mutableHobbies.add(Answer("COCINAR", "Cocinar"))
        mutableHobbies.add(Answer("JARDINERIA", "Jardinería"))
        mutableHobbies.add(Answer("JUEGOS_DE_MESA", "Juegos de mesa"))
        mutableHobbies.add(Answer("JUEGOS_DE_APUESTAS", "Juegos de apuestas"))
        mutableHobbies.add(Answer("PINTAR_DIBUJAR", "Pintar/dibujar"))
        mutableHobbies.add(Answer("ARTESANIAS", "Hacer artesanías"))
        mutableHobbies.add(Answer("EJERCICIO_DEPORTES", "Hacer ejercicio/deportes"))
        mutableHobbies.add(Answer("COSER_TEJER", "Coser/tejer"))
        mutableHobbies.add(Answer("COLECCIONAR_COSAS", "Coleccionar cosas"))
        mutableHobbies.add(Answer("FOTOGRAFIA", "Fotografía"))
        mutableHobbies.add(Answer("MEDITACION", "Meditación"))
        mutableHobbies.add(Answer("AMIGOS", "Juntarse con amigos/as"))
        mutableHobbies.add(Answer("FAMILIA", "Pasar el tiempo con la familia"))
        return mutableHobbies
    }

    fun sendQuiz(checkedHobbies: List<Answer>, callback: () -> Unit) {
        val quiz = Quiz("M", "", 29, "So", "SI", "ET", checkedHobbies.map { it.key })
        QuizService.postQuestions(quiz, callback, {})
    }

}