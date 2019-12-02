package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.itrip.models.Actividad

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(actividad: Actividad)

    @Query("SELECT * from actividades_table WHERE ciudadId = :cityId")
    fun getActivitiesOfCity(cityId: Long): List<Actividad>

    @Query("SELECT * from actividades_table WHERE ciudadId = :cityId")
    fun getActivitiesOfCity2(cityId: Long): LiveData<List<Actividad>>

    @Query("DELETE FROM actividades_table")
    fun clear()

    @Query("SELECT * FROM actividades_table WHERE ciudadId = :ciudadId AND nombre LIKE :query ORDER BY nombre ASC")
    fun getActividadByNombre(query: String?, ciudadId: Long?): LiveData<List<Actividad>>

}

