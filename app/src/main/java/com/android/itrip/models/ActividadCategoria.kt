package com.android.itrip.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "actividadCategoriaJoin_table",
    primaryKeys = ["actividadId", "categoriaId"],
    foreignKeys = [
        ForeignKey(entity = Actividad::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"]),
        ForeignKey(entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"])
    ])
data class ActividadCategoria(
    val actividadId: Long,
    val categoriaId: Long
)