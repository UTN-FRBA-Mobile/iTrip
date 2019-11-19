package com.android.itrip.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import com.android.itrip.database.*
import com.android.itrip.models.*
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class DatabaseService @Inject constructor(@Named("ApplicationContext") context: Context) :
    Service() {
    private var serviceJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val iTripDatabase: ITripDatabase = ITripDatabase.getInstance(context)
    private val actividadCategoriaDao: ActividadCategoriaDao
        get() = iTripDatabase.actividadCategoriaDao
    private val actividadDao: ActividadDao
        get() = iTripDatabase.actividadDao
    private val categoriaDao: CategoriaDao
        get() = iTripDatabase.categoriaDao
    private val ciudadDao: CiudadDao
        get() = iTripDatabase.ciudadDao


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun insertActividad(actividad: Actividad, ciudad: Ciudad?) {
        withContext(Dispatchers.IO) {
            actividad.ciudad = ciudad!!.id
            actividadDao.insert(actividad)
            actividad.categorias.forEach {
                categoriaDao.insert(it)
                actividadCategoriaDao.insert(
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
            iTripDatabase.clearAllTables()
        }
    }

    fun insertActividades(actividades: List<Actividad>, ciudad: Ciudad?) {
        uiScope.launch {
            actividades.forEach {
                insertActividad(it, ciudad)
            }
        }
    }

    fun getCategoriasOfActivity(actividad: Actividad?): LiveData<List<Categoria>>? {
        return if (actividad?.id != null)
            actividadCategoriaDao.getCategoriasOfActividad(actividad.id)
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
        return ciudadDao.getAll()
    }

    fun insert(ciudad: Ciudad) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                ciudadDao.insert(ciudad)
            }
        }
    }

    fun getActivitiesOfCity(ciudad: Ciudad): List<Actividad> {
        return actividadDao.getActivitiesOfCity(ciudad.id)
    }

    fun getActivitiesOfCity2(ciudad: Ciudad): LiveData<List<Actividad>> {
        return actividadDao.getActivitiesOfCity2(ciudad.id)
    }

}