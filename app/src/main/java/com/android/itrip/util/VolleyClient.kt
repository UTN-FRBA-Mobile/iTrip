package com.android.itrip.util

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
import com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(context.applicationContext) }

    fun <T> addToRequestQueue(request: Request<T>, initialTimeoutMs: Int? = null) {
        request.retryPolicy =
            DefaultRetryPolicy(initialTimeoutMs ?: 2000, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT)
        requestQueue.add(request)
    }

    fun cancelAll() {
        requestQueue.cancelAll { true }
    }

}