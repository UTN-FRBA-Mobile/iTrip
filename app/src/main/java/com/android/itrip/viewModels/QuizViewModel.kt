package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.database.QuestionDatabaseDao
import com.android.itrip.fragments.QuizHomeFragment
import java.util.logging.Logger


class QuizViewModel(
    var database: QuestionDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    //private val logger = Logger.getLogger(QuizHomeFragment::class.java.name)

    /*override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }*/


    data class Question(
        val text: String,
        val answers: List<String>)

    //private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //lateinit var questions: LiveData<List<Question>>
    lateinit var questions: List<Question>

    //var question: Question = Question(3,"vamos a terminar el tp")
   // var answer:Answer = Answer(1,3,"si",false)
    //private var _query = MutableLiveData<String>()
    //private val query: LiveData<String>
   //     get() = _query

    init {

        questions = mutableListOf(
            Question(text = "Genero",
                answers = listOf("Masculino", "Femenino", "Otros")),
            Question(text = "Edad",
                answers = listOf("")),
            Question(text = "Estado Civil",
                answers = listOf("Soltero/a", "En concubinato", "Casado/a", "Divorciado/a","Separado/a","Viudo/a")),
            Question(text = "Nivel de Estudios",
                answers = listOf("Secundario Incompleto", "Secundario Completo", "Terciario incompleto", "Universitario incompleto","Universitario completo","Postgrado/master/doctorado")),
            Question(text = "Ocupación",
                answers = listOf("Estudiante", "Trabajador", "Estudiante y trabajador", "Jubilado/a","Desempleado/a","Ninguna")),
            Question(text = "Build system for Android?",
                answers = listOf("Leer","Ver películas/series/tv","Ver deportes","Escuchar la radio",
                    "Escuchar música","Ir al cine","Ir al teatro","Ir a recitales","Ir a eventos deportivos",
                    "Ir a la plaza/parque","Ir a comer afuera","Ir de shopping","Videojuegos",
                    "Navegar en internet/redes sociales","Andar en bici/skate/rollers","Salir a correr/caminar/pasear",
                    "Salir a un bar/boliche","Pasear a tu mascota","Tocar un instrumento","Bailar","Cocinar","Jardinería",
                    "Juegos de mesa","Juegos de apuestas","Pintar/dibujar","Hacer artesanías","Hacer ejercicio/deportes",
                    "Coser/tejer","Coleccionar cosas","Fotografía","Meditación","Juntarse con amigos/as","Pasar tiempo con la familia"))
        )
        /*
       _query.value = ""
        uiScope.launch {
            clear()
            //TODO get questions from DB (API)

            insertQ(Question(1, "vamos a llegar a hacer el TP?"))
            insertA(Answer(1,1,"si",false))
            insertA(Answer(2,1,"no",false))

        }
        questions = Transformations.switchMap(query) { query -> updateLiveData(query) }*/
    }

    /*
    private suspend fun insertQ(question: Question) {
        withContext(Dispatchers.IO) {
            database.insertQuestion(question)
        }
    }
    private suspend fun insertA(answer: Answer) {
        withContext(Dispatchers.IO) {
            database.insertAnswer(answer)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }*/

      /*fun updateResults(query: String) {
           logger.info("Query: " + query)
           this._query.value = query
           logger.info("Query livedata: " + this.query.value!!)
           try {
               if (destinations != null)
                   logger.info("destinations: " + destinations.value)
           } catch (e: Exception) {
               logger.info(e.toString())
           }
       }*/

   /* private fun updateLiveData(query: String?): LiveData<List<Question>>? {
      return database.getAllQuestions()
    }*/
}