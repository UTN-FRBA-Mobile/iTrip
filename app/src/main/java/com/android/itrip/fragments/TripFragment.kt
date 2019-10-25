package com.android.itrip.fragments


import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.adapters.TripAdapter
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.models.Viaje
import com.android.itrip.viewModels.TripViewModel
import java.util.logging.Logger

class TripFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var tripViewModel: TripViewModel
    private lateinit var application: Application
    private lateinit var binding: FragmentTripBinding
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireNotNull(this.activity).application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        try {
            viaje = this.arguments!!.get("viaje") as Viaje
            getDestinations(viaje)
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        binding.addDestinationFloatingactionbutton.setOnClickListener {
            val bundle = bundleOf(
                "viaje" to viaje
            )
            it.findNavController()
                .navigate(
                    TripFragmentDirections.actionTripFragmentToDestinationListFragment().actionId
                    , bundle
                )
        }
        return binding.root
    }

    private fun getDestinations(viaje: Viaje) {
        if (viaje.ciudades_a_visitar.isNullOrEmpty()) {
            binding.tripLinearLayout.visibility = View.VISIBLE
        } else {
            tripViewModel = TripViewModel(
                application, viaje
            )
            binding.tripRecyclerview.apply {
                layoutManager = LinearLayoutManager(application)
                adapter = TripAdapter(tripViewModel.viaje.ciudades_a_visitar)
            }
            binding.tripLinearLayout.visibility = View.GONE
        }
    }
}