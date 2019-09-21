package com.example.roomdbtest

import androidx.room.*

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: Person)

    @Query("DELETE FROM person")
    fun deleteAll()

    @Delete
    fun delete(person: Person)
}