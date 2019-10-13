package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.itrip.util.Converters
import java.io.Serializable

@TypeConverters(Converters::class)
@Entity(tableName = "viaje_table")
data class Viaje(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "nombre")
    val nombre: String = "",

    @ColumnInfo(name = "inicio")
    var inicio: String = "",

    @ColumnInfo(name = "fin")
    var fin: String = "",

    @ColumnInfo(name = "imagen")
    var imagen: String? = null,

    //@ColumnInfo(name = "ciudades_a_visitar")
    val ciudades_a_visitar: List<Ciudad> = emptyList()

) : Serializable