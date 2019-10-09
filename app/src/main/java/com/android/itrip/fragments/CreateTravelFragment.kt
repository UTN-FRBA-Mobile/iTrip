package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentCreateTravelBinding
import com.android.itrip.wrappers.DestinationsWrapper
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class CreateTravelFragment : Fragment() {


    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var destinations: List<Destination>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCreateTravelBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_travel, container, false
        )

        this.arguments!!.get("destinations")?.let {
            val destinationWrapper = this.arguments!!.get("destinations") as DestinationsWrapper
            destinations = destinationWrapper.destinations
            destinations.forEach {
                logger.info("destination.name: " + it.name)
            }
        }

        binding.createTravel.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(CreateTravelFragmentDirections.actionCreateTravelFragmentToActivitiesHomeFragment())
        }
        binding.selectDestiny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(CreateTravelFragmentDirections.actionCreateTravelFragmentToDestinationListFragment())
        }
        return binding.root
    }


}
