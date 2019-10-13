package com.android.itrip.models

import androidx.room.*
import com.android.itrip.util.Converters
import java.io.Serializable

@TypeConverters(Converters::class)
@Entity(tableName = "actividades_table")
data class Actividad(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "nombre")
    var nombre: String = "",

    @ColumnInfo(name = "descripcion")
    var descripcion: String = "",

    @ColumnInfo(name = "duracion")
    var duracion: Int = 0,

    @ColumnInfo(name = "costo")
    var costo: Int = 0,

    @ColumnInfo(name = "calificacion")
    var calificacion: String = "",

    @Ignore
    var categorias: List<Categoria> = emptyList(),

    @ColumnInfo(name = "disponibilidad_lunes")
    var disponibilidad_lunes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_martes")
    var disponibilidad_martes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_miercoles")
    var disponibilidad_miercoles: Boolean = false,

    @ColumnInfo(name = "disponibilidad_jueves")
    var disponibilidad_jueves: Boolean = false,

    @ColumnInfo(name = "disponibilidad_viernes")
    var disponibilidad_viernes: Boolean = false,

    @ColumnInfo(name = "disponibilidad_sabado")
    var disponibilidad_sabado: Boolean = false,

    @ColumnInfo(name = "disponibilidad_domingo")
    var disponibilidad_domingo: Boolean = false,

    @ColumnInfo(name = "disponibilidad_manana")
    var disponibilidad_manana: Boolean = false,

    @ColumnInfo(name = "disponibilidad_tarde")
    var disponibilidad_tarde: Boolean = false,

    @ColumnInfo(name = "imagen")
    var imagen: String? = "",

    @ColumnInfo(name = "latitud")
    var latitud: String = "",

    @ColumnInfo(name = "longitud")
    var longitud: String = ""

) : Serializable