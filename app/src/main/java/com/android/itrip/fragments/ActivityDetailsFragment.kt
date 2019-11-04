package com.android.itrip.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.navigation.findNavController
import com.android.itrip.ActivitiesActivity
import com.android.itrip.R
import com.android.itrip.RequestCodes
import com.android.itrip.databinding.FragmentActivityDetailsBinding
import com.android.itrip.models.Actividad
import com.squareup.picasso.Picasso


class ActivityDetailsFragment : Fragment() {

    private lateinit var actividad: Actividad
    private var action = 0
    private lateinit var binding: FragmentActivityDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activity_details, container, false
        )
        this.arguments!!.get("actividad")?.let {
            actividad = it as Actividad
        }
        this.arguments!!.get("action")?.let {
            action = it as Int
        }
        binding.activity = actividad
        actividad.imagen?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .into(binding.activityImg)
        }

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val mapsFragment = MapsFragment()
        mapsFragment.arguments = bundleOf("actividad" to actividad)
        fragmentTransaction?.add(R.id.map_fragment, mapsFragment)
        fragmentTransaction?.commit()

        binding.mapFrameLayout.setOnClickListener {
            it.findNavController()
                .navigate(
                    ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToMapsFragment().actionId
                    , bundleOf(
                        "actividad" to actividad
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
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                shareActivity()
            }
        }
        setBarTitle()
        return binding.root
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

    private fun shareActivity(
    ) {
        val shareIntent = Intent()
        val bitmapDrawable: BitmapDrawable =
            binding.activityImg.drawable as BitmapDrawable
        val bitmap1: Bitmap
        bitmap1 = bitmapDrawable.bitmap
        val imgBitmapPath =
            MediaStore.Images.Media.insertImage(
                context!!.contentResolver,
                bitmap1,
                actividad.nombre,
                null
            )
        val imgBitmapUri = Uri.parse(imgBitmapPath)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Realmente necesitamos hacer esto!\n" + actividad.nombre
        )
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
        ContextCompat.startActivity(context!!, Intent.createChooser(shareIntent, "send"), null)
    }

    private fun setBarTitle() {
        (activity as ActivitiesActivity).setActionBarTitle("Actividad")
    }


}