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
import com.android.itrip.services.TravelService
import com.android.itrip.ui.DatePickerFragment
import com.emmasuzuki.easyform.EasyTextInputLayout
import java.util.logging.Logger

data class ViajeData(val nombre: String, val inicio: String, val fin: String)

class CreateTravelFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var binding: FragmentCreateTravelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_travel, container, false)
        binding.imagebuttonTravelFromDate.setOnClickListener { showDatePickerDialog(binding.textinputlayoutTravelFromDate) }
        binding.imagebuttonTravelUntilDate.setOnClickListener { showDatePickerDialog(binding.textinputlayoutTravelUntilDate) }
        binding.createTravel.setOnClickListener { view -> createTravel(view) }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.travels_creation))
    }

    private fun showDatePickerDialog(input: EasyTextInputLayout) {
        val newFragment = DatePickerFragment { year, month, day ->
            val date = getString(
                R.string.travel_creation_date_format,
                year,
                month + 1 /*fix: month ranges from 0-11*/,
                day
            )
            input.editText.setText(date)
        }
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun createTravel(view: View) {
        binding.form.validate()
        if (binding.form.isValid) {
            val request = ViajeData(
                nombre = binding.textinputlayoutTravelName.editText.text.toString(),
                inicio = binding.textinputlayoutTravelFromDate.editText.text.toString(),
                fin = binding.textinputlayoutTravelUntilDate.editText.text.toString()
            )
            TravelService.createTrip(request, { viaje ->
                val bundle = bundleOf("viaje" to viaje)
                view.findNavController()
                    .navigate(
                        CreateTravelFragmentDirections.actionCreateTravelFragmentToTripFragment().actionId,
                        bundle
                    )
            }, { error ->
                logger.severe("Failed to post new travel: " + error.message)
                Toast
                    .makeText(context, "Hubo un problema, intente de nuevo", Toast.LENGTH_SHORT)
                    .show()
            })
        } else {
            logger.severe("All fields are mandatory")
        }
    }

}
