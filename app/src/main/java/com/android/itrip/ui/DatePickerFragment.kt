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
    private val maxDate: Calendar?
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    constructor(callback: (Calendar) -> Unit) : this(callback, null, null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context!!, this, year, month, day)
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