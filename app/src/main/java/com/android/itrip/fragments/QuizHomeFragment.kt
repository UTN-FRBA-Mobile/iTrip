package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class QuizHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuizHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_home, container, false
        )
        binding.startQuiz.setOnClickListener { view: View ->
            view.findNavController().navigate(QuizHomeFragmentDirections.actionQuizHomeFragmentToQuizFragment())
        }
        return binding.root
    }


}
