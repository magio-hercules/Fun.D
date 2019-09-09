package com.example.kimyoojin.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kimyoojin.DATABASE.Dto.LARGE_DTO


@Dao
interface LARGE_DAO {
    @Query("SELECT * FROM large WHERE bigid like '%:id%'")
    fun SelectLarge(id: String): Array<LARGE_DTO>

    abstract fun itemById(i: Int): Any
}
