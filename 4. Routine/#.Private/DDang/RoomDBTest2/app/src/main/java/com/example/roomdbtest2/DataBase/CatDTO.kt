package com.example.roomdb.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat")
class CatDTO (
    @PrimaryKey @ColumnInfo(name = "id") var ID: Long,
    @ColumnInfo(name = "catname") var catName: String?,
    @ColumnInfo(name = "lifespan") var lifeSpan: Int,
    @ColumnInfo(name = "origin") var origin: String
){
    constructor(): this(1, "name", 1, "1")
}