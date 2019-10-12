package com.android.itrip.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.android.itrip.util.VolleySingleton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.logging.Logger

object ApiService : Service() {


    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var queue: VolleySingleton

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setContext(context: Context) {
        queue = VolleySingleton.getInstance(context)
    }

    fun post(
        uri: String,
        body: JSONObject,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(
                Method.POST, AuthenticationService.base_api_url + uri, body,
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


    fun get(
        uri: String,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(
                Method.GET, AuthenticationService.base_api_url + uri, null,
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

    fun getArray(
        uri: String,
        responseHandler: (JSONArray) -> Unit,
        errorHandler: (VolleyError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonArrayRequest(
                Method.GET, AuthenticationService.base_api_url + uri, null,
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