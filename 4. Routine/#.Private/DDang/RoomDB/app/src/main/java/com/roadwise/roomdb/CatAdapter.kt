package com.roadwise.roomdb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CatAdapter(val context: Context, val cats: List<Cat>) :
    RecyclerView.Adapter<CatAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(cats[position])
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val nameTv = itemView?.findViewById<TextView>(R.id.itemName)
        val lifeTv = itemView?.findViewById<TextView>(R.id.itemLifeSpan)
        val originTv = itemView?.findViewById<TextView>(R.id.itemOrigin)

        fun bind(cat: Cat) {
            nameTv?.text = cat.catName
            lifeTv?.text = cat.lifeSpan.toString()
            originTv?.text = cat.origin
        }
    }
}