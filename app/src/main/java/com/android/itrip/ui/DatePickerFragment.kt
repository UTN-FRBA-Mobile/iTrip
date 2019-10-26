package com.android.itrip.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(
    private val callback: (Calendar) -> Unit,
    private val minDate: Calendar?,
    private val maxDate: Calendar?,
    private val startDate: Calendar?
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    constructor(callback: (Calendar) -> Unit) : this(callback, null, null, null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = startDate ?: Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context!!,
            this,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
        minDate?.let {
            datePickerDialog.datePicker.minDate = it.timeInMillis
        }
        maxDate?.let {
            datePickerDialog.datePicker.maxDate = it.timeInMillis
        }
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        callback(Calendar.getInstance().apply { set(year, month, day) })
    }

}