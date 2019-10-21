package com.github.rubensousa.viewpagercards.DataBase.Code

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CodeDTO::class], version = 1)
abstract class CodeRDB: RoomDatabase(){

    abstract fun codeDAO(): CodeDAO

    companion object{
        @Volatile
        private var INSTANCE: CodeRDB? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): CodeRDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CodeRDB::class.java,
                        "code_database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(CodeRDBCallback(scope))
                        .build()
                INSTANCE = instance
                instance
            }
        }

        private class CodeRDBCallback(
                private val scope: CoroutineScope
        ): RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.codeDAO())
                    }
                }
            }
        }

        suspend fun populateDatabase(codeDAO: CodeDAO){
            codeDAO.deleteAll()

            var code: CodeDTO? = null

            code = CodeDTO("A", "A1", "만수르", "부러운새끼1", "", "", "")
            codeDAO.insertCode(code)

            code = CodeDTO("A", "A2", "만수르아들1", "부러운새끼2", "", "", "")
            codeDAO.insertCode(code)

            code = CodeDTO("A", "A3", "만수르아들2", "부러운새끼3", "", "", "")
            codeDAO.insertCode(code)
        }
    }
}