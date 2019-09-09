package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdb.DataBase.CatDB

class MainActivity : AppCompatActivity() {
    private var catDb: CatDB? = null

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
