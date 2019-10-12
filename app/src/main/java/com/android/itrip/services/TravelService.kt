package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.database.Destination
import com.android.itrip.database.Trip
import com.android.itrip.models.Actividad
import com.android.itrip.models.Continente
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
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

    fun getTrip(): JsonArrayRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            var trip: Trip
            logger.info("getTrip.")
            val url = AuthenticationService.base_api_url + "viaje/"
            return object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener {
                    logger.info("Viajes: $it")
//                    trip = it
                },
                Response.ErrorListener {
                    logger.info("Error in getTrip(): $it")
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

    fun getTripDetails(id: Long): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            logger.info("getTripDetails.")
            val url = AuthenticationService.base_api_url + "viaje/" + id
            return object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener {
                    logger.info("getTripDetails: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in getTripDetails(): $it")
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


    fun createTrip(trip: Trip): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {

            val params = HashMap<String, String>()
            params["nombre"] = trip.name
            params["inicio"] = trip.startDate.toString()
            params["fin"] = trip.endDate.toString()

            logger.info("createTrip.")
            val url = AuthenticationService.base_api_url + "viaje/"
            return object : JsonObjectRequest(
                Method.POST, url, JSONObject(params),
                Response.Listener {
                    logger.info("createTrip: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in createTrip(): $it")
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

    fun updateTrip(trip: Trip): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {

            val params = HashMap<String, String>()
            params["nombre"] = trip.name
            params["inicio"] = trip.startDate.toString()
            params["fin"] = trip.endDate.toString()

            logger.info("updateTrip.")
            val url = """${AuthenticationService.base_api_url}viaje/${trip.tripId}/"""
            return object : JsonObjectRequest(
                Method.PATCH, url, JSONObject(params),
                Response.Listener {
                    logger.info("updateTrip: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in updateTrip(): $it")
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

    fun deleteTrip(trip: Trip): JsonObjectRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            logger.info("deleteTrip.")
            val url = """${AuthenticationService.base_api_url}viaje/${trip.tripId}/"""
            return object : JsonObjectRequest(
                Method.DELETE, url, null,
                Response.Listener {
                    logger.info("deleteTrip: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in deleteTrip(): $it")
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