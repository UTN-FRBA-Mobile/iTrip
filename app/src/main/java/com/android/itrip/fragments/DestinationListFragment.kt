package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.FragmentDestinationListBinding

/**
 * A simple [Fragment] subclass.
 */
class DestinationListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDestinationListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )


        var destinationBuenosAires: Destination =
            Destination(1, "Buenos Aires", -34.61315, -58.37723)
        var destinationMendoza: Destination = Destination(2, "Mendoza", -32.89084, -68.82717)
        var destinationTucuman: Destination = Destination(3, "Tucuman", -26.82414, -65.2226)
        var destinationTrelew: Destination = Destination(4, "Trelew", -43.24895, -65.30505)

        return binding.root
    }


}
