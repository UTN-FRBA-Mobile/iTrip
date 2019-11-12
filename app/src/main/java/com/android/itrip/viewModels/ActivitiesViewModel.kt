package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.database.ActividadCategoriaDatabase
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadCategoria
import com.android.itrip.models.Categoria
import kotlinx.coroutines.*
import java.util.logging.Logger


class ActivitiesViewModel(
    _actividades: List<Actividad>?,
    private val actividadCategoriaDatabase: ActividadCategoriaDatabase,
    application: Application,
    __actividad: Actividad?
) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(this::class.java.name)

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val actividades: LiveData<List<Actividad>>
    val categorias: LiveData<List<Categoria>>
    private var _actividad = MutableLiveData<Actividad>()
    val actividad: LiveData<Actividad>
        get() = _actividad
    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query

    init {
        _actividades?.let { getActividadesCallback(_actividades) }
        _actividad.value = __actividad
        _query.value = ""
        categorias = Transformations.switchMap(actividad) { getCategorias(it) }
        actividades = Transformations.switchMap(query) { updateLiveData(it) }
    }

    private fun getActividadesCallback(actividades: List<Actividad>) {
        uiScope.launch {
            actividades.forEach {
                insert(it)
            }
        }
    }

    private suspend fun insert(actividad: Actividad) {
        withContext(Dispatchers.IO) {
            actividadCategoriaDatabase.apply {
                activityDatabaseDao.insert(actividad)
                actividad.categorias.forEach {
                    categoryDatabaseDao.insert(it)
                    actividadCategoriaDatabaseDao.insert(
                        ActividadCategoria(
                            actividadId = actividad.id,
                            categoriaId = it.id
                        )
                    )
                }
            }
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            actividadCategoriaDatabase.apply {
                actividadCategoriaDatabaseDao.clear()
                activityDatabaseDao.clear()
                categoryDatabaseDao.clear()
            }
        }
    }

    fun updateResults(query: String) {
        _query.value = query
    }

    private fun updateLiveData(query: String?): LiveData<List<Actividad>>? {
        return actividadCategoriaDatabase.activityDatabaseDao.getActividadByNombre("$query%")
    }

    private fun getCategorias(actividad: Actividad?): LiveData<List<Categoria>> {
        return actividadCategoriaDatabase.actividadCategoriaDatabaseDao
            .getCategoriasOfActividad(actividad?.id ?: 90L)
    }

    fun listOfCategories(): String? {
        return categorias.value
            ?.map { it.nombre }
            ?.joinToString(", ")
    }


}