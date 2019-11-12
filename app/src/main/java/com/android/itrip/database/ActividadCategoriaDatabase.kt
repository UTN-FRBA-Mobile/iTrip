package com.android.itrip.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.itrip.models.Actividad
import com.android.itrip.models.ActividadCategoria
import com.android.itrip.models.Categoria
import com.android.itrip.models.Ciudad

@Database(
    entities = [Actividad::class, Categoria::class, ActividadCategoria::class, Ciudad::class],
    version = 2,
    exportSchema = false
)
abstract class ActividadCategoriaDatabase : RoomDatabase() {
    abstract val actividadCategoriaDatabaseDao: ActividadCategoriaDatabaseDao
    abstract val categoryDatabaseDao: CategoryDatabaseDao
    abstract val activityDatabaseDao: ActivityDatabaseDao
    abstract val ciudadDatabaseDao: CiudadDatabaseDao

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
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
