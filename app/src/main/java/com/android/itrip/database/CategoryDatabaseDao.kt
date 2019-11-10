package com.android.itrip.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.itrip.models.Categoria

@Dao
interface CategoryDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoria: Categoria)

    @Query("DELETE FROM categories_table")
    fun clear()

}