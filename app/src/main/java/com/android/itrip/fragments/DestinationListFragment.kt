package com.android.itrip.fragments


import android.animation.ObjectAnimator.ofInt
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.R
import com.android.itrip.activities.ActivitiesActivity
import com.android.itrip.activities.AppWindowManager
import com.android.itrip.activities.MainActivity
import com.android.itrip.adapters.DestinationAdapter
import com.android.itrip.databinding.FragmentDestinationListBinding
import com.android.itrip.dialogs.DestinationDialog
import com.android.itrip.models.Actividad
import com.android.itrip.models.Ciudad
import com.android.itrip.models.CiudadAVisitar
import com.android.itrip.models.Viaje
import com.android.itrip.services.DatabaseService
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.DestinationViewModelFactory
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.progressbar_destination_creation.view.*

class DestinationListFragment : Fragment() {

    private lateinit var binding: FragmentDestinationListBinding
    private lateinit var destinationsViewModel: DestinationViewModel
    private lateinit var viaje: Viaje

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        viaje = arguments?.get("viaje") as Viaje
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_destination_list, container, false
        )
        loadViewModel()
        bindings()
        return binding.root
    }

    private fun loadViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DestinationViewModelFactory(
            application,
            viaje
        )
        destinationsViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DestinationViewModel::class.java)
    }

    private fun bindings() {
        binding.apply {
            recyclerviewDestinationList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DestinationAdapter(
                    context,
                    destinationsViewModel,
                    { showDestinationDialog(it) },
                    { viewActivities(it) })
            }
            lifecycleOwner = this@DestinationListFragment
        }
    }

    private fun setBarTitle() {
        with(activity as MainActivity) {
            setActionBarTitle("Seleccioná un destino")
            // show toolbar shadow
            app_bar.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    private fun viewActivities(ciudad: Ciudad) {
        destinationsViewModel.getActivities(
            ciudad, requireContext(),
            { goToActivities(it, ciudad) },
            {})
//        goToActivities2(destinationsViewModel.getActivitiesLiveData(ciudad), ciudad)
    }

    private fun goToActivities(actividades: List<Actividad>, ciudad: Ciudad) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", RequestCodes.VIEW_ACTIVITY_LIST_CODE)
            putExtras(
                bundleOf(
                    "actividades" to actividades,
                    "ciudad" to ciudad
                )
            )
        }
        startActivity(intent)
    }

    private fun goToActivities2(actividades: LiveData<List<Actividad>>, ciudad: Ciudad) {
        val intent = Intent(context, ActivitiesActivity::class.java).apply {
            putExtra("action", RequestCodes.VIEW_ACTIVITY_LIST_CODE)
            putExtras(
                bundleOf(
                    "actividadesLiveData" to actividades,
                    "ciudad" to ciudad
                )
            )
        }
        startActivity(intent)
    }

    private fun goToTrip(ciudadAVisitar: CiudadAVisitar) {
        view!!.findNavController()
            .navigate(
                DestinationListFragmentDirections.actionDestinationListFragmentToScheduleFragment().actionId,
                bundleOf("ciudadAVisitar" to ciudadAVisitar)
            )
    }

    private fun showDestinationDialog(ciudad: Ciudad) {
        DestinationDialog(this, viaje, destinationsViewModel) {
            destinationAdded(ciudad)
        }
    }

    private fun destinationAdded(ciudad: Ciudad) {
        // disable screen to prevent unwanted clicks
        AppWindowManager.disableScreen(activity!!)
        // inflate new view to show the destination creation progress
        val viewProgress = inflateProgress()
        val container = viewProgress.transition as ViewGroup
        val progressBar = viewProgress.progressbar_destination_creation
        val progressText = viewProgress.textview_progress
        var showDuringText = true
        // set up progress objects (main and secondary)
        val progress = ofInt(progressBar, "progress", 0, 100).apply {
            duration = 20000 // the same value as the request timeout
            interpolator = AccelerateInterpolator()
            start()
        }
        val secondaryProgress = ofInt(progressBar, "secondaryProgress", 0, 100).apply {
            duration = 5000 // minimum wait time
            interpolator = LinearInterpolator()
            doOnEnd {
                // changing text with animation
                if (showDuringText) {
                    TransitionManager.beginDelayedTransition(
                        container,
                        ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
                    )
                    progressText.text = getString(R.string.progressbar_during)
                }
            }
            start()
        }
        // call "addDestinations"
        destinationsViewModel.addDestination(viaje, ciudad, {
            // if request finishes before max duration be reached then it ends the progress
            showDuringText = false
            progress.end()
            secondaryProgress.end()
            Handler().postDelayed({
                // show congrats with animation
                TransitionManager.beginDelayedTransition(
                    container,
                    ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
                )
                progressText.text = getString(R.string.progressbar_done)
                // enable screen after processing
                AppWindowManager.enableScreen(activity!!)
                // after 1 sec of delay go to destination schedule view
                Handler().postDelayed({ goToTrip(it) }, 1500)
            }, 1000)
        }, {
            progressText.text = getString(R.string.progressbar_failure)
            progressBar.visibility = View.GONE
            // enable screen even if request fails
            AppWindowManager.enableScreen(activity!!)
        })
    }

    private fun inflateProgress(): ViewGroup {
        val root = binding.relativelayoutDestinationsList as ViewGroup
        root.removeAllViews()
        val progressView =
            layoutInflater.inflate(R.layout.progressbar_destination_creation, root, false)
        root.addView(progressView)
        return root
    }

}
