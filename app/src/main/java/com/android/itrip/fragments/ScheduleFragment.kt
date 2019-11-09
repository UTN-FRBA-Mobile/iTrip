package com.android.itrip.fragments


import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.ActivitiesActivity
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.RequestCodes.Companion.ADD_ACTIVITY_CODE
import com.android.itrip.RequestCodes.Companion.VIEW_ACTIVITY_DETAILS_CODE
import com.android.itrip.adapters.ActivityType
import com.android.itrip.adapters.BucketAdapter
import com.android.itrip.databinding.FragmentScheduleBinding
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.viewModels.CiudadAVisitarDate
import com.android.itrip.viewModels.ScheduleViewModel
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.*
import java.util.logging.Logger


class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var ciudadAVisitar: CiudadAVisitar
    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // hide toolbar shadow because sub toolbar
        activity!!.app_bar.view_toolbar_shadow.visibility = View.GONE
        logger.severe("LA CONCHA DE TU REPUTISIMA MADRE")
        try {
            ciudadAVisitar = this.arguments!!.get("ciudadAVisitar") as CiudadAVisitar
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule, container, false
        )
        scheduleViewModel = ScheduleViewModel(ciudadAVisitar)
        setCalendar()
        binding.myRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireNotNull(activity).application)
            adapter = BucketAdapter(scheduleViewModel,
                { addActivityToBucket(it) },
                { showActivityDetails(it) })
            setUpItemTouchHelper(this)
        }
        setBarTitle()
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.myRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addActivityToBucket(actividadARealizar: ActividadARealizar) {
        scheduleViewModel.getPossibleActivitiesForBucket(actividadARealizar,
            { actividades -> goToAddActivity(actividades) },
            {})
    }

    private fun goToAddActivity(actividades: List<Actividad>) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", ADD_ACTIVITY_CODE)
            putExtras(bundleOf("actividades" to actividades))
        }
        startActivityForResult(intent, ADD_ACTIVITY_CODE)
    }

    private fun showActivityDetails(actividadARealizar: ActividadARealizar) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", VIEW_ACTIVITY_DETAILS_CODE)
            putExtras(bundleOf("actividad" to actividadARealizar.detalle_actividad))
        }
        startActivity(intent)
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("Itinerario")
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
        horizontalCalendar.selectDate(CiudadAVisitarDate.date.value, false)
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                scheduleViewModel.updateDate(date)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val actividad: Actividad = data?.extras?.get("actividad") as Actividad
                scheduleViewModel.addActividadToBucket(actividad)
            }
        }
    }

    private fun setUpItemTouchHelper(recyclerView: RecyclerView) {

        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                var deleteBackground: Drawable? = null
                var deleteDrawable: Drawable? = null
                var drawableMargin: Int = 0
                var initiated: Boolean = false

                private fun init() {
                    deleteBackground = ColorDrawable(Color.RED)
                    deleteDrawable = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_delete_white_24dp
                    )
                    deleteDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    drawableMargin =
                        context!!.resources
                            .getDimension(R.dimen.ic_clear_margin)
                            .toInt()
                    initiated = true
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    if ((recyclerView.adapter as BucketAdapter).getItemViewType(viewHolder.adapterPosition) == ActivityType.ACTIVITY) {
                        if (swipeDir == ItemTouchHelper.RIGHT)
                            (recyclerView.adapter as BucketAdapter).remove(
                                viewHolder.adapterPosition
                            )
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if ((recyclerView.adapter as BucketAdapter).getItemViewType(viewHolder.adapterPosition) == ActivityType.ACTIVITY) {
                        val itemView = viewHolder.itemView
                        if (viewHolder.adapterPosition == -1)
                            return
                        if (!initiated)
                            init()
                        if (dX > 0)
                            swipeRight(itemView, dX, c, deleteDrawable, deleteBackground)
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                    }
                }

                private fun swipeRight(
                    itemView: View,
                    dX: Float,
                    c: Canvas,
                    drawable: Drawable?,
                    background: Drawable?
                ) {
                    background!!.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt(),
                        itemView.bottom
                    )
                    background.draw(c)
                    val xMarkLeft = itemView.left + drawableMargin
                    val xMarkRight =
                        itemView.left + drawableMargin + drawable!!.intrinsicWidth
                    val xMarkTop =
                        itemView.top + (itemView.bottom - itemView.top - drawable.intrinsicWidth) / 2
                    val xMarkBottom = xMarkTop + drawable.intrinsicWidth
                    drawable.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                    drawable.draw(c)
                }

            }

        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

}

