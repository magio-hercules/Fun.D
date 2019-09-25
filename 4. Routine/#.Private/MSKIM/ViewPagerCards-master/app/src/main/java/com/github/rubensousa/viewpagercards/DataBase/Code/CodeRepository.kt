package com.github.rubensousa.viewpagercards.DataBase.Code

import androidx.lifecycle.LiveData

class CodeRepository (private val codeDAO: CodeDAO){
    val allCodes: LiveData<List<CodeDTO>> = codeDAO.getCodeInfo()

    suspend fun insert(codeDTO: CodeDTO){
        codeDAO.insertCode(codeDTO)
    }

    suspend fun deleteAll(){
        codeDAO.deleteAll()
    }
}