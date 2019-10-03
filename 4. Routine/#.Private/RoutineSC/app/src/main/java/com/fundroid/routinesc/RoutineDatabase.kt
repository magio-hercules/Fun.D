package com.fundroid.routinesc

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = arrayOf(MyAlarm::class, AlarmMaster::class, CodeManager::class), version = 1)
abstract class RoutineDatabase : RoomDatabase() {
    abstract fun codeManagerDao(): CodeManagerDao
    abstract fun alarmMasterDao(): AlarmMasterDAO
    abstract fun myAlarmDao(): MyAlarmDAO

}