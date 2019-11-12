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
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.dialogs.DestinationDialog
import com.android.itrip.models.Actividad
import com.android.itrip.models.Ciudad
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*

class DestinationListFragment : Fragment() {

    private lateinit var binding: FragmentDestinationListBinding
    private lateinit var destinationsViewModel: DestinationViewModel
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        viaje = arguments?.get("viaje") as Viaje
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )
        loadViewModel()
        bindings()
        return binding.root
    }

    private fun loadViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DestinationViewModelFactory(
            application,
            viaje
        )
        destinationsViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DestinationViewModel::class.java)
    }

    private fun bindings() {
        binding.apply {
            recyclerviewDestinationList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DestinationAdapter(
                    context,
                    destinationsViewModel,
                    { showDestinationDialog(it) },
                    { viewActivities(it) })
            }
            lifecycleOwner = this@DestinationListFragment
        }
    }

    private fun setBarTitle() {
        with(activity as MainActivity) {
            setActionBarTitle("Seleccioná un destino")
            // show toolbar shadow
            app_bar.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    private fun viewActivities(ciudad: Ciudad) {
        destinationsViewModel.getActivities(ciudad, context!!, { goToActivities(it) }, {})
    }

    private fun goToActivities(actividades: List<Actividad>) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", RequestCodes.VIEW_ACTIVITY_LIST_CODE)
            putExtras(bundleOf("actividades" to actividades))
        }
        startActivity(intent)
    }

    private fun goToTrip(ciudadAVisitar: CiudadAVisitar) {
        view!!.findNavController()
            .navigate(
                DestinationListFragmentDirections.actionDestinationListFragmentToScheduleFragment().actionId,
                bundleOf("ciudadAVisitar" to ciudadAVisitar)
            )
    }

    private fun showDestinationDialog(ciudad: Ciudad) {
        DestinationDialog(this, viaje, destinationsViewModel) {
            destinationAdded(ciudad)
        }
    }

    private fun destinationAdded(ciudad: Ciudad) {
        val spinner = binding.progressbarDestinationListSpinner.apply {
            AppWindowManager.disableScreen(activity!!)
            visibility = View.VISIBLE
        }
        destinationsViewModel.addDestination(viaje, ciudad, {
            AppWindowManager.enableScreen(activity!!)
            spinner.visibility = View.GONE
            goToTrip(it)
        }, {
            AppWindowManager.enableScreen(activity!!)
            spinner.visibility = View.GONE
        })
    }

}
