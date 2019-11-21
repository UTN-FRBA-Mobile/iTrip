package com.android.itrip.fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.itrip.R
import com.android.itrip.activities.MainActivity
import com.android.itrip.databinding.FragmentCreateTravelBinding
import com.android.itrip.ui.DatePickerFragment
import com.android.itrip.util.RequestCodes.Companion.REQUEST_IMAGE_GET
import com.android.itrip.util.calendarToString
import com.android.itrip.viewModels.CreateTravelViewMovel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.*
import java.util.logging.Logger


class CreateTravelFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var binding: FragmentCreateTravelBinding
    private val createTravelViewModel by lazy { CreateTravelViewMovel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_travel, container, false)
        bindings()
        return binding.root
    }

    private fun setBarTitle() {
        with(activity as MainActivity) {
            setActionBarTitle(getString(R.string.travels_creation))
            // show toolbar shadow
            app_bar.view_toolbar_shadow.visibility = View.VISIBLE
        }
    }

    private fun bindings() {
        binding.apply {
            // when on focus changes it closes the keyboard because date inputs cant be written
            textinputlayoutTravelName.editText.setOnFocusChangeListener { _, hasFocus ->
                closeKeyboard(hasFocus)
            }
            // touching EditText opens the dialog
            textinputlayoutTravelFromDate.editText.apply {
                setFocusableBehavior()
                setOnClickListener {
                    closeKeyboard(false)
                    setFromDate()
                }
            }
            // touching EditText opens the dialog
            textinputlayoutTravelUntilDate.editText.apply {
                setFocusableBehavior()
                setOnClickListener {
                    closeKeyboard(false)
                    setUntilDate()
                }
            }
            // touching ImageButton opens the dialog
            imagebuttonTravelFromDate.setOnClickListener {
                closeKeyboard(false) // force to close keyboard
                setFromDate()
            }
            // touching ImageButton opens the dialog
            imagebuttonTravelUntilDate.setOnClickListener {
                closeKeyboard(false) // force to close keyboard
                setUntilDate()
            }
            createTravel.setOnClickListener { createTravel() }

            binding.addPicture.setOnClickListener {
                selectImage()
            }

        }
    }

    private fun selectImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            //                        putExtra(MediaStore.EXTRA_OUTPUT, Uri.withAppendedPath("".toUri(), ""))
        }
        val getGalleryPhotoIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val getPickerIntent = Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        val chooserIntent = Intent.createChooser(getPickerIntent, "Selecciona una Imagen").apply {
            putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                arrayOf(takePictureIntent,getGalleryPhotoIntent)
            )
        }
        if (chooserIntent.resolveActivity(requireActivity().packageManager) != null)
            startActivityForResult(chooserIntent, REQUEST_IMAGE_GET)
    }


    private fun EditText.setFocusableBehavior() {
        // set common behavior for open dialog from EditText components
        keyListener = null
        isFocusableInTouchMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) focusable = View.NOT_FOCUSABLE
    }

    private fun setFromDate() {
        val newFragment = DatePickerFragment(
            callback = { calendar ->
                createTravelViewModel.viaje.inicio = calendar
                binding.textinputlayoutTravelFromDate.editText.setText(
                    calendarToString(calendar, "dd/MM/yyyy")
                )
            },
            minDate = Calendar.getInstance(),
            maxDate = createTravelViewModel.viaje.fin,
            startDate = createTravelViewModel.viaje.inicio
        )
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun setUntilDate() {
        val newFragment = DatePickerFragment(
            callback = { calendar ->
                createTravelViewModel.viaje.fin = calendar
                binding.textinputlayoutTravelUntilDate.editText.setText(
                    calendarToString(calendar, "dd/MM/yyyy")
                )
            },
            minDate = createTravelViewModel.viaje.inicio,
            maxDate = null,
            startDate = createTravelViewModel.viaje.fin
        )
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun closeKeyboard(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.root.let { v ->
                val imm =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    private fun createTravel() {
        binding.form.validate()
        if (binding.form.isValid) {
            createTravelViewModel.viaje.nombre = binding.textinputlayoutTravelName.editText.text.toString()
            createTravelViewModel.createTrip {
                val bundle = bundleOf("viajeID" to it.id)
                findNavController()
                    .navigate(
                        CreateTravelFragmentDirections.actionCreateTravelFragmentToTripFragment().actionId,
                        bundle
                    )
            }
        } else {
            logger.severe("All fields are mandatory")
        }
    }

}
