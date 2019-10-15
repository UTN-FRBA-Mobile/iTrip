package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.Hobbie


class QuizViewModel(
    application: Application
) : AndroidViewModel(application) {
    val hobbies: List<Hobbie>

    init {
        hobbies = addHobbies().toList()
    }

    private fun addHobbies(): MutableList<Hobbie> {
        val mutableHobbies: MutableList<Hobbie> = mutableListOf()
        mutableHobbies.add(Hobbie("LEER", "Leer"))
        mutableHobbies.add(Hobbie("VER_PELICULAS_SERIES_TV", "Ver películas/series/tv"))
        mutableHobbies.add(Hobbie("VER_DEPORTES", "Ver deportes"))
        mutableHobbies.add(Hobbie("RADIO", "Escuchar la radio"))
        mutableHobbies.add(Hobbie("MUSICA", "Escuchar música"))
        mutableHobbies.add(Hobbie("CINE", "Ir al cine"))
        mutableHobbies.add(Hobbie("TEATRO", "Ir al teatro"))
        mutableHobbies.add(Hobbie("RECITALES", "Ir a recitales"))
        mutableHobbies.add(Hobbie("EVENTOS_DEPORTIVOS", "Ir a eventos deportivos"))
        mutableHobbies.add(Hobbie("PLAZA", "Ir a la plaza/parque"))
        mutableHobbies.add(Hobbie("COMER_AFUERA", "Ir a comer a fuera"))
        mutableHobbies.add(Hobbie("SHOPPING", "Ir de shopping"))
        mutableHobbies.add(Hobbie("VIDEOJUEGOS", "Videojuegos"))
        mutableHobbies.add(Hobbie("INTERNET", "Navegar en internet/redes sociales"))
        mutableHobbies.add(Hobbie("BICICLETA_SKATE_ROLLERS", "Andar en bici/skate/rollers"))
        mutableHobbies.add(Hobbie("CORRER_CAMINAR_PASEAR", "Salir a correr/caminar/pasear"))
        mutableHobbies.add(Hobbie("BAR_BOLICHE", "Salir a un bar/boliche"))
        mutableHobbies.add(Hobbie("MASCOTA", "Pasear a tu mascota"))
        mutableHobbies.add(Hobbie("INSTRUMENTO", "Tocar un instrumento"))
        mutableHobbies.add(Hobbie("BAILAR", "Bailar"))
        mutableHobbies.add(Hobbie("COCINAR", "Cocinar"))
        mutableHobbies.add(Hobbie("JARDINERIA", "Jardinería"))
        mutableHobbies.add(Hobbie("JUEGOS_DE_MESA", "Juegos de mesa"))
        mutableHobbies.add(Hobbie("JUEGOS_DE_APUESTAS", "Juegos de apuestas"))
        mutableHobbies.add(Hobbie("PINTAR_DIBUJAR", "Pintar/dibujar"))
        mutableHobbies.add(Hobbie("ARTESANIAS", "Hacer artesanías"))
        mutableHobbies.add(Hobbie("EJERCICIO_DEPORTES", "Hacer ejercicio/deportes"))
        mutableHobbies.add(Hobbie("COSER_TEJER", "Coser/tejer"))
        mutableHobbies.add(Hobbie("COLECCIONAR_COSAS", "Coleccionar cosas"))
        mutableHobbies.add(Hobbie("FOTOGRAFIA", "Fotografía"))
        mutableHobbies.add(Hobbie("MEDITACION", "Meditación"))
        mutableHobbies.add(Hobbie("AMIGOS", "Juntarse con amigos/as"))
        mutableHobbies.add(Hobbie("FAMILIA", "Pasar el tiempo con la familia"))
        return mutableHobbies
    }

}