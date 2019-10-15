package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabaseDao
import com.android.itrip.models.Continente
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

    init {
        TravelService.getDestinations({ continentes ->
            getDestinationsCallback(continentes)
        }, {}
        )
        destinations = database.getAll()
    }

    private fun getDestinationsCallback(continentes: List<Continente>) {
        logger.info("Cantidad de Continentes: " + continentes.size.toString())
        uiScope.launch {
            clear()
            continentes.forEach {
                logger.info("Continente: " + it.nombre)
                it.paises.forEach { pais ->
                    pais.ciudades.forEach { ciudad ->
                        insert(Destination(ciudad.id, ciudad.nombre))
                    }
                }
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

}