package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService

class TripViewModel(viajeID: Long, callback: () -> Unit) : ViewModel() {

    private var _viaje = MutableLiveData<Viaje>()
    val viaje: LiveData<Viaje> get() = _viaje
    var ciudadesAVisitar: LiveData<List<CiudadAVisitar>>

    init {
        getTravel(viajeID) { callback() }
        ciudadesAVisitar =
            Transformations.switchMap(viaje) { viaje -> ciudadesAVisitarTransformation(viaje) }
    }

    fun deleteCityToVisit(ciudadAVisitar: CiudadAVisitar, callback: () -> Unit) {
        TravelService.deleteDestination(ciudadAVisitar, { getTravel(null, callback) }, {})
    }

    fun getTravel(viajeID: Long?, callback: () -> Unit) {
        TravelService.getTrip(viajeID ?: viaje.value!!.id, {
            _viaje.value = it
            callback()
        }, {})
    }

    private fun ciudadesAVisitarTransformation(viaje: Viaje): LiveData<List<CiudadAVisitar>> {
        return MutableLiveData<List<CiudadAVisitar>>(viaje.ciudades_a_visitar)
    }

}