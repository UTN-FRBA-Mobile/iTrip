package com.android.itrip.services

import com.android.itrip.util.ApiError
import com.android.itrip.util.VolleyClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class AuthenticationService @Inject constructor(queue: VolleyClient) : ApiService(queue) {

    companion object {
        var refreshToken = ""
            private set
        var accessToken = ""
            private set
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