package com.fundroid.routinesc.data

import android.content.Context
import android.content.SharedPreferences

class RoutineSharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val testValue : String = "testValue"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var test: String
        get() = prefs.getString(testValue, "").toString()
        set(value) = prefs.edit().putString(testValue, value).apply()

    var isInitialized : Boolean
        get() = prefs.getBoolean("isInitialized", false)
        set(value) = prefs.edit().putBoolean("isInitialized", value).apply()
}