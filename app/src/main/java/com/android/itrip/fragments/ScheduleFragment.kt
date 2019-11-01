package com.android.itrip.fragments


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.R.color.colorDarkGreen
import com.android.itrip.adapters.ActivityType
import com.android.itrip.adapters.BucketAdapter
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
    private lateinit var mRecyclerView: RecyclerView
    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
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
        binding.scheduleViewModel = scheduleViewModel
        mRecyclerView = binding.myRecyclerView
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireNotNull(activity).application)
            adapter = BucketAdapter(scheduleViewModel)
            setUpItemTouchHelper()
        }

        Toast.makeText(
            context,
            "<= REMOVER   |   DETALLES =>",
            Toast.LENGTH_LONG
        ).show()
        setBarTitle()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun showActivityDetails(actividadARealizar: ActividadARealizar) {
        val bundle = bundleOf(
            "actividad" to actividadARealizar.detalle_actividad
        )
        findNavController()
            .navigate(
                ScheduleFragmentDirections.actionScheduleFragmentToActivityDetailsFragment().actionId
                , bundle
            )
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
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                scheduleViewModel.updateDate(date)
            }
        }
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private fun setUpItemTouchHelper() {

        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
                var detailsBackground: Drawable? = null
                var detailsDrawable: Drawable? = null
                var deleteBackground: Drawable? = null
                var deleteDrawable: Drawable? = null
                var drawableMargin: Int = 0
                var initiated: Boolean = false

                private fun init() {
                    detailsBackground = ColorDrawable(resources.getColor(colorDarkGreen))
                    detailsDrawable = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_info_black_24dp
                    )
                    detailsDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
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
                    if ((mRecyclerView.adapter as BucketAdapter).getItemViewType(viewHolder.adapterPosition) == ActivityType.ACTIVITY) {
                        when (swipeDir) {
                            ItemTouchHelper.RIGHT -> (mRecyclerView.adapter as BucketAdapter).remove(
                                viewHolder.adapterPosition
                            )
                            ItemTouchHelper.LEFT -> showActivityDetails(
                                (mRecyclerView.adapter as BucketAdapter).getItem(
                                    viewHolder.adapterPosition
                                )
                            )
                        }
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
                    if ((mRecyclerView.adapter as BucketAdapter).getItemViewType(viewHolder.adapterPosition) == ActivityType.ACTIVITY) {
                        val itemView = viewHolder.itemView
                        if (viewHolder.adapterPosition == -1)
                            return
                        if (!initiated)
                            init()
                        if (dX > 0)
                            swipeRight(itemView, dX, c, deleteDrawable, deleteBackground)
                        else if (dX < 0)
                            swipeLeft(itemView, dX, c, detailsDrawable, detailsBackground)
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

                private fun swipeLeft(
                    itemView: View,
                    dX: Float,
                    c: Canvas,
                    drawable: Drawable?,
                    background: Drawable?
                ) {
                    background!!.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)
                    val deleteLeft =
                        itemView.right - drawableMargin - drawable!!.intrinsicWidth
                    val deleteRight = itemView.right - drawableMargin
                    val deleteTop =
                        itemView.top + (itemView.bottom - itemView.top - drawable.intrinsicWidth) / 2
                    val deleteBottom = deleteTop + drawable.intrinsicWidth
                    drawable.setBounds(deleteLeft, deleteTop, deleteRight, deleteBottom)
                    drawable.draw(c)
                }


            }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

}

