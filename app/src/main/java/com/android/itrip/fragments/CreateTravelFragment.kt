package com.android.itrip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.databinding.FragmentCreateTravelBinding
import com.android.itrip.ui.DatePickerFragment

class CreateTravelFragment : Fragment() {

    private lateinit var binding: FragmentCreateTravelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_travel, container, false
        )
        binding.fromDateTextinputedittext.setOnClickListener { showDatePickerDialog(it) }
        binding.untilDateTextinputedittext.setOnClickListener { showDatePickerDialog(it) }
        binding.createTravel.setOnClickListener { view: View ->
            val bundle = bundleOf(
//                "viaje" to viaje
            )
            view.findNavController()
                .navigate(
                    CreateTravelFragmentDirections.actionCreateTravelFragmentToTripFragment().actionId
                    , bundle
                )
        }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.travels_creation))
    }

    private fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment { year, month, day ->
            Toast.makeText(
                context,
                "$day/$month/$year",
                Toast.LENGTH_SHORT
            ).show()
        }
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

}
