package com.android.itrip.services

import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.android.itrip.models.Quiz
import com.google.gson.Gson
import org.json.JSONObject
import javax.inject.Inject


class QuizService @Inject constructor(context: Context): ApiService(context) {

    private val gson = Gson()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    fun getResolution(responseHandler: (Boolean) -> Unit, errorHandler: (ApiError) -> Unit) {
        val url = "questions/verify"
        get(url, {
            responseHandler(it.getBoolean("respondidas"))
        }, errorHandler)
    }

    fun postAnswers(quiz: Quiz, responseHandler: () -> Unit, errorHandler: (ApiError) -> Unit) {
        val url = "questions/"
        val json = JSONObject(gson.toJson(quiz.toQuizApiModel()))
        post(url, json, {
            responseHandler()
        }, errorHandler)
    }

}