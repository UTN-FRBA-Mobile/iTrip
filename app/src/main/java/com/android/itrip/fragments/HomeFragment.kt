package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentHomeBinding
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToCreateTravelFragment
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToQuizInfoFragment
import com.android.itrip.fragments.HomeFragmentDirections.Companion.actionHomeFragmentToTravelsFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        binding.createTravel.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(actionHomeFragmentToCreateTravelFragment())
        }

        binding.getTravels.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(actionHomeFragmentToTravelsFragment())
        }

        binding.quiz.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(actionHomeFragmentToQuizInfoFragment())
        }

        return binding.root
    }

}
