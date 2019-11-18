package com.android.itrip.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.itrip.models.Viaje
import com.android.itrip.services.ApiError
import com.android.itrip.services.TravelService

class HomeViewModel(
    context: Context, successCallback: (List<Viaje>) -> Unit, failureCallback: (ApiError) -> Unit
) :
    ViewModel() {

    private var _viajes = MutableLiveData<List<Viaje>>()
    private val travelService = TravelService(context)
    val viajes: LiveData<List<Viaje>>
        get() = _viajes

    init {
        getTravels(successCallback, failureCallback)
    }

    private fun getTravels(
        successCallback: (List<Viaje>) -> Unit,
        failureCallback: (ApiError) -> Unit
    ) {
        travelService.getTravels({
            _viajes.value = it
            successCallback(it)
        }, { failureCallback(it) })
    }

    fun deleteTravel(
        travel: Viaje,
        deleteTravelSuccess: () -> Unit,
        deleteTravelFailure: (ApiError) -> Unit
    ) {
        travelService.deleteTrip(travel, {
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