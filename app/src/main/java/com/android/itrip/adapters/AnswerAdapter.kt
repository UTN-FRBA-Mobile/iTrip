package com.android.itrip.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.android.itrip.models.Answer


class AnswerAdapter(
    context: Activity, @LayoutRes private val resource: Int,
    private val textViewResourceId: Int,
    private val answers: List<Answer>
) :
    ArrayAdapter<Answer>(context, resource, textViewResourceId, answers) {

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
}