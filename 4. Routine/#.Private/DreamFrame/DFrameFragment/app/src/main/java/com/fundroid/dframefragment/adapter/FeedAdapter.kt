package com.fundroid.dframefragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fundroid.dframefragment.R
import com.fundroid.dframefragment.data.DummyItem
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val dataSet:MutableList<DummyItem>) : RecyclerView.Adapter<FeedAdapter.onViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): onViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return onViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    inner class onViewHolder(val view: View):RecyclerView.ViewHolder(view){
        fun bind(item:DummyItem) = with(view){
            view.setOnClickListener{
                view.findNavController().navigate(R.id.action_feedFragment_to_detailFragment)
            }
        }
    }
}
