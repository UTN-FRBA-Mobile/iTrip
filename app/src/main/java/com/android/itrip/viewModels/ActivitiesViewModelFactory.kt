package com.android.itrip.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.itrip.models.Actividad
import com.android.itrip.models.Ciudad
import com.android.itrip.services.DatabaseService

class ActivitiesViewModelFactory(

    private val databaseService: DatabaseService,
    private val ciudad: Ciudad?,
    private val actividades: List<Actividad>?,
    private val actividad: Actividad?
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(
                databaseService,
                ciudad,
                actividades,
                actividad
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}