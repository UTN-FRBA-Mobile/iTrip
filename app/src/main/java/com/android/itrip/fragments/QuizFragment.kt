package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizBinding

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        binding.nextQuiz.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(QuizFragmentDirections.actionQuizFragmentToQuizEndFragment())
        }
        return binding.root
    }


}
