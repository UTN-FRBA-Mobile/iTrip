package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.ActividadARealizar
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

    init {
        CiudadAVisitarDate._date.value = CiudadAVisitarDate.date.value ?: ciudadAVisitar.inicio
        actividadesARealizar =
            Transformations.switchMap(CiudadAVisitarDate.date) { date -> setBuckets(date) }

    }

    private fun setBuckets(date: Calendar): LiveData<List<ActividadARealizar>> {
        val tempList =
            createBuckets(ciudadAVisitar.actividades_a_realizar.filter { it.dia == date })
        return MutableLiveData<List<ActividadARealizar>>(tempList)
    }

    private fun createBuckets(list: List<ActividadARealizar>): List<ActividadARealizar> {
        val bucketsTemp: HashMap<Int, ActividadARealizar> = hashMapOf()
        bucketsTemp.apply {
            for (i in 1..6) {
                this[i] = ActividadARealizar(
                    CiudadAVisitarDate.date.value!!.timeInMillis + i,
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

}