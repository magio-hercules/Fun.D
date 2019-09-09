package com.example.roomdbtest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdb.DataBase.CatDB

class MainActivity(var catDb: CatDB?) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catDb = CatDB.getInstance(this)

        val r = Runnable {

        }

        val thread = Thread(r)
        thread.start()
    }
}
