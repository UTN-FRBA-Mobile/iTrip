package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.databinding.BucketItemBinding
import com.android.itrip.databinding.FragmentScheduleBinding
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.viewModels.ScheduleViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*
import java.util.logging.Logger

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var ciudadAVisitar: CiudadAVisitar
    private lateinit var container: ViewGroup
    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container!!
        try {
            ciudadAVisitar = this.arguments!!.get("ciudadAVisitar") as CiudadAVisitar
            logger.info("ciuda.nombre: " + ciudadAVisitar.detalle_ciudad.nombre)
        } catch (e: Exception) {
            logger.info(e.toString())
        }

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule, container, false
        )

        val application = requireNotNull(this.activity).application

        scheduleViewModel = ScheduleViewModel(application, ciudadAVisitar)
        binding.scheduleViewModel = scheduleViewModel

        scheduleViewModel.setCiudadAVisitar { setCalendar() }

        return binding.root
    }

    private fun setCalendar() {
        val horizontalCalendar =
            HorizontalCalendar.Builder(binding.root.rootView, R.id.calendarView)
                .range(
                    scheduleViewModel.ciudad_a_visitar.inicio,
                    scheduleViewModel.ciudad_a_visitar.fin
                )
                .datesNumberOnScreen(5)
                .build()
//        setActivities(ciudadAVisitar.actividades_a_realizar.filter { it.dia == scheduleViewModel.ciudad_a_visitar.inicio })

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                ciudadAVisitar.actividades_a_realizar.filter { it.dia == date }
                    .apply {
                        logger.info("Actividades en el dia: " + size.toString())
                        setActivities(this)
                    }.forEach {
                        logger.info(it.detalle_actividad.nombre)
                    }
                //do something
            }
        }
    }

    private fun setActivities(actividades_a_realizar: List<ActividadARealizar>) {
        val buckets: HashMap<Int, ActividadARealizar> = hashMapOf()

        actividades_a_realizar.forEach {
            buckets[it.bucket_inicio] = it
            repeat(it.detalle_actividad.duracion) { counter: Int ->
                buckets[it.bucket_inicio + counter] = it
            }
        }
        repeat(binding.bucketsConstraintlayout.childCount) {
//            ((binding.bucketsConstraintlayout[it]) as BucketItemBinding).activity =
//                buckets[it + 1]?.detalle_actividad
        }
    }
}