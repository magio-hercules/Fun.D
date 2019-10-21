package com.fundroid.routinesc.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Routine(
    @ColumnInfo(name = "top_comment") var topComment: String,
    @ColumnInfo(name = "cover_message") var coverMessage: String,
    @ColumnInfo(name = "bottom_comment") var bottomComment: String,
    @ColumnInfo(name = "detail_message") var detailMessage: String,
    @ColumnInfo(name = "type") var type: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}