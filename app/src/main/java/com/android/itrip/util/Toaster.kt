package com.android.itrip.util

import android.content.Context
import android.widget.Toast
import javax.inject.Inject
import javax.inject.Named

class Toaster @Inject constructor(@Named("ApplicationContext") context: Context) {

    private val toast: Toast = Toast.makeText(context,"",Toast.LENGTH_SHORT)

    fun shortToastMessage(message: String?) {
        toast.setText(message)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun longToastMessage(message: String?) {
        toast.setText(message)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

}