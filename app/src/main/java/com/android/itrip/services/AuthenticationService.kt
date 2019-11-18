package com.android.itrip.services

import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import org.json.JSONArray
import org.json.JSONObject
import java.util.logging.Logger
import javax.inject.Inject

class AuthenticationService @Inject constructor(context: Context) : ApiService(context) {

    companion object {
        var refreshToken = ""
        var accessToken = ""
    }

    private val logger = Logger.getLogger(this::class.java.name)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun validateFirebaseToken(
        paramToken: String,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        val url = "firebase/"
        val json = JSONObject().put("token", paramToken)
        post(url, json, {
            refreshToken = it.getString("refresh")
            accessToken = it.getString("access")
            logger.info("Access token: " + accessToken)
            logger.info(accessToken)
            responseHandler()
        }, errorHandler)
    }

    fun verifyUser(
        auth: FirebaseUser?, responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        auth!!.getIdToken(true)
            .addOnCompleteListener { task ->
                onVerifyUserComplete(task, responseHandler, errorHandler)
            }
    }

    private fun onVerifyUserComplete(
        task: Task<GetTokenResult>, responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        if (task.isSuccessful) {
            validateFirebaseToken(task.result?.token!!, responseHandler, errorHandler)
        } else {
            logger.severe("Error verifying user: " + task.exception)
            errorHandler(mapOf(task.exception))
        }
    }

    private fun mapOf(e: Exception?): ApiError {
        return ApiError(
            statusCode = 500,
            message = e?.message ?: "",
            data = JSONObject().put("non_field_errors", JSONArray().put(e?.message ?: "error"))
        )
    }

}