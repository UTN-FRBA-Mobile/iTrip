package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import java.util.logging.Logger


object TravelService : Service() {

    private val logger = Logger.getLogger(this::class.java.name)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getDestinations(): JsonArrayRequest? {
        if (!AuthenticationService.accessToken.value.isNullOrEmpty()) {
            logger.info("getDestinations.")
            val url = AuthenticationService.base_api_url + "destinos/"
            return object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener {
                    logger.info("Destinos: " + it.toString())
                },
                Response.ErrorListener {
                    logger.info("Error in getDestinations(): " + it.toString())
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken.value!!
                    headers["Authorization"] = "Bearer " + accessToken
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