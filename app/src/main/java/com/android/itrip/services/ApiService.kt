package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.util.VolleyClient
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.logging.Logger

data class ApiError(val statusCode: Int, val message: String? = null, val data: JSONObject)

abstract class ApiService constructor(private val queue: VolleyClient) : Service() {

    protected val logger = Logger.getLogger(this::class.java.name)
    protected val gson = Gson()
    private val base_api_url = "https://proyecto.brazilsouth.cloudapp.azure.com/rest-api/"

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    fun post(
        uri: String,
        body: JSONObject,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (ApiError) -> Unit,
        initialTimeoutMs: Int? = null,
        retries: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(
                Method.POST, base_api_url + uri, body,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(mapOf(error)) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken
                    if (!accessToken.isBlank()) {
                        headers["Authorization"] = "Bearer $accessToken"
                    }
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.addToRequestQueue(request, initialTimeoutMs, retries)
    }

    fun patch(
        uri: String,
        body: JSONObject,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (ApiError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(
                Method.PATCH, base_api_url + uri, body,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(mapOf(error)) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken
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

    fun delete(
        uri: String,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : StringRequest(
                Method.DELETE,
                base_api_url + uri,
                Response.Listener { responseHandler() },
                Response.ErrorListener { error -> errorHandler(mapOf(error)) }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.addToRequestQueue(request, initialTimeoutMs)
    }

    fun get(
        uri: String,
        responseHandler: (JSONObject) -> Unit,
        errorHandler: (ApiError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonObjectRequest(
                Method.GET, base_api_url + uri, null,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(mapOf(error)) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.addToRequestQueue(request, initialTimeoutMs)
    }

    fun getArray(
        uri: String,
        responseHandler: (JSONArray) -> Unit,
        errorHandler: (ApiError) -> Unit,
        initialTimeoutMs: Int? = null
    ) {
        val request =
            object : JsonArrayRequest(
                Method.GET, base_api_url + uri, null,
                Response.Listener { response -> responseHandler(response) },
                Response.ErrorListener { error -> errorHandler(mapOf(error)) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken: String = AuthenticationService.accessToken
                    headers["Authorization"] = "Bearer $accessToken"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
        queue.addToRequestQueue(request, initialTimeoutMs)
    }

    private fun mapOf(error: VolleyError): ApiError {
        try {
            val dataString = error.networkResponse.data.toString(Charsets.UTF_8)
            return ApiError(
                statusCode = error.networkResponse.statusCode,
                message = error.message,
                data = JSONObject(dataString)
            )
        } catch (e: JSONException) {
            logger.severe(e.message)
            return ApiError(
                statusCode = error.networkResponse.statusCode,
                message = error.message,
                data = JSONObject()
                    .put("non_field_errors", JSONArray().put("Error leyendo respuesta"))
            )
        } catch (e: NullPointerException) {
            logger.severe(e.message)
            return when (error) {
                is TimeoutError -> ApiError(
                    statusCode = 500,
                    message = e.message,
                    data = JSONObject()
                        .put("non_field_errors", JSONArray().put("Tiempo de espera superado"))
                )
                else -> ApiError(
                    statusCode = 500,
                    message = error.message,
                    data = JSONObject().put("non_field_errors", JSONArray().put(error.message))
                )
            }
        }
    }

}