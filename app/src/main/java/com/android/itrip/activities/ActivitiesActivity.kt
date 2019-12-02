package com.android.itrip.activities

import android.os.Bundle


class ActivitiesActivity : ActivitiesBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readSource()
        bindings()
        initDrawer()
        initNavigation()
        startActivitiesList()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }
}