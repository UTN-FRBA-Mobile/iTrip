package com.android.itrip.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.android.itrip.models.Ciudad
import java.util.logging.Logger

class StorageService(context: Context) : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val logger = Logger.getLogger(this::class.java.name)
    private val databaseService = DatabaseService(context)
    private val travelService = TravelService(context)


    fun getCiudades(): MediatorLiveData<List<Ciudad>> {
        val databaseData = databaseService.getCiudades()
        val mediatorLiveData = MediatorLiveData<List<Ciudad>>()
        mediatorLiveData.addSource(databaseData) { ciudades -> mediatorLiveData.value = ciudades }
        getCiudadesApi {
            mediatorLiveData.addSource(it) { ciudades ->
                insertCiudadesInDatabase(ciudades)
                mediatorLiveData.value = ciudades
            }
        }
        return mediatorLiveData
    }

    private fun getCiudadesApi(callback: (LiveData<List<Ciudad>>) -> Unit) {
        travelService.getDestinations({
            callback(it)
        }, { error ->
            logger.severe("Failed to retrieve destinations - status: ${error.statusCode} - message: ${error.message}")
            val message = "Hubo un problema, intente de nuevo"
            Toast
                .makeText(null, message, Toast.LENGTH_SHORT)
                .show()
        })
    }

    private fun insertCiudadesInDatabase(ciudades: List<Ciudad>) {
        ciudades.forEach { databaseService.insert(it) }
    }

}