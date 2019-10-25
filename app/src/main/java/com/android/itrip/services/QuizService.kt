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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getQuestionsVerify(
        responseHandler: (Boolean) -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("getQuestionsVerify.")
        ApiService.get("questions/verify", {
            responseHandler(it.getBoolean("respondidas"))
        }, errorHandler)
    }


    fun postQuestions(
        quiz: Quiz,
        responseHandler: () -> Unit,
        errorHandler: (VolleyError) -> Unit
    ) {
        logger.info("postQuestions.")
        logger.info(gson.toJson(quiz))
        ApiService.post("questions/", JSONObject(gson.toJson(quiz)), {
            responseHandler()
        }, errorHandler)
    }

}