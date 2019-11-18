package com.android.itrip.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.Actividad
import com.android.itrip.models.Ciudad
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.ConnectionService
import com.android.itrip.services.StorageService
import com.android.itrip.services.TravelService
import com.android.itrip.util.ApiError
import com.android.itrip.util.Toaster
import java.util.*
import java.util.logging.Logger
import javax.inject.Inject


class DestinationViewModel(
    application: Application,
    viaje: Viaje?
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)
    @Inject
    lateinit var travelService: TravelService
    @Inject
    lateinit var storageService: StorageService
    @Inject
    lateinit var toaster: Toaster
    val ciudades: LiveData<List<Ciudad>>
    var ciudadAVisitar: CiudadAVisitar = CiudadAVisitar(
        id = 0,
        inicio = viaje?.inicio ?: Calendar.getInstance(),
        fin = viaje?.fin ?: Calendar.getInstance(),
        detalle_ciudad = null,
        actividades_a_realizar = listOf()
    )

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectDestinationViewModel(this)
        ciudades = storageService.getCiudades()
    }

    fun chooseStartDate(calendar: Calendar) {
        ciudadAVisitar.inicio = calendar
    }

    fun chooseEndDate(calendar: Calendar) {
        ciudadAVisitar.fin = calendar
    }

    fun addDestination(
        viaje: Viaje,
        ciudad: Ciudad,
        callback: (CiudadAVisitar) -> Unit,
        callbackError: () -> Unit
    ) {
        ciudadAVisitar.detalle_ciudad = ciudad
        travelService.postDestination(viaje, ciudadAVisitar, {
            storageService.insertActividades(
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
            toaster.shortToastMessage(message)
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
            travelService.getActivities(ciudad, {
                it.forEach { actividad ->
                    actividad.ciudad = ciudad.id
                }
                storageService.insertActividades(it, ciudad)
                successCallback(it)
            }, failureCallback)
        } else {
            successCallback(storageService.getActivitiesOfCity(ciudad))
        }
    }

}