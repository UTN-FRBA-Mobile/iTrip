package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.itrip.database.ActivityDatabaseDao
import com.android.itrip.database.Destination

class ActivitiesViewModelFactory(

    private val destination: Destination,
    private val dataSource: ActivityDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(destination, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}