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
                    { calendar: Calendar ->
                        minDate = calendar
                        binding.textinputlayoutTravelFromDate.editText.setText(
                            com.android.itrip.util.calendarToString(
                                minDate
                            )
                        )
                    },
                    Calendar.getInstance(),
                    maxDate,
                    minDate
                )
            }
            imagebuttonTravelUntilDate.setOnClickListener {
                showDatePickerDialog(
                    { calendar: Calendar ->
                        maxDate = calendar
                        binding.textinputlayoutTravelUntilDate.editText.setText(
                            com.android.itrip.util.calendarToString(
                                maxDate
                            )
                        )
                    },
                    minDate,
                    null,
                    maxDate
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
                inicio = com.android.itrip.util.calendarToString(minDate),
                fin = com.android.itrip.util.calendarToString(maxDate)
            )
            logger.info("nombre: " + request.nombre)
            logger.info("inicio: " + request.inicio)
            logger.info("fin: " + request.fin)
            TravelService.createTrip(request, {
                val bundle = bundleOf("viajeID" to it.id)
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
