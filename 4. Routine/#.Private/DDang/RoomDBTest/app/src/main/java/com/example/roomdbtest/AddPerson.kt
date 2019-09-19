package com.example.roomdbtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_person.*

class AddPerson : AppCompatActivity() {

    private var personDB: PersonDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)

        personDB = PersonDB.getInstance(this)

        val addRunnable = Runnable {
            val newPerson = Person()
//            newPerson.id = addID.text.toString()
            newPerson.name = addName.text.toString()
            newPerson.age = addAge.text.toString().toInt()
            newPerson.sex = addSex.text.toString()
            personDB?.personDao()?.insert(newPerson)
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
        PersonDB.destroyInstance()
        super.onDestroy()
    }
}
