package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class ScheduleViewModel(
    application: Application,
    val ciudadAVisitar: CiudadAVisitar
) : AndroidViewModel(application) {
    lateinit var ciudad_a_visitar: CiudadAVisitar
    private val logger = Logger.getLogger(this::class.java.name)


    fun setCiudadAVisitar(fragmentCallback: () -> Unit) {
        TravelService.get_CityToVisit(ciudadAVisitar,
            { ciudad_a_visitar ->
                this.ciudad_a_visitar = ciudad_a_visitar
                this.ciudad_a_visitar.actividades_a_realizar.forEach {
                    logger.info(it.detalle_actividad.nombre)
                }
                fragmentCallback()
            },
            {})
    }
}
