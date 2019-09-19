package com.example.roomdbtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(val context: Context, val persons: List<Person>): RecyclerView.Adapter<PersonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return persons.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(persons[position])
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
//        val idTv = itemView?.findViewById<TextView>(R.id.itemID)
        val nameTv = itemView?.findViewById<TextView>(R.id.itemName)
        val ageTv = itemView?.findViewById<TextView>(R.id.itemAge)
        val sexTv = itemView?.findViewById<TextView>(R.id.itemSex)

        fun bind(person: Person) {
//            idTv?.text = person.id
            nameTv?.text = person.name.toString()
            ageTv?.text = person.age.toString()
            sexTv?.text = person.sex
        }
    }
}