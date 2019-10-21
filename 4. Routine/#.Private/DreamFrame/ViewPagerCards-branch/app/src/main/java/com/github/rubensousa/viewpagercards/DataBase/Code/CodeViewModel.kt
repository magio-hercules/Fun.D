package com.github.rubensousa.viewpagercards.DataBase.Code

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CodeViewModel (application: Application): AndroidViewModel(application){

    private val coderepository: CodeRepository

    val allCodes: LiveData<List<CodeDTO>>

    init {
        val codeDAO = CodeRDB.getDatabase(application, viewModelScope).codeDAO()
        coderepository = CodeRepository(codeDAO)
        allCodes = coderepository.allCodes
    }

    fun insertCode(codeDTO: CodeDTO) = viewModelScope.launch {
        coderepository.insert(codeDTO)
    }

    fun deleteAll() = viewModelScope.launch {
        coderepository.deleteAll()
    }

}