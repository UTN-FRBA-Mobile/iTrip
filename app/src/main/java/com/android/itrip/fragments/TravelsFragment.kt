package com.android.itrip.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.adapters.TravelAdapter
import com.android.itrip.databinding.FragmentTravelsBinding
import com.android.itrip.services.TravelService
import java.util.logging.Logger

class TravelsFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private var travelAdapter = TravelAdapter()
    private lateinit var application: Application
    private lateinit var binding: FragmentTravelsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_travels, container, false)
        application = requireNotNull(this.activity).application
        getTravels()
        return binding.root
    }

    private fun getTravels() {
        binding.recyclerviewTravels.layoutManager = LinearLayoutManager(application)
        binding.recyclerviewTravels.itemAnimator = DefaultItemAnimator()
        binding.recyclerviewTravels.adapter = travelAdapter
        TravelService.getTravels({ travels ->
            travelAdapter.replaceItems(travels)
        }, { error ->
            logger.info("Failed to get travels: " + error.message)
            Toast
                .makeText(this.context, "Hubo un problema, intente de nuevo", Toast.LENGTH_SHORT)
                .show()
        })
    }

}