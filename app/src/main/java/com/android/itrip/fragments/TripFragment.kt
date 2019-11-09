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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.AppWindowManager
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.TripAdapter
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.services.ApiError
import com.android.itrip.viewModels.TripViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.logging.Logger

class TripFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var binding: FragmentTripBinding
    private lateinit var tripAdapter: TripAdapter
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        setBarTitle()
        loadViewModel()
        return binding.root
    }

    private fun setBarTitle() {
        with(activity as MainActivity) {
            setActionBarTitle(getString(R.string.destinations_title))
            // hide toolbar shadow because sub toolbar
            app_bar.view_toolbar_shadow.visibility = View.GONE
        }
    }

    private fun loadViewModel() {
        arguments!!.getLong("viajeID").let {
            // set a spinner when destinations are being loaded
            val spinner = binding.progressbarDestinationsListSpinner.apply {
                AppWindowManager.disableScreen(activity!!)
                visibility = View.VISIBLE
            }
            tripViewModel = TripViewModel(it) { ciudadesAVisitar ->
                getDestinations(ciudadesAVisitar)
                AppWindowManager.enableScreen(activity!!)
                spinner.visibility = View.GONE
            }
        }
        binding.lifecycleOwner = this
    }

    private fun getDestinations(ciudadesAVisitar: List<CiudadAVisitar>) {
        binding.viaje = tripViewModel.viaje
        // if travel has no destinations it shows a friendly warning
        if (ciudadesAVisitar.isNullOrEmpty()) {
            binding.linearlayoutDestinationsNoDestinations.visibility = View.VISIBLE
        } else {
            tripAdapter = TripAdapter(ciudadesAVisitar) { viewCityToVisit(it) }
            binding.recyclerviewDestinations.apply {
                layoutManager =
                    LinearLayoutManager(requireNotNull(this@TripFragment.activity).application)
                adapter = tripAdapter
                setUpItemTouchHelper(this)
            }
            binding.linearlayoutDestinationsNoDestinations.visibility = View.INVISIBLE
        }
        binding.floatingactionbuttonDestinationsCreation.setOnClickListener {
            it.findNavController().navigate(
                TripFragmentDirections.actionTripFragmentToDestinationListFragment().actionId,
                bundleOf("viaje" to tripViewModel.viaje)
            )
        }
    }

    private fun deleteCityToVisit(ciudadAVisitar: CiudadAVisitar) {
        tripViewModel.deleteCityToVisit(
            ciudadAVisitar,
            { deleteCityToVisitSuccess(ciudadAVisitar) },
            { deleteCityToVisitFailure(it) })
    }

    private fun deleteCityToVisitSuccess(ciudadAVisitar: CiudadAVisitar) {
        tripAdapter.removeItem(ciudadAVisitar)
        if (tripAdapter.isEmpty()) {
            binding.linearlayoutDestinationsNoDestinations.visibility = View.VISIBLE
        }
    }

    private fun deleteCityToVisitFailure(error: ApiError) {
        val message = if (error.statusCode == 404) {
            error.data.getString("detail")
        } else {
            logger.severe("Failed to delete city - status: ${error.statusCode} - message: ${error.message}")
            "Hubo un problema, intente de nuevo"
        }
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun viewCityToVisit(ciudadAVisitar: CiudadAVisitar) {
        findNavController().navigate(
            TripFragmentDirections.actionTripFragmentToScheduleFragment().actionId,
            bundleOf(
                "ciudadAVisitar" to ciudadAVisitar
            )
        )
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
                        ItemTouchHelper.RIGHT -> deleteCityToVisit(
                            (recyclerView.adapter as TripAdapter).getItem(
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