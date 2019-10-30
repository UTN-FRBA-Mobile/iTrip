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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.ActivityType
import com.android.itrip.adapters.BucketAdapter
import com.android.itrip.databinding.FragmentScheduleBinding
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
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireNotNull(activity).application)
            adapter = BucketAdapter(scheduleViewModel)
            setUpItemTouchHelper()
        }
        setBarTitle()
        binding.lifecycleOwner = this
        return binding.root
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
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                // we want to cache these and not allocate anything repeatedly in the onChildDraw method
                var background: Drawable? = null
                var xMark: Drawable? = null
                var xMarkMargin: Int = 0
                var initiated: Boolean = false

                private fun init() {
                    background = ColorDrawable(Color.RED)
                    xMark = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_delete_white_24dp
                    )
                    xMark!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    xMarkMargin =
                        context!!.resources
                            .getDimension(R.dimen.ic_clear_margin)
                            .toInt()
                    initiated = true
                }

                // not important, we don't want drag & drop
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    if ((mRecyclerView.adapter as BucketAdapter).getItemViewType(viewHolder.adapterPosition) == ActivityType.ACTIVITY)
                        (mRecyclerView.adapter as BucketAdapter).remove(viewHolder.adapterPosition)
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

                        // not sure why, but this method get's called for viewholder that are already swiped away
                        if (viewHolder.adapterPosition == -1) {
                            // not interested in those
                            return
                        }

                        if (!initiated) {
                            init()
                        }

                        // draw red background
                        background!!.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        background!!.draw(c)

                        // draw x mark
                        val itemHeight = itemView.bottom - itemView.top
                        val intrinsicWidth = xMark!!.intrinsicWidth
                        val intrinsicHeight = xMark!!.intrinsicWidth

                        val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                        val xMarkRight = itemView.right - xMarkMargin
                        val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                        val xMarkBottom = xMarkTop + intrinsicHeight
                        xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)

                        xMark!!.draw(c)

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


            }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

}

