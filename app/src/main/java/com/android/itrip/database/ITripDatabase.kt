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
    version = 3,
    exportSchema = false
)
abstract class ITripDatabase : RoomDatabase() {
    abstract val actividadCategoriaDao: ActividadCategoriaDao
    abstract val categoriaDao: CategoriaDao
    abstract val actividadDao: ActividadDao
    abstract val ciudadDao: CiudadDao

    companion object {
        @Volatile
        private var INSTANCE: ITripDatabase? = null

        fun getInstance(context: Context): ITripDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ITripDatabase::class.java,
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
