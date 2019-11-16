package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.Actividad
import com.android.itrip.models.Categoria
import com.android.itrip.models.Ciudad
import com.android.itrip.services.DatabaseService


class ActivitiesViewModel(
    private val databaseService: DatabaseService,
    private val ciudad: Ciudad?,
    private val _actividades: List<Actividad>?,
    __actividad: Actividad?
) : ViewModel() {
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
        categorias =
            Transformations.switchMap(actividad) { databaseService.getCategoriasOfActivity(it) }
        _query.value = ""
        actividades = Transformations.switchMap(query) { updateLiveData(it) }
    }

    private fun getActividadesCallback(actividades: List<Actividad>) {
        databaseService.insertActividades(actividades, ciudad)
    }

    fun updateResults(query: String) {
        _query.value = query
    }

    private fun updateLiveData(query: String): LiveData<List<Actividad>>? {
        val filteredActivities = _actividades?.filter {
            it.nombre.contains(query, true)
        }
        return MutableLiveData(filteredActivities)
//        return databaseService.getActividadByNombre(query, ciudad!!)
    }

    fun listOfCategories(): String? {
        return categorias.value
            ?.map { it.nombre }
            ?.joinToString(", ")
    }

}