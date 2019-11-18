package com.android.itrip.util

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
import com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import javax.inject.Inject
import javax.inject.Named

class VolleyClient @Inject constructor(@Named("ApplicationContext") context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    fun <T> addToRequestQueue(
        request: Request<T>,
        initialTimeoutMs: Int? = null,
        retries: Int? = null
    ) {
        request.retryPolicy = DefaultRetryPolicy(
            initialTimeoutMs ?: 2000,
            retries ?: DEFAULT_MAX_RETRIES,
            DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }

}