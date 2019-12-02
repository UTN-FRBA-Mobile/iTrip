package com.android.itrip.fragments


import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.activities.ActivitiesActivity
import com.android.itrip.adapters.ActivitiesAdapter
import com.android.itrip.databinding.FragmentActivitiesListBinding
import com.android.itrip.models.Actividad
import com.android.itrip.models.Ciudad
import com.android.itrip.util.RequestCodes.Companion.ADD_ACTIVITY_CODE
import com.android.itrip.util.ShakeDetector
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory
import kotlinx.android.synthetic.main.activity_activities.*
import kotlinx.android.synthetic.main.app_bar.view.*


class ActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentActivitiesListBinding
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var actividades: List<Actividad> = emptyList()
    private var action: Int = 0
    // The following are used for the shake detection
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // indicates that fragment has menu (for search view)
        setBarTitle()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activities_list, container, false
        )
        @Suppress("UNCHECKED_CAST")
        actividades = arguments?.get("actividades") as List<Actividad>
//        actividades = (arguments?.get("actividadesLiveData") as LiveData<List<Actividad>>).value!!
        action = arguments?.getInt("action") ?: 0
        val viewModelFactory = ActivitiesViewModelFactory(
            requireActivity().application,
            ciudad = arguments?.get("ciudad") as Ciudad?,
            actividades = actividades,
            actividad = null
        )
        activitiesViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ActivitiesViewModel::class.java)
        binding.myRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity().application)
            adapter = ActivitiesAdapter(activitiesViewModel.actividades) { actividadDetails(it) }
        }
        binding.mapsActivityFloatingActionButton.setOnClickListener {
            it.findNavController().navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToMapsFragment().actionId,
                bundleOf(
                    "actividades" to activitiesViewModel.actividades.value!!,
                    "action" to action
                )
            )
        }
        if (action == ADD_ACTIVITY_CODE) configShake()
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun configShake() {
        // ShakeDetector initialization
        // ShakeDetector initialization
        mSensorManager =
            getSystemService<SensorManager>(requireContext(), SensorManager::class.java)
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener {
            /*
                         * The following method, "handleShakeEvent(count):" is a stub //
                         * method you would use to setup whatever you want done once the
                         * device has been shook.
                         */
            //            handleShakeEvent(count)
            (activity as ActivitiesActivity).finishActivity(activitiesViewModel.getRandomActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        if (action == ADD_ACTIVITY_CODE) mSensorManager!!.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        if (action == ADD_ACTIVITY_CODE) mSensorManager!!.unregisterListener(mShakeDetector)
        super.onPause()
    }

    private fun actividadDetails(actividad: Actividad) {
        val bundle = bundleOf(
            "actividad" to actividad,
            "action" to action
        )
        findNavController()
            .navigate(
                ActivitiesListFragmentDirections.actionActivitiesListFragmentToActivityDetailsFragment().actionId,
                bundle
            )
    }

    private fun setBarTitle() {
        with(activity as ActivitiesActivity) {
            setActionBarTitle("Actividades")
            // show toolbar shadow
            app_bar_activities.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // inflate search view in toolbar
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.searchActivity).actionView as SearchView
        // add search listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activitiesViewModel.updateResults(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                activitiesViewModel.updateResults(newText)
                return true
            }
        })
    }

}
