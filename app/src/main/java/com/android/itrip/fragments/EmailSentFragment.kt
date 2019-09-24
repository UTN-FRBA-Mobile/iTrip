package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.databinding.FragmentEmailSentBinding

/**
 * A simple [Fragment] subclass.
 */
class EmailSentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEmailSentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_email_sent, container, false)
        return binding.root
    }


}
