package com.android.itrip.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Destination::class], version = 1, exportSchema = false)
abstract class DestinationDatabase : RoomDatabase() {

    abstract val destinationDatabaseDao: DestinationDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: DestinationDatabase? = null

        fun getInstance(context: Context): DestinationDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DestinationDatabase::class.java,
                        "destination_database"
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
