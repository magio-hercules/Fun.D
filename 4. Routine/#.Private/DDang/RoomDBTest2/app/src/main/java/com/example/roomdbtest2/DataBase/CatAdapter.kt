package com.example.roomdbtest2.DataBase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.DataBase.CatDTO
import com.example.roomdbtest2.R

class CatAdapter(val context: Context, val cats: List<CatDTO>) : RecyclerView.Adapter<CatAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder?.bind(cats[position])
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val nameTv = itemView?.findViewById<TextView>(R.id.itemName)
        val lifeTv = itemView?.findViewById<TextView>(R.id.itemLifeSpan)
        val originTv = itemView?.findViewById<TextView>(R.id.itemOrigin)

        fun bind(cat: CatDTO) {
            nameTv?.text = cat.catName
            lifeTv?.text = cat.lifeSpan.toString()
            originTv?.text = cat.origin
        }
    }

}

