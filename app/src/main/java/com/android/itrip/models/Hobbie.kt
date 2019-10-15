package com.android.itrip.models

import java.io.Serializable

data class Hobbie(
    val key: String = "",
    val value: String = "",
    var choosed: Boolean = false
) : Serializable