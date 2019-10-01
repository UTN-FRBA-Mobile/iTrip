package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentCreateTravelBinding

/**
 * A simple [Fragment] subclass.
 */
class CreateTravelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateTravelBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_travel, container, false
        )
        binding.createTravel.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(CreateTravelFragmentDirections.actionCreateTravelFragmentToActivitiesHomeFragment())
        }
        binding.selectDestiny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(CreateTravelFragmentDirections.actionCreateTravelFragmentToDestinationListFragment())
        }
        return binding.root
    }


}
