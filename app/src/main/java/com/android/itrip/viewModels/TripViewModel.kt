package com.android.itrip.viewModels

import androidx.lifecycle.ViewModel
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.ApiError
import com.android.itrip.services.DatabaseService
import com.android.itrip.services.TravelService

class TripViewModel(private val databaseService: DatabaseService, viajeID: Long, callback: (List<CiudadAVisitar>) -> Unit) : ViewModel() {

    private lateinit var _viaje: Viaje
    val viaje: Viaje get() = _viaje

    init {
        getTravel(viajeID, callback)
    }

    fun deleteCityToVisit(
        ciudadAVisitar: CiudadAVisitar,
        deleteCityToTravelSuccess: () -> Unit,
        deleteCityToTravelFailure: (ApiError) -> Unit
    ) {
        TravelService.deleteDestination(
            ciudadAVisitar,
            { deleteCityToTravelSuccess() },
            { error -> deleteCityToTravelFailure(error) })
    }

    private fun getTravel(viajeID: Long?, callback: (List<CiudadAVisitar>) -> Unit) {
        TravelService.getTrip(viajeID ?: viaje.id, {
            _viaje = it
            try {
                databaseService.insertActividadesDeCiudadAVisitar(it.ciudades_a_visitar)
                callback(it.ciudades_a_visitar)
            } catch (e: KotlinNullPointerException) {
            }
        }, {})
    }

}