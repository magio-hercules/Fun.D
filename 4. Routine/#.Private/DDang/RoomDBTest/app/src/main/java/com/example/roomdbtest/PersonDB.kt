package com.example.roomdbtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class PersonDB: RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object {
        private var INSTANCE: PersonDB? = null

        fun getInstance(context: Context): PersonDB? {
            if (INSTANCE == null) {
                synchronized(PersonDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PersonDB::class.java, "person.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}