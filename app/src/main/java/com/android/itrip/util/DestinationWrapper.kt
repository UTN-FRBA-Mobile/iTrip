package com.android.itrip.util

import com.android.itrip.database.Destination
import java.io.Serializable

class DestinationWrapper : Serializable {


    constructor(destination: Destination) {
        this.destination = destination
    }

    val destination: Destination
    // GETTERS AND SETTERS
}