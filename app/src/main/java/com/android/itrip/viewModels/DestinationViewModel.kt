package com.android.itrip.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabaseDao
import com.android.itrip.models.*
import com.android.itrip.services.ApiError
import com.android.itrip.services.TravelService
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger


class DestinationViewModel(
    val database: DestinationDatabaseDao,
    application: Application,
    viaje: Viaje?
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val destinations: LiveData<List<Destination>>
    var ciudadAVisitar: CiudadAVisitar = CiudadAVisitar(
        id = 0,
        inicio = viaje?.inicio ?: Calendar.getInstance(),
        fin = viaje?.fin ?: Calendar.getInstance(),
        detalle_ciudad = null,
        actividades_a_realizar = listOf()
    )

    init {
        TravelService.getDestinations({ continentes ->
            getDestinationsCallback(continentes)
        }, { error ->
            logger.severe("Failed to retrieve destinations - status: ${error.statusCode} - message: ${error.message}")
            val message = "Hubo un problema, intente de nuevo"
            Toast
                .makeText(getApplication(), message, Toast.LENGTH_SHORT)
                .show()

        })
        destinations = database.getAll()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun chooseStartDate(calendar: Calendar) {
        ciudadAVisitar.inicio = calendar
    }

    fun chooseEndDate(calendar: Calendar) {
        ciudadAVisitar.fin = calendar
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

    fun addDestination(
        viaje: Viaje,
        destination: Destination,
        callback: (CiudadAVisitar) -> Unit,
        callbackError: () -> Unit
    ) {
        ciudadAVisitar.detalle_ciudad = Ciudad(
            id = destination.destinationId,
            nombre = destination.name
        )
        TravelService.postDestination(viaje, ciudadAVisitar, {
            callback(it)
        }, { error ->
            val message = if (error.statusCode == 400) {
                error.data.getJSONArray("non_field_errors")[0] as String
            } else {
                logger.severe("Failed to post new destination - status: ${error.statusCode} - message: ${error.message}")
                try {
                    error.data.getJSONArray("non_field_errors")[0] as String
                } catch (e: Exception) {
                    "Hubo un problema, intente de nuevo"
                }
            }
            Toast
                .makeText(getApplication(), message, Toast.LENGTH_LONG)
                .show()
            callbackError()
        })
    }

    fun getActivities(
        destination: Destination,
        successCallback: (List<Actividad>) -> Unit,
        failureCallback: (ApiError) -> Unit
    ) {
        TravelService.getActivities(destination, successCallback, failureCallback)
    }

}