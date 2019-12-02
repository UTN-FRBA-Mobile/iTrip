package com.android.itrip.activities

import android.app.Activity
import android.view.WindowManager

/**
 * Enable/Disable elements in the screen.
 * For instance, don't let user do anything while content is loading
 */
object AppWindowManager {

    fun disableScreen(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun enableScreen(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}