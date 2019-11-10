package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.itrip.database.ActividadCategoriaDatabase
import com.android.itrip.models.Actividad

class ActivitiesViewModelFactory(

    private val actividades: List<Actividad>?,
    private val application: Application,
    private val actividad: Actividad?
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(
                actividades,
                ActividadCategoriaDatabase.getInstance(application),
                application,
                actividad
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}