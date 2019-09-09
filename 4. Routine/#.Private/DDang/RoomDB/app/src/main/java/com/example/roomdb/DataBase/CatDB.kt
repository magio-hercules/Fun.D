package com.example.roomdb.DataBase

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class CatDB : RoomDatabase() {
    abstract fun catDao(): CatDAO

    companion object{
        private var INSTANCE: CatDB? = null

        fun getInstance(context: Context): CatDB?{
            if (INSTANCE == null){
                synchronized(CatDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, CatDB::class.java, "cat.db")
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