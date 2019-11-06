package com.android.itrip.dialogs

import android.app.Dialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.databinding.LayoutDestinationDialogBinding
import com.android.itrip.models.Viaje
import com.android.itrip.ui.DatePickerFragment
import com.android.itrip.util.calendarToString
import com.android.itrip.viewModels.DestinationViewModel
import java.util.*

class DestinationDialog(
    private val fragment: Fragment,
    private val viaje: Viaje,
    private val model: DestinationViewModel,
    private val callback: () -> Unit
) : Dialog(fragment.context!!) {

    private var dialog: AlertDialog
    private var binding: LayoutDestinationDialogBinding = DataBindingUtil
        .inflate(
            LayoutInflater.from(fragment.context),
            R.layout.layout_destination_dialog,
            null,
            false
        )

    init {
        dialog = with(AlertDialog.Builder(fragment.context!!)) {
            setTitle(fragment.getString(R.string.destination_creation_dialog_title))
            setView(binding.root)
            create()
        }
        dialog.show()
        bindings()
    }

    private fun bindings() {
        // same bindings for both EditText and ImageButton
        binding.textinputlayoutDestinationDialogFromDate.editText.apply {
            setFocusableBehavior()
            setOnClickListener { view ->
                showDatePickerDialog(viaje.inicio, view as TextView) { calendar ->
                    model.chooseStartDate(calendar)
                }
            }
        }
        binding.textinputlayoutDestinationDialogUntilDate.editText.apply {
            setFocusableBehavior()
            setOnClickListener { view ->
                showDatePickerDialog(viaje.fin, view as TextView) { calendar ->
                    model.chooseEndDate(calendar)
                }
            }
        }
        binding.imagebuttonDestinationDialogFromDate.setOnClickListener { view ->
            showDatePickerDialog(
                viaje.inicio,
                binding.textinputlayoutDestinationDialogFromDate.editText
            ) { calendar ->
                model.chooseStartDate(calendar)
            }
        }
        binding.imagebuttonDestinationDialogUntilDate.setOnClickListener { view ->
            showDatePickerDialog(
                viaje.fin,
                binding.textinputlayoutDestinationDialogUntilDate.editText
            ) { calendar ->
                model.chooseEndDate(calendar)
            }
        }
        // button binding
        binding.buttonDestinationDialogCreation.setOnClickListener {
            callback()
            dialog.cancel()
        }
    }

    private fun showDatePickerDialog(
        startDate: Calendar?,
        textView: TextView,
        callback: (Calendar) -> Unit
    ) {
        val datePickerFragment = DatePickerFragment(
            callback = { calendar ->
                textView.text = calendarToString(calendar)
                callback(calendar)
            },
            minDate = viaje.inicio,
            maxDate = viaje.fin,
            startDate = startDate
        )
        fragment.fragmentManager?.let { datePickerFragment.show(it, "datePicker") }
    }

    private fun EditText.setFocusableBehavior() {
        // set common behavior for open dialog from EditText components
        keyListener = null
        isFocusableInTouchMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) focusable = View.NOT_FOCUSABLE
    }

}
