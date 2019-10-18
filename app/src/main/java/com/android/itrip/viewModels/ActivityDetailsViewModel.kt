package com.android.itrip.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.itrip.database.ActivityDatabaseDao


class ActivityDetailsViewModel(
    val database: ActivityDatabaseDao,
    application: Application
) : AndroidViewModel(application) {}