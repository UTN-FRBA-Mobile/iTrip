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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.ActivitiesActivity
import com.android.itrip.R
import com.android.itrip.adapters.ActivitiesAdapter
import com.android.itrip.database.ActivityDatabase
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.models.Actividad
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory
import kotlinx.android.synthetic.main.activity_activities.*
import kotlinx.android.synthetic.main.app_bar.view.*


class ActivitiesListFragment : Fragment() {

    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var actividades: List<Actividad> = emptyList()
    private var action: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivitiesListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_list, container, false
        )
        @Suppress("UNCHECKED_CAST")
        actividades = arguments?.get("actividades") as List<Actividad>
        action = arguments?.getInt("action") ?: 0
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
            adapter = ActivitiesAdapter(activitiesViewModel.actividades) { actividadDetails(it) }
        }
        binding.mapsActivityFloatingActionButton.setOnClickListener {
            it.findNavController().navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToMapsFragment().actionId,
                bundleOf(
                    "actividades" to activitiesViewModel.actividades.value!!,
                    "action" to action
                )
            )
        }
        binding.lifecycleOwner = this
        setBarTitle()
        return binding.root
    }

    private fun actividadDetails(actividad: Actividad) {
        val bundle = bundleOf(
            "actividad" to actividad,
            "action" to action
        )
        findNavController()
            .navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToActivityDetailsFragment().actionId
                , bundle
            )

    }

    private fun setBarTitle() {
        with(activity as ActivitiesActivity) {
            setActionBarTitle("Actividades")
            // show toolbar shadow
            app_bar_activities.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

}
