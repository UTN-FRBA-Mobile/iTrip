package com.android.itrip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
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
            mapDestination = this.arguments!!.get("mapDestination") as MapDestination
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        try {
            mapDestinations = this.arguments!!.get("mapDestinations") as List<MapDestination>
            logger.info(".destinations.size: " + mapDestinations.size)
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val fragment = childFragmentManager.findFragmentById(R.id.map)
        mapFragment = fragment as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapDestination?.let {
            val destinationLatLng =
                LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(destinationLatLng).title(it.name))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 10.0f))
        }
        if (!mapDestinations.isNullOrEmpty()) {
            var latitudeAverage = 0.0
            var longitudeAverage = 0.0
            var counter = 0
            mapDestinations.forEach { destinationToPin: MapDestination ->
                if (destinationToPin.latitude != 0.0 && destinationToPin.longitude != 0.0) {
                    counter++
                    val destinationLatLng =
                        LatLng(destinationToPin.latitude, destinationToPin.longitude)
                    latitudeAverage += destinationToPin.latitude
                    longitudeAverage += destinationToPin.longitude
                    mMap.addMarker(
                        MarkerOptions().position(destinationLatLng).title(
                            destinationToPin.name
                        )
                    )
                }
            }
            val destinationLatLng =
                LatLng(latitudeAverage / counter, longitudeAverage / counter)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 8.0f))
        }
    }
}