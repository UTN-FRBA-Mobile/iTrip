package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.database.Destination
import com.android.itrip.database.Trip
import com.android.itrip.models.Actividad
import com.android.itrip.models.Continente
import com.android.itrip.models.Viaje
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
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

    fun getTrips(
        responseHandler: (List<Viaje>) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getTrips.")
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
        logger.info("getTrips.")
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
        val json: JSONObject = JSONObject().getJSONObject(gson.toJson(viajeParam))
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
        logger.info("createTrip.")
        val url = """viaje/${viajeParam.id}/"""
        val json: JSONObject = JSONObject().getJSONObject(gson.toJson(viajeParam))
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
        logger.info("createTrip.")
        val url = """viaje/${viajeParam.id}/"""
        val json: JSONObject = JSONObject().getJSONObject(gson.toJson(viajeParam))
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


    fun postDestination(destination: Destination): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {

            val params = HashMap<String, String>()
            params["ciudad"] = destination.name
            params["inicio"] = destination.startDate.toString()
            params["fin"] = destination.endDate.toString()
            logger.info("postDestination.")
            val url =
                """${AuthenticationService.base_api_url}viaje/${destination.destinationId}/add_destination/"""
            return object : JsonObjectRequest(
                Method.GET, url, JSONObject(params),
                Response.Listener {
                    logger.info("postDestination: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in postDestination(): $it")
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken.value!!
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    headers.forEach {
                        logger.info(it.key + ": " + it.value)
                    }
                    return headers
                }
            }
        }
        return null
    }

    fun updateDestination(destination: Destination): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            val params = HashMap<String, String>()
            params["ciudad"] = destination.name
            params["inicio"] = destination.startDate.toString()
            params["fin"] = destination.endDate.toString()
            logger.info("updateDestination.")
            val url =
                """${AuthenticationService.base_api_url}ciudad-a-visitar/${destination.destinationId}/"""
            return object : JsonObjectRequest(
                Method.PATCH, url, JSONObject(params),
                Response.Listener {
                    logger.info("updateDestination: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in updateDestination(): $it")
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken.value!!
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    headers.forEach {
                        logger.info(it.key + ": " + it.value)
                    }
                    return headers
                }
            }
        }
        return null
    }

    fun deleteDestination(destination: Destination): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            logger.info("deleteDestination.")
            val url =
                """${AuthenticationService.base_api_url}ciudad-a-visitar/${destination.destinationId}/"""
            return object : JsonObjectRequest(
                Method.DELETE, url, null,
                Response.Listener {
                    logger.info("deleteDestination: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in deleteDestination(): $it")
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken.value!!
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    headers.forEach {
                        logger.info(it.key + ": " + it.value)
                    }
                    return headers
                }
            }
        }
        return null
    }

}