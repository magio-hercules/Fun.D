package com.fundroid.routinesc.data.manddang

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * codeLabel : category, routine, alarm, comment
 * codeName : 부자, 가수, 요리사...
 */

@Entity
data class CodeManager constructor(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "codeId") val codeId: Long,
                                   @ColumnInfo(name = "codeLabel") val codeLabel: String,
                                   @ColumnInfo(name = "codeName") val codeName: String,
                                   @ColumnInfo(name = "comment") val comment: String,
                                   @ColumnInfo(name = "categoryId") val categoryId: Long,
                                   @ColumnInfo(name = "routineId") val routineId: Long,
                                   @ColumnInfo(name = "alarmId") val alarmId: Long)