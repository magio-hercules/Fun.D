package com.example.kimyoojin.DATABASE.DB

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.kimyoojin.DATABASE.Dto.LARGE_DTO
import com.example.kimyoojin.Dao.LARGE_DAO


@Database(entities = [LARGE_DTO::class], version = 1)
abstract class LargeDB : RoomDatabase() {
    abstract fun large_dao(): LARGE_DAO
}
