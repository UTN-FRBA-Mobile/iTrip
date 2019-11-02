package com.android.itrip.fragments


import android.Manifest
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
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.models.Actividad
import com.android.itrip.models.MapDestination
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory


class ActivitiesListFragment : Fragment() {

    lateinit var activitiesViewModel: ActivitiesViewModel
    lateinit var actividades: List<Actividad>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivitiesListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_list, container, false
        )
        @Suppress("UNCHECKED_CAST")
        this.arguments!!.get("actividades")?.let {
            actividades = it as List<Actividad>
        }
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ActivitiesViewModelFactory(
            actividades,
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
            adapter = ActivitiesAdapter(activitiesViewModel.actividades) { requestPermission() }
        }
        binding.mapsFloatingActionButton.setOnClickListener { view: View ->
            val mapDestinations: MutableList<MapDestination> = mutableListOf()
            activitiesViewModel.actividades.value!!.forEach {
                mapDestinations.add(MapDestination(it.nombre, it.latitud, it.longitud))
            }
            val bundle = bundleOf(
                "mapDestinations" to mapDestinations
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

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("Actividades")
    }

}
