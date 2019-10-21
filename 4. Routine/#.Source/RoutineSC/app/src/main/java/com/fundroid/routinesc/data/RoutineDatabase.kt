package com.fundroid.routinesc.data

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.fundroid.routinesc.data.manddang.*

@Database(
    entities = arrayOf(
//        MyAlarm::class,
//        AlarmMaster::class,
//        CodeManager::class,

        MyAlarm::class,
        Routine::class,
        Alarm::class,
        Notification::class
    ), version = 1
)
@TypeConverters(DateConverter::class)
abstract class RoutineDatabase : RoomDatabase() {
//    abstract fun codeManagerDao(): CodeManagerDao
//    abstract fun alarmMasterDao(): AlarmMasterDAO
//    abstract fun myAlarmDao(): MyAlarmDAO

    abstract fun myAlarmDao(): MyAlarmDao
    abstract fun routineDao(): RoutineDao
    abstract fun alarmDao(): AlarmDao
    abstract fun notificationDao(): NotificationDao

}