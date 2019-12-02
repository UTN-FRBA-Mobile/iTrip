package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.Actividad
import com.android.itrip.models.Categoria
import com.android.itrip.models.Ciudad
import com.android.itrip.services.DatabaseService
import javax.inject.Inject


class ActivitiesViewModel(
    application: Application,
    private val ciudad: Ciudad?,
    private val _actividades: List<Actividad>?,
    __actividad: Actividad?
) : AndroidViewModel(application) {

    val actividades: LiveData<List<Actividad>>
    val categorias: LiveData<List<Categoria>>
    private var _actividad = MutableLiveData<Actividad>()
    val actividad: LiveData<Actividad>
        get() = _actividad
    private var _query = MutableLiveData<String>()
    private val query: LiveData<String>
        get() = _query
    @Inject
    lateinit var databaseService: DatabaseService

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectActivitiesViewModel(this)
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
        }?.sortedBy { it.nombre }
        return MutableLiveData(filteredActivities)
    }

    fun listOfCategories(): String? {
        return categorias.value
            ?.map { it.nombre }
            ?.joinToString(", ")
    }

    fun getRandomActivity(): Actividad {
        return actividades.value!!.random()
    }

}