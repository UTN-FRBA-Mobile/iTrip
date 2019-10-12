package com.android.itrip.viewModels

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.database.Answer
import com.android.itrip.database.Question
import com.android.itrip.database.QuestionDatabaseDao
import com.android.itrip.fragments.QuizHomeFragment
import kotlinx.coroutines.*
import java.util.logging.Logger
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class QuizViewModel(
    var database: QuestionDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(QuizHomeFragment::class.java.name)


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val questions: LiveData<List<Question>>
    var question: Question = Question(3,"vamos a terminar el tp")
    var answer:Answer = Answer(1,3,"si",false)
    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query

    init {


       _query.value = ""
        uiScope.launch {
            clear()
            //TODO get questions from DB (API)

            insertQ(Question(1, "vamos a llegar a hacer el TP?"))
            insertA(Answer(1,1,"si",false))
            insertA(Answer(2,1,"no",false))

        }
            questions = Transformations.switchMap(query) { query -> updateLiveData(query) }
    }

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
    }

 /*   fun updateResults(query: String) {
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

    private fun updateLiveData(query: String?): LiveData<List<Question>>? {
        return database.getAllQuestions()
    }
}