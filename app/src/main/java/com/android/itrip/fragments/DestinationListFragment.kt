package com.android.itrip.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.*
import com.android.itrip.adapters.DestinationAdapter
import com.android.itrip.database.Destination
import com.android.itrip.database.DestinationDatabase
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.dialogs.DestinationDialog
import com.android.itrip.models.Actividad
import com.android.itrip.models.Viaje
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory

class DestinationListFragment : Fragment() {

    private lateinit var binding: FragmentDestinationListBinding
    lateinit var destinationsViewModel: DestinationViewModel
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
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
            recyclerviewDestinationList.apply {
                layoutManager = LinearLayoutManager(application)
                adapter = DestinationAdapter(
                    context,
                    destinationsViewModel,
                    { showDestinationDialog(it) },
                    { viewActivities(it) })
            }
            lifecycleOwner = this@DestinationListFragment
        }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("Seleccioná un destino")
    }

    private fun viewActivities(destination: Destination) {
        destinationsViewModel.getActivities(destination, { goToActivities(it) }, {})
    }

    private fun goToActivities(actividades: List<Actividad>) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", RequestCodes.VIEW_ACTIVITY_LIST_CODE)
            putExtras(bundleOf("actividades" to actividades))
        }
        startActivity(intent)
    }

    private fun goToTrip() {
        view!!.findNavController()
            .navigate(
                DestinationListFragmentDirections.actionDestinationListFragmentToTripFragment().actionId,
                bundleOf("viajeID" to viaje.id)
            )
    }

    private fun showDestinationDialog(destination: Destination) {
        DestinationDialog(this, viaje, destinationsViewModel) {
            destinationAdded(destination)
        }
    }

    private fun destinationAdded(destination: Destination) {
        val spinner = binding.progressbarDestinationListSpinner.apply {
            AppWindowManager.disableScreen(activity!!)
            visibility = View.VISIBLE
        }
        destinationsViewModel.addDestination(viaje, destination, {
            AppWindowManager.enableScreen(activity!!)
            spinner.visibility = View.GONE
            goToTrip()
        }, {
            AppWindowManager.enableScreen(activity!!)
            spinner.visibility = View.GONE
        })
    }

}
