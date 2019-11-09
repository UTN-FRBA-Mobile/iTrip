package com.android.itrip.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.itrip.models.Quiz
import com.android.itrip.models.QuizApiModel
import com.google.gson.Gson
import org.json.JSONObject


object QuizService : Service() {

    private val gson = Gson()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    fun getResolution(responseHandler: (Boolean) -> Unit, errorHandler: (ApiError) -> Unit) {
        val url = "questions/verify"
        ApiService.get(url, {
            responseHandler(it.getBoolean("respondidas"))
        }, errorHandler)
    }

    fun postAnswers(quiz: Quiz, responseHandler: () -> Unit, errorHandler: (ApiError) -> Unit) {
        val url = "questions/"
        val json = JSONObject(gson.toJson(quiz.toQuizApiModel()))
        ApiService.post(url, json, {
            responseHandler()
        }, errorHandler)
    }

}