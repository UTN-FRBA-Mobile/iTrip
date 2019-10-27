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
import com.android.itrip.util.calendarToString
import java.util.*
import java.util.logging.Logger

data class ViajeData(val nombre: String, var inicio: String, var fin: String)

class CreateTravelFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var binding: FragmentCreateTravelBinding
    private var minDate: Calendar = Calendar.getInstance()
    private var maxDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_travel, container, false)
        binding.apply {
            imagebuttonTravelFromDate.setOnClickListener {
                showDatePickerDialog(
                    callback = { calendar: Calendar ->
                        minDate = calendar
                        binding.textinputlayoutTravelFromDate.editText.setText(
                            calendarToString(minDate, "yyyy-MM-dd")
                        )
                    },
                    minDate = Calendar.getInstance(),
                    maxDate = maxDate,
                    startDate = minDate
                )

            }
            imagebuttonTravelUntilDate.setOnClickListener {
                showDatePickerDialog(
                    { calendar: Calendar ->
                        maxDate = calendar
                        binding.textinputlayoutTravelUntilDate.editText.setText(
                            calendarToString(maxDate!!, "yyyy-MM-dd")
                        )
                    },
                    minDate = minDate,
                    maxDate = null,
                    startDate = maxDate
                )
            }
            createTravel.setOnClickListener { view -> createTravel(view) }
        }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.travels_creation))
    }

    private fun showDatePickerDialog(
        callback: (Calendar) -> Unit,
        minDate: Calendar?,
        maxDate: Calendar?,
        startDate: Calendar?
    ) {
        val newFragment = DatePickerFragment({ calendar ->
            callback(calendar)
        }, minDate, maxDate, startDate)
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun createTravel(view: View) {
        binding.form.validate()
        if (binding.form.isValid) {
            val request = ViajeData(
                nombre = binding.textinputlayoutTravelName.editText.text.toString(),
                inicio = calendarToString(minDate, "yyyy-MM-dd"),
                fin = calendarToString(maxDate!!, "yyyy-MM-dd")
            )
            TravelService.createTrip(request, {
                val bundle = bundleOf("viajeID" to it.id)
                view.findNavController()
                    .navigate(
                        CreateTravelFragmentDirections.actionCreateTravelFragmentToTripFragment().actionId,
                        bundle
                    )
            }, { error ->
                val message = if (error.statusCode == 400) {
                    error.data.getJSONArray("non_field_errors")[0] as String
                } else {
                    logger.severe("Failed to post new travel - status: ${error.statusCode} - message: ${error.message}")
                    "Hubo un problema, intente de nuevo"
                }
                Toast
                    .makeText(context, message, Toast.LENGTH_SHORT)
                    .show()
            })
        } else {
            logger.severe("All fields are mandatory")
        }
    }

}
