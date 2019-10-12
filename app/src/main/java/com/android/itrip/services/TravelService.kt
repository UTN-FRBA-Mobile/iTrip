package com.android.itrip.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.android.itrip.database.Destination
import com.android.itrip.database.Trip
import com.android.itrip.models.Pais
import com.android.itrip.util.VolleySingleton
import com.android.volley.AuthFailureError
import com.android.volley.Request.Method.GET
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.logging.Logger


object TravelService : Service() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var queue: VolleySingleton

    private val gson = Gson()

    fun setContext(context: Context) {
        queue = VolleySingleton.getInstance(context)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getDestinations(
        responseHandler: (List<Pais>) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getDestinations.")
        val url = "destinos/"
        getArray(url, {
            val paisesList: MutableList<Pais> = arrayListOf(Pais())
            val paises = it.getJSONObject(0).getJSONArray("paises")
            for (i in 0 until paises.length()) {
                val pais =
                    gson.fromJson(paises.getJSONObject(i).toString(), Pais::class.java)
                paisesList.add(pais)
                logger.info("Destino: " + pais.nombre)
            }
            responseHandler(paisesList)
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

    fun getActivities(destination: Destination): JsonArrayRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            logger.info("getActivities.")
            val url =
                """${AuthenticationService.base_api_url}destinos/${destination.destinationId}/"""
            return object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener {
                    logger.info("getActivities: $it")
                },
                Response.ErrorListener {
                    logger.info("Error in getActivities(): $it")
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


    private fun post(
        uri: String,
        body: JSONObject,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(POST, AuthenticationService.base_api_url + uri, body,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(error) }) {
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
        queue.addToRequestQueue(request, initialTimeoutMs)
    }


    private fun get(
        uri: String,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(GET, AuthenticationService.base_api_url + uri, null,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(error) }) {
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
        queue.addToRequestQueue(request, initialTimeoutMs)
    }

    private fun getArray(
        uri: String,
        responseHandler: (JSONArray) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonArrayRequest(GET, AuthenticationService.base_api_url + uri, null,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(error) }) {
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
        queue.addToRequestQueue(request, initialTimeoutMs)
    }

}