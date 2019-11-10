package com.android.itrip.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.android.itrip.ActivitiesActivity
import com.android.itrip.R
import com.android.itrip.RequestCodes
import com.android.itrip.databinding.FragmentActivityDetailsBinding
import com.android.itrip.models.Actividad
import com.android.itrip.viewModels.ActivitiesViewModel
import com.android.itrip.viewModels.ActivitiesViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_activities.*
import kotlinx.android.synthetic.main.app_bar.view.*


class ActivityDetailsFragment : Fragment() {

    private var actividad: Actividad? = null
    private var action = 0
    private lateinit var binding: FragmentActivityDetailsBinding
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var isCurrencyDollar = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_activity_details, container, false)
        setBarTitle()
        bindings()
        setCost()
        setCategories()
        setMap()
        setDuration()
        return binding.root
    }

    private fun bindings() {
        actividad = arguments?.get("actividad") as Actividad?
        action = arguments?.getInt("action") ?: 0
        activitiesViewModel =
            ViewModelProviders.of(
                this,
                ActivitiesViewModelFactory(
                    null,
                    activity!!.application,
                    actividad
                )
            ).get(ActivitiesViewModel::class.java)
        binding.activitiesViewModel = activitiesViewModel
        actividad?.imagen?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .into(binding.activityImg)
        }
        binding.mapFrameLayout.setOnClickListener {
            it.findNavController()
                .navigate(
                    ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToMapsFragment().actionId,
                    bundleOf(
                        "actividad" to actividad,
                        "action" to action
                    )
                )
        }
        binding.addActivityFloatingActionButton.apply {
            when (action) {
                RequestCodes.VIEW_ACTIVITY_DETAILS_CODE -> this.hide()
                RequestCodes.VIEW_ACTIVITY_LIST_CODE -> this.hide()
                else -> setOnClickListener {
                    (activity as ActivitiesActivity).finishActivity(actividad)
                }
            }
        }
        binding.shareActivityFloatingActionButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                shareActivity()
            }
        }
        binding.activityCostTextview.setOnClickListener {
            if (actividad!!.costo != 0) {
                if (isCurrencyDollar) {
                    isCurrencyDollar = false
                    binding.activityCostTextview.text =
                        getString(R.string.activity_details_cost_pesos, actividad!!.costo * 63)
                } else {
                    isCurrencyDollar = true
                    binding.activityCostTextview.text =
                        getString(R.string.activity_details_cost_dollar, actividad!!.costo)
                }
            }
        }
    }

    private fun setCategories() {
        activitiesViewModel.categorias.observeForever {
            binding.activitycategoriesTextview.text = activitiesViewModel.listOfCategories()
        }
    }

    private fun setCost() {
        binding.activityCostTextview.text = if (actividad!!.costo != 0) {
            getString(R.string.activity_details_cost_dollar, actividad!!.costo)
        } else {
            getString(R.string.activity_details_cost_free)
        }
    }

    private fun setMap() {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val mapsFragment = MapsFragment()
        mapsFragment.arguments = bundleOf("actividad" to actividad)
        fragmentTransaction?.add(R.id.map_fragment, mapsFragment)
        fragmentTransaction?.commit()
    }

    private fun setDuration() {
        binding.activityAvailabilityDurationTextview.text = when (actividad!!.duracion) {
            1 -> getString(R.string.activity_details_duration_short)
            2, 3 -> getString(R.string.activity_details_duration_medium)
            4, 5 -> getString(R.string.activity_details_duration_large)
            else -> getString(R.string.activity_details_duration_all_day)
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101 && grantResults[0] == PERMISSION_GRANTED) {
            shareActivity()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun shareActivity() {
        val shareIntent = Intent()
        val bitmapDrawable: BitmapDrawable =
            binding.activityImg.drawable as BitmapDrawable
        val bitmap1: Bitmap
        bitmap1 = bitmapDrawable.bitmap
        val imgBitmapPath =
            MediaStore.Images.Media.insertImage(
                context!!.contentResolver,
                bitmap1,
                actividad?.nombre,
                null
            )
        val imgBitmapUri = Uri.parse(imgBitmapPath)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Realmente necesitamos hacer esto!\n" + actividad?.nombre + "\n" + actividad?.descripcion
        )
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
        ContextCompat.startActivity(context!!, Intent.createChooser(shareIntent, "send"), null)
    }

    private fun setBarTitle() {
        with(activity as ActivitiesActivity) {
            setActionBarTitle("Actividad")
            // show toolbar shadow
            app_bar_activities.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

}