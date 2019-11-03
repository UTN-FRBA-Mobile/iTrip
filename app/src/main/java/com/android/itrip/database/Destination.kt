package com.android.itrip.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.itrip.util.Converters
import java.io.Serializable
import java.util.*

@TypeConverters(Converters::class)
@Entity(tableName = "destination_table")
data class Destination(
    @PrimaryKey(autoGenerate = true)
    var destinationId: Long = 0L,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "latitude")
    val latitude: Double? = 0.0,
    @ColumnInfo(name = "longitude")
    var longitude: Double? = 0.0,
    @ColumnInfo(name = "startDate")
    var startDate: Date? = Date(),
    @ColumnInfo(name = "endDate")
    var endDate: Date? = Date(),
    @ColumnInfo(name = "imagen")
    val picture: String?
) : Serializable