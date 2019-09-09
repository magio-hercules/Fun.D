package com.example.roomdb.DataBase

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

interface CatDAO {
    @Query("SELECT * FROM cat")
    fun getAll(): List<CatDTO>

    @Insert(onConflict = REPLACE)
    fun insert(cat: CatDTO)

    @Query("DELETE FROM cat")
    fun deleteAll()
}