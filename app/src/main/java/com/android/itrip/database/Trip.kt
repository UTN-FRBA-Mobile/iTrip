package com.android.itrip.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "trip_table")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    var tripId: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "startDate")
    val startDate: Date,

    @ColumnInfo(name = "endDate")
    var endDate: Date
) : Serializable