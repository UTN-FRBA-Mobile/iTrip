package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.models.Bucket
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.services.DatabaseService
import com.android.itrip.services.TravelService
import com.android.itrip.util.CiudadAVisitarObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.set

class ScheduleViewModel(
    application: Application,
    var ciudadAVisitar: CiudadAVisitar
) : AndroidViewModel(application) {
    var actividadesARealizar: LiveData<List<ActividadARealizar>>
    private lateinit var bucket: Bucket
    @Inject
    lateinit var travelService: TravelService
    @Inject
    lateinit var databaseService: DatabaseService

    init {
        DaggerApiComponent.builder().contextModule(ContextModule(getApplication())).build()
            .injectScheduleViewModel(this)
        CiudadAVisitarObject._date.value =
            if (ciudadAVisitar == CiudadAVisitarObject.ciudadAVisitar)
                CiudadAVisitarObject.date.value ?: ciudadAVisitar.inicio
            else ciudadAVisitar.inicio
        actividadesARealizar =
            Transformations.switchMap(CiudadAVisitarObject.date) { date -> setBuckets(date) }
    }

    private fun setBuckets(date: Calendar): LiveData<List<ActividadARealizar>> {
        val tempList =
            cleanDuplication(date, ciudadAVisitar.actividades_a_realizar.filter { it.dia == date })
        return MutableLiveData<List<ActividadARealizar>>(tempList)
    }

    private fun cleanDuplication(
        date: Calendar,
        list: List<ActividadARealizar>
    ): List<ActividadARealizar> {
        val bucketsTemp: HashMap<Int, ActividadARealizar> = hashMapOf()
        val mutableList: MutableList<ActividadARealizar> = list.toMutableList()
        var tempItem: ActividadARealizar?
        var duration: Int
        var j = 1
        for (i in 1..6) {
            if (i >= j) {
                tempItem = mutableList.firstOrNull { it.bucket_inicio == i }
                if (tempItem != null) {
                    tempItem.apply {
                        bucketsTemp[i] = this
                        j += this.detalle_actividad.duracion
                        mutableList.remove(this)
                    }
                } else {
                    duration = 1
//                    duration =
//                        mutableList.firstOrNull()?.bucket_inicio?.minus(i) ?: 7 - i
                    bucketsTemp[i] = ActividadARealizar(
                        0L,
                        date,
                        i,
                        Actividad(0L, "", "", duration)
                    )
                    j += duration
                }
            }
        }
        return bucketsTemp.map { it.value }
    }


    fun updateDate(date: Calendar) {
        CiudadAVisitarObject._date.value = date
    }

    private fun updateCiudadAVisitar() {
        travelService.get_CityToVisit(ciudadAVisitar, { ciudadAVisitar: CiudadAVisitar ->
            databaseService.insertActividades(
                ciudadAVisitar.actividades_a_realizar.map { it.detalle_actividad },
                ciudadAVisitar.detalle_ciudad
            )
            this@ScheduleViewModel.ciudadAVisitar = ciudadAVisitar
            updateDate(CiudadAVisitarObject.date.value!!)
        }, {})
    }

    fun deleteToDoActivity(item: ActividadARealizar) {
        travelService.deleteToDoActivity(item, {
            updateCiudadAVisitar()
        }, {})
    }

    fun getPossibleActivitiesForBucket(
        actividadARealizar: ActividadARealizar,
        successCallback: (List<Actividad>) -> Unit,
        failureCallback: () -> Unit
    ) {
        bucket = Bucket(
            ciudadAVisitar,
            actividadARealizar.dia,
            actividadARealizar.bucket_inicio
        )
        travelService.getActivitiesForBucket(bucket,
            {
                databaseService.insertActividades(it, ciudadAVisitar.detalle_ciudad)
                successCallback(it)
            },
            { failureCallback() })
    }

    fun addActividadToBucket(actividad: Actividad) {
        bucket.actividad = actividad
        travelService.addActivityToBucket(bucket, {
            updateCiudadAVisitar()
        }, {})
    }

}