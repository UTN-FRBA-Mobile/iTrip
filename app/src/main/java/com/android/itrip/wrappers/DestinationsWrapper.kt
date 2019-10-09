package com.android.itrip.wrappers

import com.android.itrip.database.Destination
import java.io.Serializable

class DestinationsWrapper : Serializable {


    constructor(destinations: List<Destination>) {
        this.destinations = destinations
    }

    val destinations: List<Destination>
    // GETTERS AND SETTERS
}