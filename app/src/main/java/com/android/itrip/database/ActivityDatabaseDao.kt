/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.itrip.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.itrip.models.Actividad

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface ActivityDatabaseDao {

    @Insert
    fun insert(actividad: Actividad)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param destination new value to write
     */
    @Update
    fun update(actividad: Actividad)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key to match
     */
    @Query("SELECT * from actividades_table WHERE id = :key")
    fun get(key: Long): Actividad?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM actividades_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by name in descending order.
     */
    @Query("SELECT * FROM actividades_table ORDER BY nombre DESC")
    fun getAll(): LiveData<List<Actividad>>

    /**
     * Selects and returns the matched destinations.
     */
    @Query("SELECT * FROM actividades_table WHERE nombre LIKE :query ORDER BY nombre ASC")
    fun getActividadByNombre(query: String?): LiveData<List<Actividad>>

}
