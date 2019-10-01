package com.android.itrip.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.util.logging.Logger


object AuthenticationService : Service() {

    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private var tokenValid: Boolean = false
    private val apiLogin = JSONObject("""{"username":"admin", "password":"SlaidTeam123"}""")
    private var base_api_url = "https://proyecto.brazilsouth.cloudapp.azure.com/rest-api/"
    private val logger = Logger.getLogger(AuthenticationService::class.java.name)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        logger.info("init Event")
    }


    fun requestToken(auth: FirebaseAuth): JsonObjectRequest {
        val url = base_api_url + "token/"

        return JsonObjectRequest(
            Request.Method.POST, url, apiLogin,
            Response.Listener { response ->
                logger.info("Response: %s".format(response.toString()))
                refreshToken = response.getString("refresh")
                accessToken = response.getString("access")
//                auth.signInWithCustomToken(accessToken)
            },
            Response.ErrorListener { response -> logger.info("That didn't work! " + response.message) })
    }

    private fun refreshToken(): JsonObjectRequest {
        val url = base_api_url + "token/refresh"

        val params = HashMap<String, String>()
        params["refresh"] = refreshToken

        return JsonObjectRequest(
            Request.Method.POST, url, JSONObject(params),
            Response.Listener { response ->
                logger.info("Response: %s".format(response.toString()))
                accessToken = response.getString("access")
            },
            Response.ErrorListener { response -> logger.info("That didn't work! " + response.message) })
    }

    private fun validateToken(): JsonObjectRequest {
        val url = base_api_url + "token/verify"

        val params = HashMap<String, String>()
        params["token"] = accessToken

        return JsonObjectRequest(
            Request.Method.POST, url, JSONObject(params),
            Response.Listener { response ->
                tokenValid = true
                logger.info("Response: %s".format(response.toString()))
            },
            Response.ErrorListener { response ->
                if (response.networkResponse.statusCode == 401) {
                    tokenValid = false
                } else {
                    logger.info("That didn't work! Status Code: " + response.networkResponse.statusCode)
                }
            })
    }

    fun validateAndRefreshToken(queue: VolleyController): Boolean {
        queue.addToRequestQueue(validateToken())
        return if (!tokenValid) {
            logger.info("Invalid Token")
            queue.addToRequestQueue(refreshToken())
            queue.addToRequestQueue(validateToken())
            tokenValid
            //TODO logica horrible
        } else {
            true
        }
    }
}