package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.TripAdapter
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.services.TravelService
import com.android.itrip.viewModels.TripViewModel
import java.util.logging.Logger

class TripFragment : Fragment() {

    private val logger = Logger.getLogger("prueba")
    private lateinit var binding: FragmentTripBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        arguments!!.getLong("viajeID")
            .let { tripViewModel = TripViewModel(it) { getDestinations() } }
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.destinations_title))
    }

    private fun getDestinations() {
        if (tripViewModel.viaje.value!!.ciudades_a_visitar.isNullOrEmpty()) {
            binding.linearlayoutDestinationsNoDestinations.visibility = View.VISIBLE
        } else {
            binding.recyclerviewDestinations.apply {
                layoutManager = LinearLayoutManager(requireNotNull(activity).application)
                adapter = TripAdapter(tripViewModel,
                    { deleteCityToVisit(it) },
                    { viewCityToVisit(it) })
            }
            binding.linearlayoutDestinationsNoDestinations.visibility = View.INVISIBLE
        }
        binding.floatingactionbuttonDestinationCreation.setOnClickListener {
            val bundle = bundleOf("viaje" to tripViewModel.viaje.value!!)
            it.findNavController().navigate(
                TripFragmentDirections.actionTripFragmentToDestinationListFragment().actionId,
                bundle
            )
        }
    }

    private fun viewCityToVisit(ciudadAVisitar: CiudadAVisitar) {

    }

    private fun deleteCityToVisit(ciudadAVisitar: CiudadAVisitar) {
        logger.info("deleteCityToVisit: " + ciudadAVisitar.detalle_ciudad!!.nombre)
        TravelService.deleteDestination(ciudadAVisitar, {
            tripViewModel.getTravel(null) {
                logger.info("deleteCityToVisit")
                getDestinations()
                binding.recyclerviewDestinations.invalidate()
            }
        }, {})
    }

}