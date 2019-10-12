package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.itrip.util.VolleyController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import org.json.JSONObject
import java.util.logging.Logger


object AuthenticationService : Service() {

    private lateinit var refreshToken: String
    private var _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String>
        get() = _accessToken
    private var tokenValid: Boolean = false
    private val apiLogin = JSONObject("""{"username":"admin", "password":"SlaidTeam123"}""")
    const val base_api_url = "https://proyecto.brazilsouth.cloudapp.azure.com/rest-api/"
    private val logger = Logger.getLogger("PRUEBA TOKEN")

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        _accessToken.value = ""
        logger.info("init Event")
    }

    /* region Deprecated */
    private fun validateToken(): JsonObjectRequest {
        val url = base_api_url + "token/verify"

        val params = HashMap<String, String>()
        params["token"] = accessToken.value!!

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
        val url = base_api_url + "token/verify"

        val params = HashMap<String, String>()
        params["token"] = accessToken.value!!

        queue.addToRequestQueue(
            JsonObjectRequest(
                Request.Method.POST, url, JSONObject(params),
                Response.Listener { response ->
                    tokenValid = true
                    logger.info("Response: %s".format(response.toString()))
                },
                Response.ErrorListener { response ->
                    if (response.networkResponse.statusCode == 401) {
                        logger.info("Invalid Token")
                        tokenValid = false
                        val url = base_api_url + "token/refresh"

                        val params = HashMap<String, String>()
                        params["refresh"] =
                            refreshToken

                        queue.addToRequestQueue(
                            JsonObjectRequest(
                                Request.Method.POST, url, JSONObject(params),
                                Response.Listener { response ->
                                    logger.info("Response: %s".format(response.toString()))
                                    _accessToken.value = response.getString("access")
                                    tokenValid = true
                                },
                                Response.ErrorListener { response -> logger.info("That didn't work when calling token/refresh! Status Code: " + response.message) })
                        )

                    } else {
                        logger.info("That didn't work when calling token/verify! Status Code: " + response.networkResponse.statusCode)
                    }
                })
        )
        return tokenValid
    }

    fun requestToken(auth: FirebaseAuth): JsonObjectRequest {
        val url = base_api_url + "token/"

        return JsonObjectRequest(
            Request.Method.POST, url, apiLogin,
            Response.Listener { response ->
                logger.info("Response: %s".format(response.toString()))
                refreshToken = response.getString("refresh")
                _accessToken.value = response.getString("access")
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
                _accessToken.value = response.getString("access")
            },
            Response.ErrorListener { response -> logger.info("That didn't work! " + response.message) })
    }
    /* endregion */

    fun validateFirebaseToken(token: String): JsonObjectRequest {
        logger.info("validateFirebaseToken Called, token: $token")

        val url = base_api_url + "firebase/"

        val params = HashMap<String, String>()
        params["token"] = token

        return JsonObjectRequest(
            Request.Method.POST, url, JSONObject(params),
            Response.Listener { response ->
                refreshToken = response.getString("refresh")
                _accessToken.value = response.getString("access")
                logger.info("Response: %s".format(response.toString()))
                logger.info("User verified: $refreshToken")
            },
            Response.ErrorListener { response ->
                logger.info("That didn't work! Status Code: " + response.networkResponse.statusCode)
            })
    }

    fun verifyUser(auth: FirebaseUser?, queue: VolleyController) {
        logger.info("Calling verifying user: " + auth?.displayName)

        auth!!.getIdToken(true)
            .addOnCompleteListener { task ->
                onVerifyUserComplete(task, queue)
            }
    }

    private fun onVerifyUserComplete(task: Task<GetTokenResult>, queue: VolleyController) {
        logger.info("Calling onVerifyUserComplete.")
        if (task.isSuccessful) queue.addToRequestQueue(
            validateFirebaseToken(
                task.result?.token!!
            )
        )
        else logger.info("Error verifying user: " + task.exception)
    }
}