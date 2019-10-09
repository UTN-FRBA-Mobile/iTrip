package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizEndBinding

/**
 * A simple [Fragment] subclass.
 */
class QuizEndFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuizEndBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_end, container, false
        )
        binding.armarViajeButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(QuizEndFragmentDirections.actionQuizEndFragmentToHomeFragment())
        }
        return binding.root
    }


}
