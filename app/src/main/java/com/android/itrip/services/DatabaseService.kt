package com.android.itrip.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import com.android.itrip.database.*
import com.android.itrip.models.*
import kotlinx.coroutines.*

class DatabaseService(context: Context) : Service() {
    private var serviceJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val actividadCategoriaDatabase: ActividadCategoriaDatabase =
        ActividadCategoriaDatabase.getInstance(context)
    private val actividadCategoriaDatabaseDao: ActividadCategoriaDatabaseDao
        get() = actividadCategoriaDatabase.actividadCategoriaDatabaseDao
    private val activityDatabaseDao: ActivityDatabaseDao
        get() = actividadCategoriaDatabase.activityDatabaseDao
    private val categoryDatabaseDao: CategoryDatabaseDao
        get() = actividadCategoriaDatabase.categoryDatabaseDao
    private val ciudadDatabaseDao: CiudadDatabaseDao
        get() = actividadCategoriaDatabase.ciudadDatabaseDao


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun insertActividad(actividad: Actividad, ciudad: Ciudad?) {
        withContext(Dispatchers.IO) {
            actividad.ciudad = ciudad!!.id
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

    suspend fun clearActividadCategoriaDatabase() {
        withContext(Dispatchers.IO) {
            actividadCategoriaDatabase.clearAllTables()
        }
    }

    fun insertActividades(actividades: List<Actividad>, ciudad: Ciudad?) {
        uiScope.launch {
            actividades.forEach {
                insertActividad(it, ciudad)
            }
        }
    }

    fun getActividadByNombre(query: String?, ciudad: Ciudad): LiveData<List<Actividad>> {
        return activityDatabaseDao.getActividadByNombre("%$query%", ciudad.id)

    }

    fun getCategoriasOfActivity(actividad: Actividad?): LiveData<List<Categoria>>? {
        return if (actividad?.id != null)
            actividadCategoriaDatabaseDao.getCategoriasOfActividad(actividad.id)
        else null
    }

    fun insertActividadesDeCiudadAVisitar(ciudadesAVisitar: List<CiudadAVisitar>) {
        ciudadesAVisitar.forEach { ciudadAVisitar ->
            ciudadAVisitar.actividades_a_realizar.forEach { actividadARealizar ->
                uiScope.launch {
                    insertActividad(
                        actividadARealizar.detalle_actividad,
                        ciudadAVisitar.detalle_ciudad
                    )
                }
            }
        }
    }

    fun getCiudades(): LiveData<List<Ciudad>> {
        return ciudadDatabaseDao.getAll()
    }

    fun insert(ciudad: Ciudad) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                ciudadDatabaseDao.insert(ciudad)
            }
        }
    }

    fun getActivitiesOfCity(ciudad: Ciudad): List<Actividad> {
        return activityDatabaseDao.getActivitiesOfCity(ciudad.id)
    }

}