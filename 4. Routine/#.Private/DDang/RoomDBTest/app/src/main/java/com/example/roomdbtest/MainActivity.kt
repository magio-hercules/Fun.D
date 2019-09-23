package com.example.roomdbtest

import android.content.Intent
import android.os.AsyncTask
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

    public fun selectDB() {
        personList = personDb?.personDao()?.getAll()!!
        mAdapter.setItems(personList)
        mAdapter.notifyDataSetChanged()
    }

    public class PersonDeleteAsync(var personDb : PersonDB?) : AsyncTask<Person, Void, Void?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg person: Person?): Void? {
            personDb?.personDao()?.delete(person[0]?.id!!.toLong())
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }
    }

    override fun onItemClick(person: Person, position: Int) {
        Toast.makeText(this, "${person.id} / ${person.name} / ${person.age} / ${person.sex}", Toast.LENGTH_SHORT).show()
        Log.d("test","onItemClick Thread ${Thread.currentThread().name}" )
//        personDb?.personDao()?.delete(person.id!!.toLong())
        var personDeleteAsync : PersonDeleteAsync
        personDeleteAsync = PersonDeleteAsync(personDb)
        personDeleteAsync.execute(person)
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
                mAdapter.setOnItemClickListener(this)
                selectDB()
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
