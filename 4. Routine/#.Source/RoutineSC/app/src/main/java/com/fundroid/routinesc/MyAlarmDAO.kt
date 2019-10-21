package com.fundroid.routinesc

import androidx.room.*

@Dao
interface MyAlarmDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: MyAlarm)

    @Query("SELECT * FROM MyAlarm")
    fun findAll(): List<MyAlarm>

    @Update
    fun updateUser(user: MyAlarm)

    @Delete
    fun deleteUser(user: MyAlarm)

}