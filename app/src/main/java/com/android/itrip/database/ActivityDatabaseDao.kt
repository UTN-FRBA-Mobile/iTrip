package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.itrip.models.Actividad

@Dao
interface ActivityDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(actividad: Actividad)

    @Update
    fun update(actividad: Actividad)

    @Query("SELECT * from actividades_table WHERE id = :key")
    fun get(key: Long): Actividad?

    @Query("DELETE FROM actividades_table")
    fun clear()

    @Query("SELECT * FROM actividades_table ORDER BY nombre DESC")
    fun getAll(): LiveData<List<Actividad>>

    @Query("SELECT * FROM actividades_table WHERE nombre LIKE :query ORDER BY nombre ASC")
    fun getActividadByNombre(query: String?): LiveData<List<Actividad>>

}

