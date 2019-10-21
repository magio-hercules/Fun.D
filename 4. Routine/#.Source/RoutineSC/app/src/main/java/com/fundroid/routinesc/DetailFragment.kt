package com.fundroid.routinesc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

class DetailFragment : Fragment() {

    var builder: AlarmBuilder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goAlarm.setOnClickListener{
            activity?.startActivity(Intent(activity, AlarmActivity::class.java))
        }

        setAlarm.setOnClickListener {
            Log.d("lsc","onClick setAlarm")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis() + 10000
//            builder?.setTargetActivity(javaClass)
            builder?.setAlarm(calendar, true, AlarmType.ALARM)
        }

        builder = AlarmBuilder().with(context!!)
    }
}
