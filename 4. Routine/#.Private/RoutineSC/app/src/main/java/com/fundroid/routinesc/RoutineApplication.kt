package com.fundroid.routinesc

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.facebook.stetho.Stetho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineApplication : Application() {

    val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        lateinit var database: RoutineDatabase
        lateinit var prefs: RoutineSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("lsc", "RoutineApplication onCreate")
        Stetho.initializeWithDefaults(this)
        database = Room.databaseBuilder(this, RoutineDatabase::class.java, "ROUTINE_DB").build()
        prefs = RoutineSharedPreferences(applicationContext)

        if (!prefs.isInitialized) {
            dataInitialize()
            prefs.isInitialized = true
        }

    }

    fun dataInitialize() {
        scope.launch {
            database.codeManagerDao()
                .createCode(CodeManager(0, "category", "가수", "너두 수지처럼\n 당당한 집순이 루틴", 0, 0, 0))
            database.alarmMasterDao().createAlarm(AlarmMaster(0, 0, 0, 0, 0, 0, false))
            database.alarmMasterDao().createAlarm(AlarmMaster(1, 0, 0, 0, 0, 1, false))
            database.alarmMasterDao().createAlarm(AlarmMaster(2, 0, 0, 0, 0, 2, false))
            database.alarmMasterDao().createAlarm(AlarmMaster(3, 0, 0, 0, 0, 3, false))
        }

    }
}