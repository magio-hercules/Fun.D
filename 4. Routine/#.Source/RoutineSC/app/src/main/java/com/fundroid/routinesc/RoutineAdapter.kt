package com.fundroid.routinesc

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fundroid.routinesc.data.Routine
import kotlinx.android.synthetic.main.item_routine.view.*

class RoutineAdapter(private val routines: List<Routine>) :
    RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    var mContext : Context? = null

    private var onItemClick: OnItemClick? = null

    interface OnItemClick {
        fun onRoutineClick(routine: Routine)
    }

    fun setOnItemClick(OnItemClick: OnItemClick) {
        onItemClick = OnItemClick
    }

    override fun getItemCount(): Int = routines.size

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        mContext = parent.context
        return RoutineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_routine,
                parent,
                false
            )
        )
    }

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cover = itemView.cover
        private val title = itemView.title

        fun bind(routine : Routine) {
            title.text = routine.coverMessage
            when(routine.id) {
                1 -> cover.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
                2 -> cover.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
                3 -> cover.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
                4 -> cover.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
            }
            itemView.setOnClickListener {
                Log.d("lsc","itemClick ${routine.id}")
                onItemClick!!.onRoutineClick(routine)
            }
        }

    }

}