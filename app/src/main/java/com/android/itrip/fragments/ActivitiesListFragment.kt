package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.adapters.ActivitiesAdapter
import com.android.itrip.database.ActivityDatabase
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.util.VolleyController
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class ActivitiesListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ActivitiesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val logger = Logger.getLogger(this::class.java.name)

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
            destination = this.arguments!!.get("destination") as Destination
            Toast.makeText(
                context,
                destination.name,
                Toast.LENGTH_SHORT
            ).show()
        }


        val application = requireNotNull(this.activity).application

        val dataSource = ActivityDatabase.getInstance(application).activityDatabaseDao

        val viewModelFactory = ActivitiesViewModelFactory(destination, dataSource, application)

        activitiesViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ActivitiesViewModel::class.java)


        binding.activitiesViewModel = activitiesViewModel

        //SEARCHVIEW Logic

        val simpleSearchView = binding.simpleSearchView


        simpleSearchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                logger.info("searchView expanded")
            } else {
                logger.info("searchView not expanded")
            }
        }

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activitiesViewModel.updateResults(query!!)
//                binding.textView.text = query
                logger.info("Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                activitiesViewModel.updateResults(newText)
//                binding.textView.text = newText
                logger.info("Llego al querytextchange")
                return true
            }
        })


//        //RECYCLERVIEW logic
        recyclerView = binding.myRecyclerView
        viewManager = LinearLayoutManager(application)
        recyclerView.layoutManager = viewManager

        viewAdapter = ActivitiesAdapter(activitiesViewModel.actividades)
        recyclerView.adapter = viewAdapter


        binding.lifecycleOwner = this
        subscribeUi(viewAdapter)

        return binding.root
    }


    private fun subscribeUi(adapter: ActivitiesAdapter) {
        activitiesViewModel.actividades.observe(viewLifecycleOwner, Observer {
            try {
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                logger.info(e.toString())
            }
        })
    }


}
