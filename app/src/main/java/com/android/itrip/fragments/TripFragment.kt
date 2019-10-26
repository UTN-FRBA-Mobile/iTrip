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
import com.android.itrip.R
import com.android.itrip.adapters.TripAdapter
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.models.CiudadAVisitar
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        arguments?.get("viajeID")?.let {
            tripViewModel = TripViewModel(it as Long) { getDestinations() }
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun getDestinations() {
        if (tripViewModel.viaje.value!!.ciudades_a_visitar.isNullOrEmpty()) {
            binding.tripLinearLayout.visibility = View.VISIBLE
        } else {
            binding.tripRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireNotNull(activity).application)
                adapter = TripAdapter(tripViewModel,
                    { deleteCityToVisit(it) },
                    { viewCityToVisit(it) })
            }
            binding.tripLinearLayout.visibility = View.GONE
        }
        binding.addDestinationFloatingactionbutton.setOnClickListener {
            val bundle = bundleOf("viaje" to tripViewModel.viaje.value!!)
            it.findNavController()
                .navigate(
                    TripFragmentDirections.actionTripFragmentToDestinationListFragment().actionId
                    , bundle
                )
        }
    }

    private fun viewCityToVisit(ciudadAVisitar: CiudadAVisitar) {

    }

    private fun deleteCityToVisit(ciudadAVisitar: CiudadAVisitar) {
        logger.info("deleteCityToVisit: " + ciudadAVisitar.detalle_ciudad!!.nombre)
        tripViewModel.deleteCityToVisit(ciudadAVisitar) {
            getDestinations()
            logger.info("deleteCityToVisit")
            binding.tripRecyclerview.invalidate()
        }

    }
}