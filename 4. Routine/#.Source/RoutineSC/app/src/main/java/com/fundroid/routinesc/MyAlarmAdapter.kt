package com.fundroid.routinesc

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fundroid.routinesc.data.Alarm
import com.fundroid.routinesc.data.MyAlarm
import com.fundroid.routinesc.data.Routine
import kotlinx.android.synthetic.main.item_alarm.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_routine.view.*
import kotlinx.android.synthetic.main.item_routine.view.cover
import kotlinx.android.synthetic.main.item_routine.view.title

class MyAlarmAdapter(private val alarms: List<MyAlarm>, private val routine: Routine) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    private var onItemClick: OnItemClick? = null

    interface OnItemClick {
        fun onChoice()
    }

    fun setOnItemClick(OnItemClick: OnItemClick) {
        onItemClick = OnItemClick
    }

    var mContext: Context? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM

    }

    override fun getItemCount(): Int = alarms.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AlarmViewHolder) {
            holder.bind(alarms[position])
        } else if (holder is HeaderViewHolder) {
            holder.bind(alarms[position])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("lsc", "onCreateViewHolder")
        mContext = parent.context
        var viewHolder: RecyclerView.ViewHolder?
        if (viewType == VIEW_TYPE_HEADER) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            viewHolder = HeaderViewHolder(view)
        } else if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alarm, parent, false)
            viewHolder = AlarmViewHolder(view)
        } else viewHolder = null

        return viewHolder!!
    }

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var time = itemView.time
        private val image = itemView.image
        private val comment = itemView.comment
        private val onOff = itemView.onOff

        fun bind(alarm: MyAlarm) {
            Log.d("lsc", "bind ${alarm}")
            time.text = alarm.localTime.toString()
            comment.text = alarm.comment
            onOff.isChecked = alarm.isUse
            when (alarm.id) {
                1 -> image.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_background))
                2 -> image.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
                3 -> image.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_background))
                4 -> image.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_foreground))
            }
        }

    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cover = itemView.cover
        private val title = itemView.title
        private val btnChoice = itemView.choice

        fun bind(alarm: MyAlarm) {
            title.text = routine.detailMessage
            cover.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_launcher_background))
            btnChoice.setOnClickListener {
                Log.d("lsc", "itemClick ${alarm.id}")
                onItemClick!!.onChoice()
            }
        }

    }

}