package com.example.roomdbtest

import androidx.room.*

@Entity(tableName = "person")
class Person(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "sex") var sex: String)
{
    @Ignore constructor(): this(null, "", 0, "")
}