package com.example.roomdbtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var personDb: PersonDB? = null
    private var personList = listOf<Person>()
    lateinit var mAdapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        personDb = PersonDB.getInstance(this)
        mAdapter = PersonAdapter(this, personList)

        val r = Runnable {
            try {
                personList = personDb?.personDao()?.getAll()!!
                mAdapter.notifyDataSetChanged()

                RoomRecyclerView.adapter = mAdapter
                RoomRecyclerView.layoutManager = LinearLayoutManager(this)
                RoomRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        addButton.setOnClickListener {
            val i = Intent(this, AddPerson::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onDestroy() {
        PersonDB.destroyInstance()
        personDb = null
        super.onDestroy()
    }
}
