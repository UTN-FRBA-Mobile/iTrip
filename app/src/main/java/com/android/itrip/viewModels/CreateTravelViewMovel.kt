package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import com.android.itrip.util.Toaster
import com.android.itrip.util.ViajeData
import java.util.logging.Logger
import javax.inject.Inject

class CreateTravelViewMovel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var travelService: TravelService
    @Inject
    lateinit var toaster: Toaster
    private val logger = Logger.getLogger(this::class.java.name)

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectCreateTravelViewMovel(this)
    }

    fun createTrip(
        request: ViajeData,
        createTripSucces: (Viaje) -> Unit
    ) {
        travelService.createTrip(request, {
            createTripSucces(it)
        }, { error ->
            val message = if (error.statusCode == 400) {
                error.data.getJSONArray("non_field_errors")[0] as String
            } else {
                logger.severe("Failed to post new travel - status: ${error.statusCode} - message: ${error.message}")
                "Hubo un problema, intente de nuevo"
            }
            toaster.shortToastMessage(message)
        })
    }
}