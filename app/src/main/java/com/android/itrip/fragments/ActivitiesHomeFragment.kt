package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentActivitiesHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class ActivitiesHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivitiesHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_home, container, false
        )
        binding.activityButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(ActivitiesHomeFragmentDirections.actionActivitiesHomeFragmentToActivitiesListFragment())
        }
        return binding.root
    }


}
