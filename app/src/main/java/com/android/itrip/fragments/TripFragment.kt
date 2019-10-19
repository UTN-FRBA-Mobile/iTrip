package com.android.itrip.fragments


import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.adapters.TripAdapter
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.models.Viaje
import com.android.itrip.services.TravelService
import com.android.itrip.viewModels.TripViewModel
import java.util.logging.Logger

class TripFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var tripViewModel: TripViewModel
    //    private var travelAdapter = TravelAdapter()
    private lateinit var application: Application
    private lateinit var binding: FragmentTripBinding
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireNotNull(this.activity).application
        TravelService.getTrip(13, { viaje -> getDestinations(viaje) }, {})
//        try {
//            viaje = this.arguments!!.get("viaje") as Viaje
//            logger.info("viaje: " + viaje.nombre)
//            Toast.makeText(context, viaje.nombre, Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            logger.info(e.toString())
//        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        binding.addDestinationFloatingactionbutton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(TripFragmentDirections.actionTripFragmentToDestinationListFragment())
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
            binding.tripRecyclerview.layoutManager = LinearLayoutManager(application)
            binding.tripRecyclerview.itemAnimator = DefaultItemAnimator()
            val viewAdapter = TripAdapter(viaje.ciudades_a_visitar)
            binding.tripRecyclerview.adapter = viewAdapter
            Toast.makeText(
                context,
                viaje.ciudades_a_visitar[0].detalle_ciudad.nombre,
                Toast.LENGTH_SHORT
            ).show()
            binding.tripLinearLayout.visibility = View.GONE
        }
    }

}
