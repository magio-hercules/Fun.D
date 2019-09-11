package com.example.roomdbtest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdb.DataBase.CatDB
import com.example.roomdb.DataBase.CatDTO
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private var catDb : CatDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        catDb = CatDB.getInstance(this)

        /* 새로운 cat 객체를 생성, id 이외의 값을 저장 후 DB에 추가 */
        val addRunnable = Runnable {
            val newCat = CatDTO()
            newCat.catName = "testName"     // addName.text.toString()
            newCat.lifeSpan = 1             // addLifeSpan.text.toString().toInt()
            newCat.origin = "testOtigin"    // addOrigin.text.toString()
            catDb?.catDao()?.insert(newCat)
        }

        addBtn.setOnClickListener {
            val addThread = Thread(addRunnable)
            addThread.start()

            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onDestroy() {
        CatDB.destroyInstance()
        super.onDestroy()
    }
}
