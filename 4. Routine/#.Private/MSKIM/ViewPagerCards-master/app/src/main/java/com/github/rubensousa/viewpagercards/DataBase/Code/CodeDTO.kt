package com.github.rubensousa.viewpagercards.DataBase.Code

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code")
data class CodeDTO(
        @ColumnInfo(name = "codelabel") val codelabel: String,
        @PrimaryKey @ColumnInfo(name = "codeid") val codeid: String,
        @ColumnInfo(name = "codename") var codename: String,
        @ColumnInfo(name = "comment") var comment: String,
        @ColumnInfo(name = "categoryid") var categoryid: String,
        @ColumnInfo(name = "routineid") var routineid: String,
        @ColumnInfo(name = "alarmid") var alarmid: String
)