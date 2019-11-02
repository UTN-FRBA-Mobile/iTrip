package com.android.itrip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.models.MapDestination
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.logging.Logger


class MapsFragment : Fragment(), OnMapReadyCallback {

    private var mapDestination: MapDestination? = null
    private var mapDestinations: List<MapDestination> = emptyList()
    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        try {
            mapDestination = arguments!!.get("mapDestination") as MapDestination
            setBarTitle(mapDestination!!.name)
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        try {
            @Suppress("UNCHECKED_CAST")
            mapDestinations = arguments!!.get("mapDestinations") as List<MapDestination>
            setBarTitle("Mapa")
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapDestination?.let {
            val destinationLatLng =
                LatLng(it.latitude, it.longitude)
            googleMap.addMarker(MarkerOptions().position(destinationLatLng).title(it.name))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 10.0f))
        }
        if (!mapDestinations.isNullOrEmpty()) {
            var latitudeAverage = 0.0
            var longitudeAverage = 0.0
            var counter = 0
            mapDestinations.forEach {
                if (it.latitude != 0.0 && it.longitude != 0.0) {
                    counter++
                    val destinationLatLng =
                        LatLng(it.latitude, it.longitude)
                    latitudeAverage += it.latitude
                    longitudeAverage += it.longitude
                    googleMap.addMarker(
                        MarkerOptions().position(destinationLatLng).title(
                            it.name
                        )
                    )
                }
            }
            val destinationLatLng =
                LatLng(latitudeAverage / counter, longitudeAverage / counter)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 8.0f))
        }
    }

    private fun setBarTitle(title: String) {
        (activity as MainActivity).setActionBarTitle(title)
    }

}