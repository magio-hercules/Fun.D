package com.fundroid.routinesc

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_routine.view.*

class RoutineAdapter(private val routines: List<Routine>) :
    RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    override fun getItemCount(): Int = routines.size

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        Log.d("lsc","onCreateViewHolder")
        return RoutineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_routine,
                parent,
                false
            )
        )
    }

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.title

        fun bind(routine : Routine) {
            Log.d("lsc","bind ${routine}")

            title.text = routine.name
        }

    }

}