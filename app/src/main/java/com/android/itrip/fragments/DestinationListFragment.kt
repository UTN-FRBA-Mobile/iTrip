package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.adapters.DestinationAdapter
import com.android.itrip.database.DestinationDatabase
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.util.VolleyController
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import java.util.logging.Logger


/**
 * A simple [Fragment] subclass.
 */
class DestinationListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DestinationAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val logger = Logger.getLogger(this::class.java.name)

    lateinit var destinationsViewModel: DestinationViewModel
    val queue = VolleyController.getInstance(context)!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentDestinationListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = DestinationDatabase.getInstance(application).destinationDatabaseDao

        val viewModelFactory = DestinationViewModelFactory(dataSource, application)

        destinationsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(DestinationViewModel::class.java)


        binding.destinationsViewModel = destinationsViewModel

//        //RECYCLERVIEW logic
        recyclerView = binding.myRecyclerView
        viewManager = LinearLayoutManager(application)
        recyclerView.layoutManager = viewManager

        viewAdapter = DestinationAdapter(destinationsViewModel.destinations)
        recyclerView.adapter = viewAdapter


        binding.addDestinations.setOnClickListener { view: View ->

            viewAdapter.checkedDestinations.forEach {
                logger.info("destination.name: " + it.name)
            }
            val bundle = bundleOf(
                "destinations" to viewAdapter.checkedDestinations
            )
            view.findNavController()
                .navigate(
                    DestinationListFragmentDirections.actionDestinationListFragmentToCreateTravelFragment().actionId
                    , bundle
                )
        }

        binding.lifecycleOwner = this
        subscribeUi(viewAdapter)

        return binding.root
    }

    private fun subscribeUi(adapter: DestinationAdapter) {
        destinationsViewModel.destinations.observe(viewLifecycleOwner, Observer {
            try {
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                logger.info(e.toString())
            }
        })
    }

}
