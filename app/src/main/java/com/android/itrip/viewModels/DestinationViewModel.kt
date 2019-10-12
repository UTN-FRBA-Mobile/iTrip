package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabaseDao
import com.android.itrip.models.Pais
import com.android.itrip.services.TravelService
import kotlinx.coroutines.*
import java.util.logging.Logger


class DestinationViewModel(
    val database: DestinationDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val destinations: LiveData<List<Destination>>

    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query


    init {
        TravelService.getDestinations({ paises ->
            getDestinationsCallback(paises)
        }, {}
        )

        _query.value = ""
        destinations = Transformations.switchMap(query) { query -> updateLiveData(query) }
    }

    private fun getDestinationsCallback(paises: List<Pais>) {
        logger.info("Cantidad de Paises: " + paises.size.toString())
        uiScope.launch {
            clear()
            paises.forEach {
                logger.info("Pais: " + it.nombre)
                it.ciudades.forEach { ciudad -> insert(Destination(ciudad.id, ciudad.nombre)) }
            }
        }
    }

    private suspend fun insert(destination: Destination) {
        withContext(Dispatchers.IO) {
            database.insert(destination)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun updateResults(query: String) {
        _query.value = query
    }

    private fun updateLiveData(query: String?): LiveData<List<Destination>>? {
        return database.getDestinationsByName("$query%")
    }
}