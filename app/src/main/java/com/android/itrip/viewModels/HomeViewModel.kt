package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.itrip.models.Viaje
import com.android.itrip.services.ApiError
import com.android.itrip.services.TravelService

class HomeViewModel(successCallback: (List<Viaje>) -> Unit, failureCallback: (ApiError) -> Unit) :
    ViewModel() {

    private var _viajes = MutableLiveData<List<Viaje>>()
    val viajes: LiveData<List<Viaje>>
        get() = _viajes

    init {
        getTravels(successCallback, failureCallback)
    }

    private fun getTravels(
        successCallback: (List<Viaje>) -> Unit,
        failureCallback: (ApiError) -> Unit
    ) {
        TravelService.getTravels({
            _viajes.value = it
            successCallback(it)
        }, { failureCallback(it) })
    }

    fun deleteTravel(
        travel: Viaje,
        deleteTravelSuccess: () -> Unit,
        deleteTravelFailure: (ApiError) -> Unit
    ) {
        TravelService.deleteTrip(travel, {
            _viajes.value = _viajes.value?.filter { it != travel }
            deleteTravelSuccess()
        }, { error ->
            deleteTravelFailure(error)
        })
    }

    fun hasTravels(): Boolean {
        return viajes.value?.isNotEmpty() ?: false
    }
}