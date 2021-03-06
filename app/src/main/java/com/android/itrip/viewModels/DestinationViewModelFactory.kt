package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.itrip.models.Viaje

class DestinationViewModelFactory(
    private val application: Application,
    private val viaje: Viaje?
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DestinationViewModel::class.java)) {
            return DestinationViewModel(
                application, viaje
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}