package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.database.ActivityDatabaseDao
import com.android.itrip.models.Actividad
import kotlinx.coroutines.*
import java.util.logging.Logger


class ActivitiesViewModel(
    _actividades: List<Actividad>,
    val database: ActivityDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val actividades: LiveData<List<Actividad>>
    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query

    init {
        getActividadesCallback(_actividades)
        _query.value = ""
        actividades = Transformations.switchMap(query) { query -> updateLiveData(query) }
    }

    private fun getActividadesCallback(actividades: List<Actividad>) {
        uiScope.launch {
            clear()
            actividades.forEach {
                insert(it)
            }
        }
    }

    private suspend fun insert(actividad: Actividad) {
        withContext(Dispatchers.IO) {
            database.insert(actividad)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun updateResults(query: String) {
        _query.value = query
    }

    private fun updateLiveData(query: String?): LiveData<List<Actividad>>? {
        return database.getActividadByNombre("$query%")
    }
}
