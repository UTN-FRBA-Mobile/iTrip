package com.android.itrip.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.fragments.ViajeData
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class CreateTravelViewMovel(application: Application) : AndroidViewModel(application){

    private val travelService =  TravelService(application.applicationContext)
    private val logger = Logger.getLogger(this::class.java.name)


    fun createTrip(
        request: ViajeData,
        createTripSucces: (Viaje)->Unit
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
            Toast
                .makeText((getApplication() as Application).applicationContext, message, Toast.LENGTH_SHORT)
                .show()
        })
    }
}