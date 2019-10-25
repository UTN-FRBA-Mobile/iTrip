package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizEndBinding

class QuizEndFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        val binding: FragmentQuizEndBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_end, container, false
        )
        binding.armarViajeButton.setOnClickListener {
            it.findNavController()
                .navigate(QuizEndFragmentDirections.actionQuizEndFragmentToHomeFragment())
        }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle("") // no title in this fragment
    }

}
