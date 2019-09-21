package com.example.roomdbtest

import androidx.room.*
import io.reactivex.annotations.NonNull

@Entity(tableName = "person")
class Person(
    @PrimaryKey(autoGenerate = true) var id: Long?,  //(autoGenerate = true)
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "sex") var sex: String)
{
    @Ignore constructor(): this(1, "", 0, "")
}