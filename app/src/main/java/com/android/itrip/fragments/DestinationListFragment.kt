package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.itrip.R
import com.android.itrip.database.DestinationDatabase
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class DestinationListFragment : Fragment() {


    private val logger = Logger.getLogger(DestinationListFragment::class.java.name)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDestinationListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = DestinationDatabase.getInstance(application).destinationDatabaseDao

        val viewModelFactory = DestinationViewModelFactory(dataSource, application)

        val destinationsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(DestinationViewModel::class.java)

        binding.destinationsViewModel = destinationsViewModel

        //SEARCHVIEW Logic

        val simpleSearchView = binding.simpleSearchView


        simpleSearchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                logger.info("searchView expanded")
            } else {
                logger.info("searchView not expanded")
            }
        }

        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                destinationsViewModel.updateResults(query!!)
//                binding.textView.text = query
                logger.info("Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                destinationsViewModel.updateResults(newText)
//                binding.textView.text = newText
                logger.info("Llego al querytextchange")
                return true
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }


}
