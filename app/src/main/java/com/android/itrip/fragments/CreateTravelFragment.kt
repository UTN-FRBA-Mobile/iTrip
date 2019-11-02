package com.android.itrip.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
        bindings()
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.travels_creation))
    }

    private fun bindings() {
        binding.apply {
            // when on focus changes it closes the keyboard because date inputs cant be written
            textinputlayoutTravelName.editText.setOnFocusChangeListener { _, hasFocus ->
                closeKeyboard(hasFocus)
            }
            // touching EditText opens the dialog
            textinputlayoutTravelFromDate.editText.apply {
                setFocusableBehavior()
                setOnClickListener {
                    closeKeyboard(false)
                    setFromDate()
                }
            }
            // touching EditText opens the dialog
            textinputlayoutTravelUntilDate.editText.apply {
                setFocusableBehavior()
                setOnClickListener {
                    closeKeyboard(false)
                    setUntilDate()
                }
            }
            // touching ImageButton opens the dialog
            imagebuttonTravelFromDate.setOnClickListener {
                closeKeyboard(false) // force to close keyboard
                setFromDate()
            }
            // touching ImageButton opens the dialog
            imagebuttonTravelUntilDate.setOnClickListener {
                closeKeyboard(false) // force to close keyboard
                setUntilDate()
            }
            createTravel.setOnClickListener { view -> createTravel(view) }
        }
    }

    private fun EditText.setFocusableBehavior() {
        // set common behavior for open dialog from EditText components
        keyListener = null
        isFocusableInTouchMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) focusable = View.NOT_FOCUSABLE
    }

    private fun setFromDate() {
        val newFragment = DatePickerFragment(
            callback = { calendar ->
                minDate = calendar
                binding.textinputlayoutTravelFromDate.editText.setText(
                    calendarToString(minDate, "yyyy-MM-dd")
                )
            },
            minDate = Calendar.getInstance(),
            maxDate = maxDate,
            startDate = minDate
        )
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun setUntilDate() {
        val newFragment = DatePickerFragment(
            callback = { calendar ->
                maxDate = calendar
                binding.textinputlayoutTravelUntilDate.editText.setText(
                    calendarToString(maxDate!!, "yyyy-MM-dd")
                )
            },
            minDate = minDate,
            maxDate = null,
            startDate = maxDate
        )
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun closeKeyboard(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.root.let { v ->
                val imm =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
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
