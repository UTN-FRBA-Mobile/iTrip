package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentCreateTravelBinding
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
            destinations = this.arguments!!.get("destinations") as List<Destination>
            var textMessage = ""
            destinations.forEach {
                textMessage = textMessage + it.name + ", "
            }
            textMessage += "agregadas."
            if (destinations.isNotEmpty()) Toast.makeText(
                context,
                textMessage,
                Toast.LENGTH_SHORT
            ).show()
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
