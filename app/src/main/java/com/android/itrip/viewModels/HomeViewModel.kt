package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import com.android.itrip.util.ApiError
import javax.inject.Inject

class HomeViewModel(
    application: Application,
    successCallback: (List<Viaje>) -> Unit,
    failureCallback: (ApiError) -> Unit
) :
    AndroidViewModel(application) {

    private var _viajes = MutableLiveData<List<Viaje>>()
    @Inject
    lateinit var travelService: TravelService
    val viajes: LiveData<List<Viaje>>
        get() = _viajes

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectHomeViewModel(this)
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