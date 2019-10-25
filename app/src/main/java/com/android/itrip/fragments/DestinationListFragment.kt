package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.adapters.DestinationAdapter
import com.android.itrip.database.DestinationDatabase
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.models.Viaje
import com.android.itrip.ui.DatePickerFragment
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import java.util.*
import java.util.logging.Logger

class DestinationListFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    lateinit var destinationsViewModel: DestinationViewModel
    private var viaje: Viaje? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            viaje = this.arguments!!.get("viaje") as Viaje
        } catch (e: Exception) {
            logger.info(e.toString())
        }
        val binding: FragmentDestinationListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DestinationViewModelFactory(
            DestinationDatabase.getInstance(application).destinationDatabaseDao,
            application
        )
        destinationsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(DestinationViewModel::class.java)
        binding.apply {
            destinationsViewModel = this@DestinationListFragment.destinationsViewModel
            fromDate.setOnClickListener {
                showDatePickerDialog(it as TextView) { calendar ->
                    destinationsViewModel?.chooseStartDate(
                        calendar
                    )
                }
            }
            untilDate.setOnClickListener {
                showDatePickerDialog(it as TextView) { calendar ->
                    destinationsViewModel?.chooseEndDate(
                        calendar
                    )
                }
            }
            myRecyclerView.apply {
                layoutManager = LinearLayoutManager(application)
                adapter = DestinationAdapter(destinationsViewModel!!.destinations)
            }
            lifecycleOwner = this@DestinationListFragment
        }
        return binding.root
    }

    private fun showDatePickerDialog(v: TextView, callback: (Calendar) -> Unit) {
        val newFragment = DatePickerFragment({ calendar ->
            v.text = com.android.itrip.util.calendarToString(calendar)
            callback(calendar)
        }, viaje?.inicio, viaje?.fin)
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

}
