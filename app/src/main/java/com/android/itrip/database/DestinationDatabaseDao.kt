package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DestinationDatabaseDao {

    @Insert
    fun insert(destination: Destination)

    @Query("DELETE FROM destination_table")
    fun clear()

    @Query("SELECT * FROM destination_table ORDER BY name ASC")
    fun getAll(): LiveData<List<Destination>>

}

