package com.android.itrip.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.itrip.models.Actividad


@Database(entities = [Actividad::class], version = 1, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase() {
    abstract val activityDatabaseDao: ActivityDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityDatabase? = null

        fun getInstance(context: Context): ActivityDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ActivityDatabase::class.java,
                        "activity_database"
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
