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
    private lateinit var mapDestination: MapDestination

    private val logger = Logger.getLogger(this::class.java.name)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        mapDestination = this.arguments!!.get("mapDestination") as MapDestination
        logger.info("destinationWrapper.destination.name: " + mapDestination.name)


        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        logger.info("Map ID: " + R.id.map)
        val fragment = childFragmentManager?.findFragmentById(R.id.map)
        logger.info("fragment ID: " + fragment?.id)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = fragment as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        logger.info("Map Ready!!")


        val destinationLatLng =
            LatLng(mapDestination.latitude ?: 0.0, mapDestination.longitude ?: 0.0)

        mMap.addMarker(MarkerOptions().position(destinationLatLng).title("Marker in " + mapDestination.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng))
    }
}