package com.example.kimyoojin.DATABASE.Dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* 카테고리 코드 관리(Main Category Code Manager for Fundroid Project) */
@Entity(tableName = "large")
class LARGE_DTO(
    @PrimaryKey @ColumnInfo(name = "bigid") var categoryid: String,
    @ColumnInfo(name = "bigname") var codename: String,
    @ColumnInfo(name = "bigcomment") var comment: Int
)