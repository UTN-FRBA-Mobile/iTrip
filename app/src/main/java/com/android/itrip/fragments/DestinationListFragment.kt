package com.android.itrip.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.ActivitiesActivity
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.DestinationAdapter
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabase
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.models.Actividad
import com.android.itrip.models.Viaje
import com.android.itrip.ui.DatePickerFragment
import com.android.itrip.util.calendarToString
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import java.util.*

class DestinationListFragment : Fragment() {

    private lateinit var binding: FragmentDestinationListBinding
    lateinit var destinationsViewModel: DestinationViewModel
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viaje = this.arguments!!.get("viaje") as Viaje
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DestinationViewModelFactory(
            DestinationDatabase.getInstance(application).destinationDatabaseDao,
            application,
            viaje
        )
        destinationsViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DestinationViewModel::class.java)
        binding.apply {
            destinationsViewModel = this@DestinationListFragment.destinationsViewModel
            fromDate.setOnClickListener {
                showDatePickerDialog(viaje.inicio, it as TextView) { calendar ->
                    destinationsViewModel?.chooseStartDate(
                        calendar
                    )
                }
            }
            untilDate.setOnClickListener {
                showDatePickerDialog(viaje.fin, it as TextView) { calendar ->
                    destinationsViewModel?.chooseEndDate(
                        calendar
                    )
                }
            }
            myRecyclerView.apply {
                layoutManager = LinearLayoutManager(application)
                adapter = DestinationAdapter(
                    destinationsViewModel!!,
                    { destinationAdded(it) },
                    { viewActivities(it) })
            }
            lifecycleOwner = this@DestinationListFragment
        }
        setBarTitle()
        return binding.root
    }

    private fun viewActivities(destination: Destination) {
        destinationsViewModel.getActivities(destination, { goToActivities(it) }, {})
    }

    private fun goToActivities(actividades: List<Actividad>) {
//        val intent = Intent(context, ActivitiesActivity::class.java).apply {
//            putExtra("CurrentUser", FirebaseAuth.getInstance().currentUser)
//            putExtra("actividades", arrayListOf(actividades))
//        }
//        startActivityForResult(intent, RESULT_OK)
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtras(bundleOf("actividades" to actividades))
        }
        startActivity(intent)
//        activity?.finish()
    }

    private fun destinationAdded(destination: Destination) {
        val spinner = binding.progressbarDestinationsSpinner.apply { visibility = View.VISIBLE }
        destinationsViewModel.addDestination(viaje, destination, {
            spinner.visibility = View.GONE
            goToTrip()
        }, {
            spinner.visibility = View.GONE
        })
    }

    private fun goToTrip() {
        view!!.findNavController()
            .navigate(
                DestinationListFragmentDirections.actionDestinationListFragmentToTripFragment().actionId,
                bundleOf("viajeID" to viaje.id)
            )
    }

    private fun showDatePickerDialog(
        startDate: Calendar?,
        v: TextView,
        callback: (Calendar) -> Unit
    ) {
        val newFragment = DatePickerFragment({ calendar ->
            v.text = calendarToString(calendar)
            callback(calendar)
        }, viaje.inicio, viaje.fin, startDate)
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("Elija un destino")
    }

}
