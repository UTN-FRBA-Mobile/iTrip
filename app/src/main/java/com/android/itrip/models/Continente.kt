package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "continente_table")
data class Continente(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "nombre")
    val nombre: String = "",

    @ColumnInfo(name = "descripcion")
    val descripcion: String = "",

//    @ColumnInfo(name = "ciudades")
    val paises: List<Pais> = emptyList()

) : Serializable