package com.android.itrip.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "actividadCategoriaJoin_table",
    primaryKeys = ["actividadId", "categoriaId"],
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE, entity = Actividad::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"]
        ),
        ForeignKey(
            onDelete = CASCADE, entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"]
        )
    ]
)
data class ActividadCategoria(
    val actividadId: Long,
    val categoriaId: Long
)