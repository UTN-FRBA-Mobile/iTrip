package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.volley.VolleyError
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import org.json.JSONObject
import java.util.logging.Logger


object AuthenticationService : Service() {

    private var refreshToken: String = ""
    var accessToken: String = ""
    const val base_api_url = "https://proyecto.brazilsouth.cloudapp.azure.com/rest-api/"
    private val logger = Logger.getLogger(this::class.java.name)


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun validateFirebaseToken(
        paramToken: String,
        responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("validateFirebaseToken.")
        val url = "firebase/"
        val json = JSONObject().put("token", paramToken)
        logger.info(json.toString())
        ApiService.post(url, json, {
            refreshToken = it.getString("refresh")
            accessToken = it.getString("access")
            responseHandler()
        }, errorHandler)
    }

    fun verifyUser(
        auth: FirebaseUser?, responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("Calling verifying user: " + auth?.displayName)
        auth!!.getIdToken(true)
            .addOnCompleteListener { task ->
                onVerifyUserComplete(task, responseHandler, errorHandler)
            }
    }

    private fun onVerifyUserComplete(
        task: Task<GetTokenResult>, responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("Calling onVerifyUserComplete.")
        if (task.isSuccessful)
            validateFirebaseToken(
                task.result?.token!!, responseHandler, errorHandler
            )
        else logger.info("Error verifying user: " + task.exception)
    }
}