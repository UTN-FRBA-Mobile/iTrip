package com.android.itrip.fragments


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.activities.AppWindowManager
import com.android.itrip.activities.MainActivity
import com.android.itrip.adapters.TravelAdapter
import com.android.itrip.databinding.FragmentHomeBinding
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToCreateTravelFragment
import com.android.itrip.models.Viaje
import com.android.itrip.util.ApiError
import com.android.itrip.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.logging.Logger

class HomeFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var travelAdapter: TravelAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.floatingactionbuttonTravelsCreation.setOnClickListener {
            it.findNavController()
                .navigate(actionHomeFragmentToCreateTravelFragment())
        }
        loadViewModel()
        return binding.root
    }

    private fun setBarTitle() {
        with(activity as MainActivity) {
            setActionBarTitle(getString(R.string.travels_title))
            // show toolbar shadow
            app_bar.view_toolbar_shadow.visibility = VISIBLE
        }
    }

    private fun loadViewModel() {
        // set a spinner when travels are being loaded
        val spinner = binding.progressbarTravelsListSpinner.apply {
            activity?.let { _activity -> AppWindowManager.disableScreen(_activity) }
            visibility = VISIBLE
        }
        homeViewModel = HomeViewModel(requireActivity().application, {
            getTravelsSuccess(it)
            spinner.visibility = GONE
            activity?.let { _activity -> AppWindowManager.enableScreen(_activity) }
        }, {
            getTravelsFailure(it)
            activity?.let { _activity -> AppWindowManager.enableScreen(_activity) }
        })
    }

    private fun deleteTravel(travel: Viaje) {
        homeViewModel.deleteTravel(
            travel,
            { deleteTravelSuccess(travel) },
            { deleteTravelFailure(it) })
    }

    private fun deleteTravelFailure(error: ApiError) {
        val message = if (error.statusCode == 404) {
            error.data.getString("detail")
        } else {
            logger.severe("Failed to delete travel - status: ${error.statusCode} - message: ${error.message}")
            "Hubo un problema, intente de nuevo"
        }
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun deleteTravelSuccess(travel: Viaje) {
        travelAdapter.removeItem(travel)
        if (!homeViewModel.hasTravels()) {
            binding.linearlayoutNoTravels.visibility = VISIBLE
        }
    }

    private fun getTravelsFailure(error: ApiError) {
        val message = if (error.statusCode == 400) {
            error.data.getJSONArray("non_field_errors")[0] as String
        } else {
            logger.severe("Failed to get travels - status: ${error.statusCode} - message: ${error.message}")
            "Hubo un problema, intente de nuevo"
        }
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getTravelsSuccess(viajes: List<Viaje>) {
        if (viajes.isNotEmpty()) {
            binding.linearlayoutNoTravels.visibility = INVISIBLE
            try {
                setRecyclerView()
            } catch (e: IllegalArgumentException) {
                logger.severe(e.toString())
            }
        } else {
            binding.linearlayoutNoTravels.visibility = VISIBLE
        }
    }

    private fun setRecyclerView() {
        travelAdapter = TravelAdapter(homeViewModel)
        binding.recyclerviewTravels.apply {
            layoutManager =
                LinearLayoutManager(requireNotNull(this@HomeFragment.activity).application)
            itemAnimator = DefaultItemAnimator()
            adapter = travelAdapter
            setUpItemTouchHelper(this)
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
                    when (swipeDir) {
                        ItemTouchHelper.RIGHT -> deleteTravel(
                            (recyclerView.adapter as TravelAdapter).getItem(
                                viewHolder.adapterPosition
                            )
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
                    val itemView = viewHolder.itemView
                    if (viewHolder.adapterPosition == -1) return
                    if (!initiated) init()
                    if (dX > 0) swipeRight(itemView, dX, c, deleteDrawable, deleteBackground)
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
