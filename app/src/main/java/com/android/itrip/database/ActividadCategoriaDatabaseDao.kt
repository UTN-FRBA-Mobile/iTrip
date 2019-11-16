package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.itrip.models.ActividadCategoria
import com.android.itrip.models.Categoria

@Dao
interface ActividadCategoriaDatabaseDao {

    @Query(
        """
        SELECT * FROM categories_table INNER JOIN actividadCategoriaJoin_table ON
        categories_table.id = actividadCategoriaJoin_table.categoriaId WHERE
        actividadCategoriaJoin_table.actividadId = :actividadId
        """
    )
    fun getCategoriasOfActividad(actividadId: Long): LiveData<List<Categoria>>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(actividadCategoria: ActividadCategoria)

    @Query("DELETE FROM actividadCategoriaJoin_table")
    fun clear()
}
