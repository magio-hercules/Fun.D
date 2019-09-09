package com.example.kimyoojin.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kimyoojin.DATABASE.Dto.MIDDLE_DTO


@Dao
interface MIDDLE_DAO {
    @Query("SELECT * FROM middle WHERE midid1 like '%:id%'")
    fun SelectMiddle(id: String): Array<MIDDLE_DTO>

    @Query("SELECT large.bigname, middle.midname, middle.midcomment FROM large, middle WHERE large.bigid = middle.midid1 and middle.midid2 = :id")
    fun SelectLM(id: String): Array<MIDDLE_DTO>

    // 임의로 만든 클래스
    data class LM(val largename: String?, val middlename: String?)
}
