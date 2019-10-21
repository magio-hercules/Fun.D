package com.fundroid.routinesc.data.manddang

import androidx.room.*

@Dao
interface CodeManagerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createCode(code: CodeManager)

    @Query("SELECT * FROM CodeManager")
    fun findAll(): List<CodeManager>

    @Update
    fun updateCode(code: CodeManager)

    @Delete
    fun deleteCode(code: CodeManager)

}