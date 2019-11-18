package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.itrip.models.Ciudad

@Dao
interface CiudadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ciudad: Ciudad)

    @Query("DELETE FROM ciudad_table")
    fun clear()

    @Query("SELECT * FROM ciudad_table ORDER BY nombre ASC")
    fun getAll(): LiveData<List<Ciudad>>

}

