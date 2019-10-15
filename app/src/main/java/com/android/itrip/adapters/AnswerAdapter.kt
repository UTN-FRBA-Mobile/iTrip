package com.android.itrip.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.android.itrip.models.Answer


class AnswerAdapter(
    context: Activity, @LayoutRes private val resource: Int,
    private val textViewResourceId: Int,
    private val answers: List<Answer>
) :
    ArrayAdapter<Answer>(context, resource, textViewResourceId, answers),
    AdapterView.OnItemSelectedListener {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
            resource,
            parent,
            false
        ).findViewById(textViewResourceId) as TextView
        view.text = answers[position].value
        if (position == 0) view.visibility = View.GONE
        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(context, "onNothingSelected", Toast.LENGTH_SHORT).show()
    }
}