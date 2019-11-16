package com.android.itrip.viewModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.itrip.models.*
import com.android.itrip.services.ApiError
import com.android.itrip.services.ConnectionService
import com.android.itrip.services.DatabaseService
import com.android.itrip.services.TravelService
import java.util.*
import java.util.logging.Logger


class DestinationViewModel(
    private val databaseService: DatabaseService,
    application: Application,
    viaje: Viaje?
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)
    val ciudades: LiveData<List<Ciudad>>
    var ciudadAVisitar: CiudadAVisitar = CiudadAVisitar(
        id = 0,
        inicio = viaje?.inicio ?: Calendar.getInstance(),
        fin = viaje?.fin ?: Calendar.getInstance(),
        detalle_ciudad = null,
        actividades_a_realizar = listOf()
    )

    init {
        if (ConnectionService.isNetworkConnected(application.applicationContext)) {
            TravelService.getDestinations({ continentes ->
                getDestinationsCallback(continentes)
            }, { error ->
                logger.severe("Failed to retrieve destinations - status: ${error.statusCode} - message: ${error.message}")
                val message = "Hubo un problema, intente de nuevo"
                Toast
                    .makeText(getApplication(), message, Toast.LENGTH_SHORT)
                    .show()
            })
        }
        ciudades = databaseService.getCiudades()
    }


    fun chooseStartDate(calendar: Calendar) {
        ciudadAVisitar.inicio = calendar
    }

    fun chooseEndDate(calendar: Calendar) {
        ciudadAVisitar.fin = calendar
    }

    private fun getDestinationsCallback(continentes: List<Continente>) {
        continentes.forEach {
            it.paises.forEach { pais ->
                pais.ciudades.forEach { ciudad ->
                    databaseService.insert(ciudad)
                }
            }
        }
    }

    fun addDestination(
        viaje: Viaje,
        ciudad: Ciudad,
        callback: (CiudadAVisitar) -> Unit,
        callbackError: () -> Unit
    ) {
        ciudadAVisitar.detalle_ciudad = ciudad
        TravelService.postDestination(viaje, ciudadAVisitar, {
            databaseService.insertActividades(
                it.actividades_a_realizar.map { it.detalle_actividad },
                it.detalle_ciudad
            )
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
        ciudad: Ciudad,
        context: Context,
        successCallback: (List<Actividad>) -> Unit,
        failureCallback: (ApiError) -> Unit
    ) {
        if (ConnectionService.isNetworkConnected(context)) {
            TravelService.getActivities(ciudad, {
                it.forEach { actividad ->
                    actividad.ciudad = ciudad.id
                }
                databaseService.insertActividades(it, ciudad)
                successCallback(it)
            }, failureCallback)
        } else {
            successCallback(databaseService.getActivitiesOfCity(ciudad))
        }
    }


}