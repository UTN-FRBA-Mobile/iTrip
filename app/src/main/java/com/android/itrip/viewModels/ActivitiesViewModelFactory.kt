package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.itrip.database.ActivityDatabaseDao
import com.android.itrip.models.Actividad

class ActivitiesViewModelFactory(

    private val actividades: List<Actividad>,
    private val dataSource: ActivityDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(actividades, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}