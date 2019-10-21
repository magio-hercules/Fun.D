package com.fundroid.routinesc.data

import androidx.room.*

@Dao
interface MyAlarmDao {
    @Query("DELETE FROM MyAlarm")
    fun nukeTable()

    @Insert
    fun createMyAlarm(alarm: MyAlarm)

    @Query("SELECT * FROM MyAlarm")
    fun getMyAlarms(): List<MyAlarm>

}