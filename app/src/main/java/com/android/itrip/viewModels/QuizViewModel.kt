package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.database.QuestionDatabaseDao


class QuizViewModel(
    var database: QuestionDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    data class Question(
        val text: String,
        val answers: List<String>
    )

    val questions: List<Question>
    var ages = (1..100).toList()

    init {

        questions = listOf(
            Question(
                text = "Genero",
                answers = listOf("Masculino", "Femenino", "Otros")
            ),
            Question(text = "Edad",
                answers = ages.map { it.toString() }
            ),
            Question(
                text = "Estado Civil",
                answers = listOf(
                    "Soltero/a",
                    "En concubinato",
                    "Casado/a",
                    "Divorciado/a",
                    "Separado/a",
                    "Viudo/a"
                )
            ),
            Question(
                text = "Nivel de Estudios",
                answers = listOf(
                    "Secundario Incompleto",
                    "Secundario Completo",
                    "Terciario incompleto",
                    "Universitario incompleto",
                    "Universitario completo",
                    "Postgrado/master/doctorado"
                )
            ),
            Question(
                text = "Ocupación",
                answers = listOf(
                    "Estudiante",
                    "Trabajador",
                    "Estudiante y trabajador",
                    "Jubilado/a",
                    "Desempleado/a",
                    "Ninguna"
                )
            ),
            Question(
                text = "Build system for Android?",
                answers = listOf(
                    "Leer",
                    "Ver películas/series/tv",
                    "Ver deportes",
                    "Escuchar la radio",
                    "Escuchar música",
                    "Ir al cine",
                    "Ir al teatro",
                    "Ir a recitales",
                    "Ir a eventos deportivos",
                    "Ir a la plaza/parque",
                    "Ir a comer afuera",
                    "Ir de shopping",
                    "Videojuegos",
                    "Navegar en internet/redes sociales",
                    "Andar en bici/skate/rollers",
                    "Salir a correr/caminar/pasear",
                    "Salir a un bar/boliche",
                    "Pasear a tu mascota",
                    "Tocar un instrumento",
                    "Bailar",
                    "Cocinar",
                    "Jardinería",
                    "Juegos de mesa",
                    "Juegos de apuestas",
                    "Pintar/dibujar",
                    "Hacer artesanías",
                    "Hacer ejercicio/deportes",
                    "Coser/tejer",
                    "Coleccionar cosas",
                    "Fotografía",
                    "Meditación",
                    "Juntarse con amigos/as",
                    "Pasar tiempo con la familia"
                )
            )
        )

    }

}