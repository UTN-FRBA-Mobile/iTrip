package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.TravelAdapter
import com.android.itrip.databinding.FragmentHomeBinding
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToCreateTravelFragment
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class HomeFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private var travelAdapter = TravelAdapter { deleteTravel(it) }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.floatingactionbuttonTravelsCreation.setOnClickListener {
            it.findNavController()
                .navigate(actionHomeFragmentToCreateTravelFragment())
        }
        getTravels()
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.travels_title))
    }

    private fun deleteTravel(travel: Viaje) {
        TravelService.deleteTrip(travel, {
            travelAdapter.deleteItem(travel)
            if (!travelAdapter.hasTravels()) {
                binding.linearlayoutNoTravels.visibility = VISIBLE
            }
        }, { error ->
            val message = if (error.statusCode == 404) {
                error.data.getString("detail")
            } else {
                logger.severe("Failed to delete travel - status: ${error.statusCode} - message: ${error.message}")
                "Hubo un problema, intente de nuevo"
            }
            Toast
                .makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        })
    }

    private fun getTravels() {
        binding.recyclerviewTravels.apply {
            layoutManager =
                LinearLayoutManager(requireNotNull(this@HomeFragment.activity).application)
            itemAnimator = DefaultItemAnimator()
            adapter = travelAdapter
        }
        TravelService.getTravels({
            if (it.isNotEmpty()) {
                binding.linearlayoutNoTravels.visibility = INVISIBLE
                travelAdapter.replaceItems(it)
            } else {
                binding.linearlayoutNoTravels.visibility = VISIBLE
            }
        }, { error ->
            val message = if (error.statusCode == 400) {
                error.data.getJSONArray("non_field_errors")[0] as String
            } else {
                logger.severe("Failed to get travels - status: ${error.statusCode} - message: ${error.message}")
                "Hubo un problema, intente de nuevo"
            }
            Toast
                .makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        })
    }

}
