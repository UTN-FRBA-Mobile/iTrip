package com.android.itrip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.itrip.R
import com.android.itrip.activities.ActivitiesActivity
import com.android.itrip.models.Actividad
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_activities.*
import kotlinx.android.synthetic.main.app_bar.view.*


class MapsFragment : Fragment(), OnInfoWindowClickListener, OnMapReadyCallback {
    private var actividad: Actividad? = null
    private var actividades: List<Actividad>? = emptyList()
    private var action: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        getTheArguments()
        setBarTitle()
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
        return view
    }

    private fun getTheArguments() {
        actividad = arguments?.get("actividad") as Actividad?
        @Suppress("UNCHECKED_CAST")
        actividades = arguments?.get("actividades") as List<Actividad>?
        action = arguments?.getInt("action") ?: 0
    }

    override fun onMapReady(googleMap: GoogleMap) {
        actividad?.let {
            val destinationLatLng =
                LatLng(it.latitud.toDouble(), it.longitud.toDouble())
            val marker =
                googleMap.addMarker(MarkerOptions().position(destinationLatLng).title(it.nombre))
            marker.tag = it
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(destinationLatLng, 10.0f),
                object : CancelableCallback {
                    override fun onCancel() = Unit

                    override fun onFinish() {
                        marker.showInfoWindow()
                    }
                }
            )
        }
        if (!actividades.isNullOrEmpty()) {
            googleMap.setOnInfoWindowClickListener(this)
            var latitudeAverage = 0.0
            var longitudeAverage = 0.0
            var counter = 0
            actividades?.forEach {
                if (it.latitud.toDouble() != 0.0 && it.longitud.toDouble() != 0.0) {
                    counter++
                    val destinationLatLng =
                        LatLng(it.latitud.toDouble(), it.longitud.toDouble())
                    latitudeAverage += it.latitud.toDouble()
                    longitudeAverage += it.longitud.toDouble()
                    googleMap.addMarker(
                        MarkerOptions().position(destinationLatLng).title(
                            it.nombre
                        )
                    ).tag = it
                }
            }
            val destinationLatLng =
                LatLng(latitudeAverage / counter, longitudeAverage / counter)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 8.0f))
        }
    }

    private fun setBarTitle() {
        with(activity as ActivitiesActivity) {
            if (actividades != null) {
                setActionBarTitle("Mapa") // only map title
            } else {
                setActionBarTitle("Información")
            }
            // show toolbar shadow
            app_bar_activities.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        findNavController().navigate(
            MapsFragmentDirections.actionMapsFragmentToActivityDetailsFragment().actionId,
            bundleOf("actividad" to (marker?.tag as Actividad), "action" to action)
        )
    }

}