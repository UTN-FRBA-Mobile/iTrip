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
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentTripBinding
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToCreateTravelFragment
import com.android.itrip.models.Viaje
import java.util.logging.Logger

class TripFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    //    private var travelAdapter = TravelAdapter()
    private lateinit var application: Application
    private lateinit var binding: FragmentTripBinding
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            viaje = this.arguments!!.get("destination") as Viaje
            logger.info("viaje: " + viaje.nombre)
            Toast.makeText(context, viaje.nombre, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            logger.info(e.toString())
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false)
        binding.createTravel.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(TripFragmentDirections.actionTripFragmentToDestinationListFragment())
        }
        application = requireNotNull(this.activity).application
//        getTravels()
        return binding.root
    }

    /* private fun getTravels() {
         binding.recyclerviewTravels.layoutManager = LinearLayoutManager(application)
         binding.recyclerviewTravels.itemAnimator = DefaultItemAnimator()
         binding.recyclerviewTravels.adapter = travelAdapter
         TravelService.getTravels({ travels ->
             if (travels.isNotEmpty()) {
                 binding.linearlayoutNoTravels.visibility = INVISIBLE
                 travelAdapter.replaceItems(travels)
             } else {
                 binding.linearlayoutNoTravels.visibility = VISIBLE
             }
         }, { error ->
             logger.info("Failed to get travels: " + error.message)
             Toast
                 .makeText(this.context, "Hubo un problema, intente de nuevo", Toast.LENGTH_SHORT)
                 .show()
         })
     }*/

}
