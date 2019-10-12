package com.android.itrip.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "actividades_table")
data class Actividad(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "nombre")
    val nombre: String = "",

    @ColumnInfo(name = "descripcion")
    val descripcion: String = "",

    @ColumnInfo(name = "duracion")
    val duracion: Int = 0,

    @ColumnInfo(name = "costo")
    val costo: Int = 0,

    @ColumnInfo(name = "calificacion")
    val calificacion: String = "",

    @ColumnInfo(name = "categorias")
    val categorias: List<Categoria> = emptyList(),

    @ColumnInfo(name = "disponibilidad_lunes")
    val disponibilidad_lunes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_martes")
    val disponibilidad_martes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_miercoles")
    val disponibilidad_miercoles: Boolean = false,

    @ColumnInfo(name = "disponibilidad_jueves")
    val disponibilidad_jueves: Boolean = false,

    @ColumnInfo(name = "disponibilidad_viernes")
    val disponibilidad_viernes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_sabado")
    val disponibilidad_sabado: Boolean = false,

    @ColumnInfo(name = "disponibilidad_domingo")
    val disponibilidad_domingo: Boolean = false,

    @ColumnInfo(name = "disponibilidad_manana")
    val disponibilidad_manana: Boolean = false,

    @ColumnInfo(name = "disponibilidad_tarde")
    val disponibilidad_tarde: Boolean = false,

    @ColumnInfo(name = "imagen")
    val imagen: String = "",

    @ColumnInfo(name = "latitud")
    val latitud: Boolean = false,

    @ColumnInfo(name = "longitud")
    val longitud: Boolean = false

) : Serializable