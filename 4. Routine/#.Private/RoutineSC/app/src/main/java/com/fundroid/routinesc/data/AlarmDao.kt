package com.fundroid.routinesc.data

import androidx.room.*
import com.fundroid.routinesc.data.Alarm

@Dao
interface AlarmDao {
    @Insert
    fun createAlarm(alarm: Alarm)

    @Query("SELECT * FROM Alarm WHERE routine_id =:routineId")
    fun getAlarms(routineId:Int): List<Alarm>

}