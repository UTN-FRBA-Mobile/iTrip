package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "categories_table", indices = [Index("id")])
data class Categoria(
    @PrimaryKey
    var id: Long = 0L,
    @ColumnInfo(name = "nombre")
    val nombre: String = "",
    @ColumnInfo(name = "descripcion")
    val descripcion: String = ""
) : Serializable