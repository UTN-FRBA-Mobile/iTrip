package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.models.Bucket
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.services.TravelService
import java.util.*

object CiudadAVisitarDate {
    var _date = MutableLiveData<Calendar>()
    val date: LiveData<Calendar>
        get() = _date
}

class ScheduleViewModel(
    var ciudadAVisitar: CiudadAVisitar
) : ViewModel() {
    var actividadesARealizar: LiveData<List<ActividadARealizar>>
    private lateinit var bucket: Bucket

    init {
        CiudadAVisitarDate._date.value = CiudadAVisitarDate.date.value ?: ciudadAVisitar.inicio
        actividadesARealizar =
            Transformations.switchMap(CiudadAVisitarDate.date) { date -> setBuckets(date) }
    }

    private fun setBuckets(date: Calendar): LiveData<List<ActividadARealizar>> {
        val tempList =
            cleanDuplication(date, ciudadAVisitar.actividades_a_realizar.filter { it.dia == date })
        return MutableLiveData<List<ActividadARealizar>>(tempList)
    }

/* DEPRECATED, OLD VERSION OF cleanDuplication
    private fun createBuckets(list: List<ActividadARealizar>): List<ActividadARealizar> {
        val bucketsTemp: HashMap<Int, ActividadARealizar> = hashMapOf()
        bucketsTemp.apply {
            for (i in 1..6) {
                this[i] = ActividadARealizar(
                    0L,
                    Calendar.getInstance(),
                    i,
                    null
                )
            }
            list.forEach {
                this[it.bucket_inicio] = it
                repeat(it.detalle_actividad!!.duracion) { counter: Int ->
                    this[it.bucket_inicio + counter] = it
                }
            }
        }
        return bucketsTemp.map { it.value }
    }
*/

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
                        j += this.detalle_actividad!!.duracion
                        mutableList.remove(this)
                    }
                } else {
                    duration =
                        mutableList.firstOrNull()?.bucket_inicio?.minus(i) ?: 7 - i
                    bucketsTemp[i] = ActividadARealizar(
                        0L,
                        date,
                        i,
                        Actividad(0L, "", "", duration)
                    )
                    j += bucketsTemp[i]!!.detalle_actividad!!.duracion
                }
            }
        }
        return bucketsTemp.map { it.value }
    }


    fun updateDate(date: Calendar) {
        CiudadAVisitarDate._date.value = date
    }

    private fun updateCiudadAVisitar() {
        TravelService.get_CityToVisit(ciudadAVisitar, { ciudadAVisitar: CiudadAVisitar ->
            this@ScheduleViewModel.ciudadAVisitar = ciudadAVisitar
            updateDate(CiudadAVisitarDate.date.value!!)
        }, {})
    }

    fun deleteToDoActivity(item: ActividadARealizar) {
        TravelService.deleteToDoActivity(item, {
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
        TravelService.getActivitiesForBucket(bucket,
            { actividadesARealizar -> successCallback(actividadesARealizar) },
            { failureCallback() })
    }

    fun addActividadToBucket(actividad: Actividad) {
        bucket.actividad = actividad
        TravelService.addActivityToBucket(bucket, {
            updateCiudadAVisitar()
        }, {})
    }

}