package com.example.roomdbtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(
    val context: Context,
    var persons: List<Person>,
    val itemClick: (Person) -> Unit
): RecyclerView.Adapter<PersonAdapter.Holder>() {

    internal fun setItems(persons: List<Person>) {
        this.persons = persons
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return persons.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(persons[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (Person) -> Unit) : RecyclerView.ViewHolder(itemView!!) {
        val idTv = itemView?.findViewById<TextView>(R.id.itemID)
        val nameTv = itemView?.findViewById<TextView>(R.id.itemName)
        val ageTv = itemView?.findViewById<TextView>(R.id.itemAge)
        val sexTv = itemView?.findViewById<TextView>(R.id.itemSex)

        fun bind(person: Person, context: Context) {
            idTv?.text = person.id.toString()
            nameTv?.text = person.name.toString()
            ageTv?.text = person.age.toString()
            sexTv?.text = person.sex

            itemView.setOnClickListener {
                itemClick(person)
                Toast.makeText(context, "${person.id} / ${person.name} / ${person.age} / ${person.sex}", Toast.LENGTH_LONG).show()

                val personDB = PersonDB.getInstance(context)

//                personDB?.personDao()?.delete(person)
            }
        }
    }
}