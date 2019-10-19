package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.database.Destination
import com.android.itrip.models.*
import com.android.volley.VolleyError
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
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getDestinations.")
        val url = "destinos/"
        ApiService.getArray(url, {
            val listType = object : TypeToken<List<Continente>>() {}.type
            val continentes: List<Continente> = gson.fromJson(it.toString(), listType)
            responseHandler(continentes)
        }, errorHandler)
    }

    fun getTravels(
        responseHandler: (List<Viaje>) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        val url = "viajes/"
        ApiService.getArray(url, {
            val listType = object : TypeToken<List<Viaje>>() {}.type
            val viajes: List<Viaje> = gson.fromJson(it.toString(), listType)
            responseHandler(viajes)
        }, errorHandler)
    }

    fun getTrip(
        id: Long,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getTrip.")
        val url = "viajes/$id"
        ApiService.get(url, {
            val viaje: Viaje = gson.fromJson(it.toString(), Viaje::class.java)
            responseHandler(viaje)
        }, errorHandler)
    }

    fun createTrip(
        viajeParam: Viaje,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("createTrip.")
        val url = "viajes/"
        val json = JSONObject(gson.toJson(viajeParam))
        ApiService.post(url, json, {
            val viaje: Viaje = gson.fromJson(it.toString(), Viaje::class.java)
            responseHandler(viaje)
        }, errorHandler)
    }

    fun updateTrip(
        viajeParam: Viaje,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("updateTrip.")
        val url = """viaje/${viajeParam.id}/"""
        val json = JSONObject(gson.toJson(viajeParam))
        ApiService.patch(url, json, {
            val viaje: Viaje = gson.fromJson(it.toString(), Viaje::class.java)
            responseHandler(viaje)
        }, errorHandler)
    }

    fun deleteTrip(
        viajeParam: Viaje,
        responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("deleteTrip.")
        val url = """viaje/${viajeParam.id}/"""
        ApiService.delete(url, {
            responseHandler()
        }, errorHandler)
    }

    fun getActivities(
        destination: Destination, responseHandler: (List<Actividad>) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getActivities.")
        val url = """destinos/${destination.destinationId}/actividades/"""
        ApiService.getArray(url, {
            val listType = object : TypeToken<List<Actividad>>() {}.type
            val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
            responseHandler(actividades)
        }, errorHandler)
    }

    fun postDestination(
        viajeParam: Viaje,
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("postDestination.")
        val url = """viajes/${viajeParam.id}/add_destination/"""
        val json = JSONObject()
        json.put("ciudad", ciudad_a_visitarParam.detalle_ciudad.id)
        json.put("inicio", ciudad_a_visitarParam.inicio)
        json.put("fin", ciudad_a_visitarParam.fin)
        ApiService.post(url, json, {
            val ciudad_a_visitar: CiudadAVisitar =
                gson.fromJson(it.toString(), CiudadAVisitar::class.java)
            responseHandler(ciudad_a_visitar)
        }, errorHandler)
    }

    fun get_CityToVisit(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("get_CityToVisit.")
        val url = """ciudad-a-visitar/${ciudad_a_visitarParam.id}/"""
        ApiService.get(url, {
            val ciudadAVisitarCreator: CiudadAVisitarCreator =
                gson.fromJson(it.toString(), CiudadAVisitarCreator::class.java)
            val ciudad_a_visitar: CiudadAVisitar = ciudadAVisitarCreator.ciudadAVisitar()
            responseHandler(ciudad_a_visitar)
        }, errorHandler)
    }

    fun updateDestination(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("updateDestination.")
        val url = """ciudad-a-visitar/${ciudad_a_visitarParam.id}/"""
        val json = JSONObject(gson.toJson(ciudad_a_visitarParam))
        ApiService.patch(url, json, {
            val ciudad_a_visitar: CiudadAVisitar =
                gson.fromJson(it.toString(), CiudadAVisitar::class.java)
            responseHandler(ciudad_a_visitar)
        }, errorHandler)
    }

    fun deleteDestination(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("deleteDestination.")
        val url =
            """ciudad-a-visitar/${ciudad_a_visitarParam.id}/"""
        ApiService.delete(url, {
            responseHandler()
        }, errorHandler)
    }

}