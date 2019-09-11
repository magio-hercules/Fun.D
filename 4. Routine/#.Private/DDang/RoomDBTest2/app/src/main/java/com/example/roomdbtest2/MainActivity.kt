package com.example.roomdbtest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb.DataBase.CatDB
import com.example.roomdb.DataBase.CatDTO
import com.example.roomdbtest2.DataBase.CatAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var catDb : CatDB? = null
    private var catList = listOf<CatDTO>()
    lateinit var mAdapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catDb = CatDB.getInstance(this)

        val r = Runnable {
            try {
                catList = catDb?.catDao()?.getAll()!!
                mAdapter = CatAdapter(this, catList)
                mAdapter.notifyDataSetChanged()

                mRecyclerView.adapter = mAdapter
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        mAddBtn.setOnClickListener {
            val i = Intent(this, AddActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
