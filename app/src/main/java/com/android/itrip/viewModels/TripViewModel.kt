package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class TripViewModel(viajeID: Long, callback: () -> Unit) : ViewModel() {

    private val logger = Logger.getLogger("prueba")
    private var _viaje = MutableLiveData<Viaje>()
    val viaje: LiveData<Viaje>
        get() = _viaje

    var ciudadesAVisitar: LiveData<List<CiudadAVisitar>>

    init {
        getTravel(viajeID) { callback() }
        ciudadesAVisitar =
            Transformations.switchMap(viaje) { viaje -> ciudadesAVisitarTransformation(viaje) }
    }

    fun deleteCityToVisit(ciudadAVisitar: CiudadAVisitar, callback: () -> Unit) {
        TravelService.deleteDestination(ciudadAVisitar, { getTravel(null, callback) }, {})
    }

    private fun getTravel(viajeID: Long? = _viaje.value!!.id, callback: () -> Unit) {
        logger.info("getTravel ")
        TravelService.getTrip(viajeID!!, {
            _viaje.value = it
            logger.info("TravelService.getTrip")
            callback()
        }, {})
    }

    private fun ciudadesAVisitarTransformation(viaje: Viaje): LiveData<List<CiudadAVisitar>> {
        logger.info("ciudadesAVisitarTransformation")
        return MutableLiveData<List<CiudadAVisitar>>(viaje.ciudades_a_visitar)
    }

}