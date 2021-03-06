package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "ciudad_table")
data class Ciudad(
    @PrimaryKey
    var id: Long = 0L,
    @ColumnInfo(name = "nombre")
    val nombre: String = "",
    @ColumnInfo(name = "descripcion")
    val descripcion: String = "",
    @ColumnInfo(name = "imagen")
    val imagen: String = ""
) : Serializable