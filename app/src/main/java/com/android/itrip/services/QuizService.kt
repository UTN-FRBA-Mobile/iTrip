package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.models.Quiz
import com.android.volley.VolleyError
import com.google.gson.Gson
import org.json.JSONObject
import java.util.logging.Logger


object QuizService : Service() {

    private val logger = Logger.getLogger(this::class.java.name)
    private val gson = Gson()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    fun getResolution(
        responseHandler: (Boolean) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        val url = "questions/verify"
        ApiService.get(url, {
            responseHandler(it.getBoolean("respondidas"))
        }, errorHandler)
    }

    fun postAnswers(
        quiz: Quiz,
        responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        val url = "questions/"
        logger.info(gson.toJson(quiz))
        val json = JSONObject(gson.toJson(quiz))
        ApiService.post(url, json, {
            responseHandler()
        }, errorHandler)
    }

}