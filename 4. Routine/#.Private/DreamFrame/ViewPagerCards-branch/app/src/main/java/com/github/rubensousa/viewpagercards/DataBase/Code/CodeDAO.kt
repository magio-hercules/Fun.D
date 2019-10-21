package com.github.rubensousa.viewpagercards.DataBase.Code

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CodeDAO{
    @Query("SELECT * FROM code")
    fun getCodeInfo(): LiveData<List<CodeDTO>>
//    @Query("SELECT * FROM code WHERE categoryid = :categoryid AND codeid = :codeid")
//    fun getCodeInfo(categoryid: String, codeid: String): LiveData<List<CodeDTO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCode(codeDTO: CodeDTO)

    @Query("DELETE FROM code")
    suspend fun deleteAll()
}