package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabaseDao
import com.android.itrip.fragments.DestinationListFragment
import kotlinx.coroutines.*
import java.util.logging.Logger


class DestinationViewModel(
    val database: DestinationDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(DestinationListFragment::class.java.name)


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val destinations: LiveData<List<Destination>>

    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query


    init {
        _query.value = ""
        uiScope.launch {
            clear()
            insert(Destination(1, "Buenos Aires", -34.61315, -58.37723))
            insert(Destination(2, "Mendoza", -32.89084, -68.82717))
            insert(Destination(3, "Tucuman", -26.82414, -65.2226))
            insert(Destination(4, "Trelew", -43.24895, -65.30505))
        }
        destinations = Transformations.switchMap(query) { query -> updateLiveData(query) }
    }

    private suspend fun insert(destination: Destination) {
        withContext(Dispatchers.IO) {
            database.insert(destination)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun updateResults(query: String) {
        logger.info("Query: " + query)
        this._query.value = query
        logger.info("Query livedata: " + this.query.value!!)
        try {
            if (destinations != null)
                logger.info("destinations: " + destinations.value)
        } catch (e: Exception) {
            logger.info(e.toString())
        }
    }

    private fun updateLiveData(query: String?): LiveData<List<Destination>>? {
        return database.getDestinationsByName("$query%")
    }
}