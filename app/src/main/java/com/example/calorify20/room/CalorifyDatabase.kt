package com.example.calorify20.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [InOutTake::class], version = 4, exportSchema = false)
abstract class CalorifyDatabase :RoomDatabase() {
    abstract fun InOutTakeDao() : InOutTakeDao?

    companion object {
        @Volatile
        private var INSTANCE : CalorifyDatabase? = null
        fun getDatabase(context: Context) : CalorifyDatabase? {
            if (INSTANCE == null) {
                synchronized(CalorifyDatabase::class.java) {
                    INSTANCE = databaseBuilder(context.applicationContext, CalorifyDatabase::class.java, "Calorify_Database")
                        .build()
                }
            }
        return INSTANCE
        }
    }
}