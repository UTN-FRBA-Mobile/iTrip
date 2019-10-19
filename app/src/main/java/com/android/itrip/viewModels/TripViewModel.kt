package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.models.Viaje
import java.util.logging.Logger

class TripViewModel(
    application: Application, viaje: Viaje
) : AndroidViewModel(application) {

    lateinit var viaje: Viaje
    private val logger = Logger.getLogger(this::class.java.name)

}