package com.fundroid.routinesc

import androidx.room.*

@Dao
interface AlarmMasterDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAlarm(alarm: AlarmMaster)

    @Query("SELECT * FROM AlarmMaster")
    fun findAll(): List<AlarmMaster>

    @Update
    fun updateAlarm(alarm: AlarmMaster)

    @Delete
    fun deleteAlarm(alarm: AlarmMaster)

}