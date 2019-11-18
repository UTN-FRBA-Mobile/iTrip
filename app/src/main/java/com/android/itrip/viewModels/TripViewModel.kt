package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.DatabaseService
import com.android.itrip.services.TravelService
import com.android.itrip.util.ApiError
import javax.inject.Inject

class TripViewModel(
    application: Application,
    viajeID: Long,
    callback: (List<CiudadAVisitar>) -> Unit
) : AndroidViewModel(application) {

    private lateinit var _viaje: Viaje
    val viaje: Viaje get() = _viaje
    @Inject
    lateinit var travelService: TravelService
    @Inject
    lateinit var databaseService: DatabaseService

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectTripViewModel(this)
        getTravel(viajeID, callback)
    }

    fun deleteCityToVisit(
        ciudadAVisitar: CiudadAVisitar,
        deleteCityToTravelSuccess: () -> Unit,
        deleteCityToTravelFailure: (ApiError) -> Unit
    ) {
        travelService.deleteDestination(
            ciudadAVisitar,
            { deleteCityToTravelSuccess() },
            { error -> deleteCityToTravelFailure(error) })
    }

    private fun getTravel(viajeID: Long?, callback: (List<CiudadAVisitar>) -> Unit) {
        travelService.getTrip(viajeID ?: viaje.id, {
            _viaje = it
            try {
                databaseService.insertActividadesDeCiudadAVisitar(it.ciudades_a_visitar)
                callback(it.ciudades_a_visitar)
            } catch (e: KotlinNullPointerException) {
            }
        }, {})
    }

}