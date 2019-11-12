package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.itrip.models.Ciudad

@Dao
interface CiudadDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ciudad: Ciudad)

    @Query("SELECT * from ciudad_table WHERE id = :key")
    fun get(key: Long): Ciudad?

    @Query("DELETE FROM ciudad_table")
    fun clear()

    @Query("SELECT * FROM ciudad_table ORDER BY nombre ASC")
    fun getAll(): LiveData<List<Ciudad>>

}

