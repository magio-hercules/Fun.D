package com.example.roomdbtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PersonAdapter.OnItemClickListener<Person> {

    private var personDb: PersonDB? = null
    private var personList = listOf<Person>()
    lateinit var mAdapter: PersonAdapter

    override fun onItemClick(person: Person, position: Int) {
        Toast.makeText(this, "${person.id} / ${person.name} / ${person.age} / ${person.sex}", Toast.LENGTH_SHORT).show()
//        personDb?.personDao()?.delete(person.id!!.toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        personDb = PersonDB.getInstance(this)
        mAdapter = PersonAdapter(this, personList) {
//            Log.d("tag", "item Click")
        }

        val r = Runnable {
            try {
                personList = personDb?.personDao()?.getAll()!!
                mAdapter.setItems(personList)
                mAdapter.setOnItemClickListener(this)
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
