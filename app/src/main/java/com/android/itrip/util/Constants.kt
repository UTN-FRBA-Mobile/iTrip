package com.android.itrip.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.itrip.R
import com.android.itrip.models.CiudadAVisitar
import org.json.JSONObject
import java.util.*

interface ActivityType {
    companion object {
        const val EMPTY = 0
        const val ACTIVITY = 1
    }
}

object Constants {
    const val DIALOG_TITLE_EC = R.string.quiz_info_dialog_title_estadocivil
    const val DIALOG_TITLE_EST = R.string.quiz_info_dialog_title_estudios
    const val DIALOG_TITLE_OCU = R.string.quiz_info_dialog_title_ocupacion
    const val DIALOG_TITLE_GEN = R.string.quiz_info_dialog_title_genero
}

data class ViajeData(val nombre: String, var inicio: String, var fin: String)

data class ApiError(val statusCode: Int, val message: String? = null, val data: JSONObject)


object CiudadAVisitarObject {
    var ciudadAVisitar: CiudadAVisitar? = null
    var _date = MutableLiveData<Calendar>()
    val date: LiveData<Calendar>
        get() = _date
}

interface DrawerLocker {
    fun setDrawerEnabled(enabled: Boolean)
}

interface RequestCodes {
    companion object {
        const val ADD_ACTIVITY_CODE = 106
        const val VIEW_ACTIVITY_DETAILS_CODE = 110
        const val VIEW_ACTIVITY_LIST_CODE = 114
    }
}