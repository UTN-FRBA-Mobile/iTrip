package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.itrip.util.Converters
import java.io.Serializable


@TypeConverters(Converters::class)
@Entity(tableName = "ciudad_a_visitar_table")
data class CiudadAVisitar(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var ciudad: Ciudad,

    @ColumnInfo(name = "inicio")
    var inicio: String = "",

    @ColumnInfo(name = "fin")
    var fin: String = ""
) : Serializable