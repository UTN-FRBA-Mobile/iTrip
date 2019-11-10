package com.android.itrip.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadCategoria
import com.android.itrip.models.Categoria

@Database(
    entities = [Actividad::class, Categoria::class, ActividadCategoria::class],
    version = 5,
    exportSchema = false
)
abstract class ActividadCategoriaDatabase : RoomDatabase() {
    abstract val actividadCategoriaDatabaseDao: ActividadCategoriaDatabaseDao
    abstract val categoryDatabaseDao: CategoryDatabaseDao
    abstract val activityDatabaseDao: ActivityDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ActividadCategoriaDatabase? = null

        fun getInstance(context: Context): ActividadCategoriaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ActividadCategoriaDatabase::class.java,
                        "actividades_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
