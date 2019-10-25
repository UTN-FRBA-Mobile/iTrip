package com.android.itrip.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentActivityDetailsBinding
import com.android.itrip.models.Actividad
import com.android.itrip.models.MapDestination
import com.squareup.picasso.Picasso

class ActivityDetailsFragment : Fragment() {

    private lateinit var actividad: Actividad

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivityDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activity_details, container, false
        )
        this.arguments!!.get("actividad")?.let {
            actividad = it as Actividad
            Toast.makeText(
                context,
                actividad.nombre,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.activity = actividad
        actividad.imagen?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .into(binding.activityImg)
        }
        binding.activityDescription.movementMethod = ScrollingMovementMethod()
        binding.activityLocation.setOnClickListener {
            val mapDestination = MapDestination(
                binding.activity!!.nombre,
                binding.activity!!.latitud,
                binding.activity!!.longitud
            )
            val bundle = bundleOf(
                "mapDestination" to mapDestination
            )
            it.findNavController()
                .navigate(
                    ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToMapsFragment().actionId
                    , bundle
                )
        }
        return binding.root
    }
}