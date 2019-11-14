package com.android.itrip.fragments


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.models.Actividad
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory
import kotlinx.android.synthetic.main.activity_activities.*
import kotlinx.android.synthetic.main.app_bar.view.*


class ActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentActivitiesListBinding
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var actividades: List<Actividad> = emptyList()
    private var action: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // indicates that fragment has menu (for search view)
        setBarTitle()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_list, container, false
        )
        @Suppress("UNCHECKED_CAST")
        actividades = arguments?.get("actividades") as List<Actividad>
        action = arguments?.getInt("action") ?: 0
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ActivitiesViewModelFactory(
            actividades,
            application,
            null
        )
        activitiesViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ActivitiesViewModel::class.java)
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
        return binding.root
    }

    private fun actividadDetails(actividad: Actividad) {
        val bundle = bundleOf(
            "actividad" to actividad,
            "action" to action
        )
        findNavController()
            .navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToActivityDetailsFragment().actionId,
                bundle
            )
    }

    private fun setBarTitle() {
        with(activity as ActivitiesActivity) {
            setActionBarTitle("Actividades")
            // show toolbar shadow
            app_bar_activities.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // inflate search view in toolbar
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.searchActivity).actionView as SearchView
        // add search listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activitiesViewModel.updateResults(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                activitiesViewModel.updateResults(newText)
                return true
            }
        })
    }

}
