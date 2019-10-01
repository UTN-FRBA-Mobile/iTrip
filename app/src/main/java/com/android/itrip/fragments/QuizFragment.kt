package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.databinding.FragmentCreateTravelBinding

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateTravelBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        return binding.root
    }


}
