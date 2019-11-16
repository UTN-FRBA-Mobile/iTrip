package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.database.Destination
import com.android.itrip.fragments.ViajeData
import com.android.itrip.models.*
import com.android.itrip.util.calendarToString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.logging.Logger


object TravelService : Service() {

    private val logger = Logger.getLogger(this::class.java.name)
    private val gson = Gson()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getDestinations(
        responseHandler: (List<Continente>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        ApiService.getArray("destinos/", {
            val listType = object : TypeToken<List<Continente>>() {}.type
            val continentes: List<Continente> = gson.fromJson(it.toString(), listType)
            responseHandler(continentes)
        }, errorHandler)
    }

    fun getTravels(
        responseHandler: (List<Viaje>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        ApiService.getArray("viajes/", {
            val viajesCreator: List<ViajeCreator> =
                gson.fromJson(it.toString(), object : TypeToken<List<ViajeCreator>>() {}.type)
            responseHandler(viajesCreator.map { it.viaje() })
        }, errorHandler)
    }

    fun getTrip(
        id: Long,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getTrip.")
        ApiService.get("viajes/$id", {
            val viajeCreator: ViajeCreator = gson.fromJson(it.toString(), ViajeCreator::class.java)
            val viaje: Viaje = viajeCreator.viaje()
            responseHandler(viaje)
        }, errorHandler)
    }

    fun createTrip(
        body: ViajeData,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        val json = JSONObject(gson.toJson(body))
        ApiService.post("viajes/", json, {
            val viajeCreator: ViajeCreator = gson.fromJson(it.toString(), ViajeCreator::class.java)
            responseHandler(viajeCreator.viaje())
        }, errorHandler)
    }

    fun deleteTrip(
        viajeParam: Viaje,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteTrip.")
        ApiService.delete("""viajes/${viajeParam.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun getActivities(
        destination: Destination,
        responseHandler: (List<Actividad>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getActivities.")
        ApiService.getArray("""destinos/${destination.destinationId}/actividades/""", {
            val listType = object : TypeToken<List<Actividad>>() {}.type
            val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
            responseHandler(actividades)
        }, errorHandler)
    }

    fun postDestination(
        viaje: Viaje,
        destination: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        val json = JSONObject().apply {
            put("ciudad", destination.detalle_ciudad?.id)
            put("inicio", calendarToString(destination.inicio, "yyyy-MM-dd"))
            put("fin", calendarToString(destination.fin, "yyyy-MM-dd"))
        }
        ApiService.post("viajes/${viaje.id}/add_destination/", json, {
            val data = gson.fromJson(it.toString(), CiudadAVisitarCreator::class.java)
            responseHandler(data.ciudadAVisitar())
        }, errorHandler, initialTimeoutMs = 25000, retries = 0)
    }

    fun get_CityToVisit(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("get_CityToVisit.")
        ApiService.get("""ciudad-a-visitar/${ciudad_a_visitarParam.id}/""", {
            val ciudadAVisitarCreator: CiudadAVisitarCreator =
                gson.fromJson(it.toString(), CiudadAVisitarCreator::class.java)
            val ciudad_a_visitar: CiudadAVisitar = ciudadAVisitarCreator.ciudadAVisitar()
            responseHandler(ciudad_a_visitar)
        }, errorHandler)
    }

    fun deleteDestination(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteDestination.")
        ApiService.delete("""ciudad-a-visitar/${ciudad_a_visitarParam.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun deleteToDoActivity(
        actividadARealizar: ActividadARealizar,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteDestination.")
        ApiService.delete("""actividad-a-realizar/${actividadARealizar.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun getActivitiesForBucket(
        bucket: Bucket,
        responseHandler: (List<Actividad>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getActivitiesForBucket.")
        val url =
            """ciudad-a-visitar/${bucket.ciudadAVisitar.id}/posibles-actividades/?dia=${bucket.dia}&bucket_inicio=${bucket.bucket_inicio}"""
        ApiService.getArray(
            url,
            {
                val listType = object : TypeToken<List<Actividad>>() {}.type
                val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
                responseHandler(actividades)
            },
            errorHandler
        )
    }

    fun addActivityToBucket(
        bucket: Bucket,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("addActivityToBucket.")
        val url =
            """ciudad-a-visitar/${bucket.ciudadAVisitar.id}/agregar-actividad/"""
        val json = JSONObject().apply {
            put("actividad", bucket.actividad.id)
            put("dia", bucket.dia)
            put("bucket_inicio", bucket.bucket_inicio)
        }
        ApiService.post(url, json, {
            responseHandler()
        }, errorHandler)
    }

}