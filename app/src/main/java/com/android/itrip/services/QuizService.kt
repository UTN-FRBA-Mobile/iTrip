package com.android.itrip.services

import android.content.Context
import com.android.itrip.models.Quiz
import org.json.JSONObject
import javax.inject.Inject


class QuizService @Inject constructor(context: Context) : ApiService(context) {

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