package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.ActivitiesAdapter
import com.android.itrip.database.ActivityDatabase
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.models.MapDestination
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory

class ActivitiesListFragment : Fragment() {

    lateinit var activitiesViewModel: ActivitiesViewModel
    lateinit var destination: Destination

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivitiesListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_list, container, false
        )
        this.arguments!!.get("destination")?.let {
            destination = it as Destination
        }
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ActivitiesViewModelFactory(
            destination,
            ActivityDatabase.getInstance(application).activityDatabaseDao,
            application
        )
        activitiesViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ActivitiesViewModel::class.java)
        binding.activitiesViewModel = activitiesViewModel
        binding.simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activitiesViewModel.updateResults(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                activitiesViewModel.updateResults(newText)
                return true
            }
        })
        binding.myRecyclerView.apply {
            layoutManager = LinearLayoutManager(application)
            adapter = ActivitiesAdapter(activitiesViewModel.actividades)
        }
        binding.mapsFloatingActionButton.setOnClickListener { view: View ->
            val mapDestinations: MutableList<MapDestination> = mutableListOf()
            activitiesViewModel.actividades.value!!.forEach {
                mapDestinations.add(MapDestination(it.nombre, it.latitud, it.longitud))
            }
            val bundle = bundleOf(
                "mapDestinations" to mapDestinations,
                "destination" to destination.name
            )
            view.findNavController().navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToMapsFragment().actionId,
                bundle
            )
        }
        binding.lifecycleOwner = this
        setBarTitle()
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("Actividades de "+destination.name)
    }

}
