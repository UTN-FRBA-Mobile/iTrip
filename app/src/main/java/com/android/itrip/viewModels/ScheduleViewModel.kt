package com.android.itrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.models.CiudadAVisitar
import java.util.*
import java.util.logging.Logger

class ScheduleViewModel(
    val ciudadAVisitar: CiudadAVisitar
) : ViewModel() {
    private val logger = Logger.getLogger(this::class.java.name)
    var actividadesARealizar: LiveData<List<ActividadARealizar>>

    private var _date = MutableLiveData<Calendar>()
    private val date: LiveData<Calendar>
        get() = _date

    init {
        _date.value = ciudadAVisitar.inicio
        actividadesARealizar = Transformations.switchMap(date) { date -> setBuckets(date) }
    }

    private fun setBuckets(date: Calendar): LiveData<List<ActividadARealizar>> {
        ciudadAVisitar.actividades_a_realizar.filter { it.dia == date }
            .apply {
                logger.info("Actividades en el dia: " + size.toString())
                return MutableLiveData<List<ActividadARealizar>>(createBuckets(this))
            }
    }

    private fun createBuckets(list: List<ActividadARealizar>): List<ActividadARealizar> {
        val bucketsTemp: HashMap<Int, ActividadARealizar> = hashMapOf()
        bucketsTemp.apply {
            list.forEach {
                this[it.bucket_inicio] = it
                repeat(it.detalle_actividad!!.duracion) { counter: Int ->
                    this[it.bucket_inicio + counter] = it
                }
            }
            for (i in 1..6) {
                if (this[i] == null) {
                    this[i] = ActividadARealizar(0, Calendar.getInstance(), i, null)
                }
            }
        }
        return bucketsTemp.map { it.value }
    }

    fun updateDate(date: Calendar) {
        _date.value = date
    }

}