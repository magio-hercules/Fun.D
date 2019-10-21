package com.fundroid.routinesc.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(foreignKeys = [ForeignKey(entity = Routine::class, parentColumns = ["id"], childColumns = ["routine_id"])])
data class MyAlarm (
    @ColumnInfo(name = "time") var localTime: LocalTime,
    @ColumnInfo(name = "comment") var comment: String,
    @ColumnInfo(name = "is_use") var isUse: Boolean,
    @ColumnInfo(name = "routine_type") var routineType: String,
    @ColumnInfo(name = "routine_id") var routineId: Int
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
}