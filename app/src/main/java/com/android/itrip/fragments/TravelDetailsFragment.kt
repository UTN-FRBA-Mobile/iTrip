package com.android.itrip.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.adapters.TravelAdapter
import com.android.itrip.databinding.FragmentTravelDetailsBinding
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class TravelDetailsFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private var travelAdapter = TravelAdapter()
    private lateinit var application: Application
    private lateinit var binding: FragmentTravelDetailsBinding
    private var tripID: Long = 0
    private lateinit var trip: Viaje
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_travel_details, container, false)
        application = requireNotNull(this.activity).application
        getTrip()
        return binding.root
    }

    private fun getTrip() {
        //  binding.recyclerviewTravels.layoutManager = LinearLayoutManager(application)
        // binding.recyclerviewTravels.itemAnimator = DefaultItemAnimator()
        //binding.recyclerviewTravels.adapter = travelAdapter
        TravelService.getTrip(tripID,
            { (Unit) }
            , { error ->
                logger.info("Failed to get travel: " + error.message)
                Toast
                    .makeText(
                        this.context,
                        "Hubo un problema, intente de nuevo",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            })
    }
}