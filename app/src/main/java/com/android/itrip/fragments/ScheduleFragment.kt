package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.adapters.BucketAdapter
import com.android.itrip.databinding.FragmentScheduleBinding
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.viewModels.ScheduleViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*
import java.util.logging.Logger


class ScheduleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: BucketAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var ciudadAVisitar: CiudadAVisitar
    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            ciudadAVisitar = this.arguments!!.get("ciudadAVisitar") as CiudadAVisitar
            logger.info("ciuda.nombre: " + ciudadAVisitar.detalle_ciudad!!.nombre)
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule, container, false
        )
        val application = requireNotNull(this.activity).application
        scheduleViewModel = ScheduleViewModel(application, ciudadAVisitar)
        binding.scheduleViewModel = scheduleViewModel
        setCalendar()
        recyclerView = binding.myRecyclerView
        viewManager = LinearLayoutManager(application)
        recyclerView.layoutManager = viewManager
        viewAdapter = BucketAdapter(scheduleViewModel)
        recyclerView.adapter = viewAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun setCalendar() {
        val horizontalCalendar =
            HorizontalCalendar.Builder(binding.root.rootView, R.id.calendarView)
                .range(
                    ciudadAVisitar.inicio,
                    ciudadAVisitar.fin
                )
                .datesNumberOnScreen(5)
                .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                scheduleViewModel.updateDate(date)
            }
        }
    }

}