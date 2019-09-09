package com.example.kimyoojin.DATABASE.Dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* 루틴 코드 관리(Main Routine Code Manager for Fundroid Project) */
@Entity(tableName = "middle")
class MIDDLE_DTO(
    @PrimaryKey @ColumnInfo(name = "midid1") var categoryid: String,
    @ColumnInfo(name = "midid2") var routineid: String,
    @ColumnInfo(name = "midname") var codename: String,
    @ColumnInfo(name = "midcomment") var comment: String
)