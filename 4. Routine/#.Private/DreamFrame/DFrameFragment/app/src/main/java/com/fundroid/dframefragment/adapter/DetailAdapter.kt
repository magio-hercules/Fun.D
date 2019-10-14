package com.fundroid.dframefragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fundroid.dframefragment.R
import com.fundroid.dframefragment.data.DummyItem
import kotlinx.android.synthetic.main.fragment_detail.view.*

//내부 클래스 onViewHoder()클래스를 생성한후
class DetailAdapter (private val dataset:MutableList<DummyItem>):RecyclerView.Adapter<DetailAdapter.onViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):onViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
        return onViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)

    }

    inner class onViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        fun bind(dummyItem: DummyItem) {
            //사황 역할시
        }

    }

}