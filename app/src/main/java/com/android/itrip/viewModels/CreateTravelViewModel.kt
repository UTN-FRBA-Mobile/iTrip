package com.android.itrip.viewModels

import androidx.lifecycle.*
import android.app.Application
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabaseDao
import com.android.itrip.fragments.DestinationListFragment
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger


class CreateTravelViewModel() : ViewModel() {
    val from : Date = Date()
    val to : Date = Date()
}




