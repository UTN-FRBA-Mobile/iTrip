package com.android.itrip.models

import java.io.Serializable

data class MapDestination(
    var name: String,
    var latitude: Double,
    var longitude: Double
) : Serializable {
    constructor(name: String, latitude: String, longitude: String) : this(
        name,
        latitude.toDouble(),
        longitude.toDouble()
    )
}