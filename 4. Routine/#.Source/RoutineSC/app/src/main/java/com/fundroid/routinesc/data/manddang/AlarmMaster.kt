package com.fundroid.routinesc.data.manddang

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmMaster constructor(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "index") val index: Long,
                               @ColumnInfo(name = "categoryId") val categoryId: Long,
                               @ColumnInfo(name = "routineId") val routineId: Long,
                               @ColumnInfo(name = "alarmId") val alarmId: Long,
                               @ColumnInfo(name = "days") val days: Long,
                               @ColumnInfo(name = "alarmTime") val alarmTime: Long,
                               @ColumnInfo(name = "useFg") val useFg: Boolean)