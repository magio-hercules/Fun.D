package com.fundroid.routinesc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fundroid.scalarmmanager.builder.AlarmBuilder
import com.fundroid.scalarmmanager.enums.AlarmType
import com.fundroid.scalarmmanager.interfaces.IAlarmListener
import kotlinx.android.synthetic.main.activity_alarm.*
import java.util.concurrent.TimeUnit

class AlarmActivity : AppCompatActivity(), IAlarmListener {

    var builder: AlarmBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        setAlarm.setOnClickListener {
            Log.d("lsc","onClick setAlarm")
            builder?.setSpecificTimeAlarm()
        }
//        builder = AlarmBuilder().with(this)
//            .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(5))
//            .setId("UPDATE_INFO_SYSTEM_SERVICE")
//            .setAlarmType(AlarmType.REPEAT)

        builder = AlarmBuilder().with(this)
            .setSpecificTimeInMilliSeconds(1569567900000)
            .setId("UPDATE_INFO_SYSTEM_SERVICE")
            .setAlarmType(AlarmType.REPEAT)

    }

    override fun onResume() {
        super.onResume()
        builder?.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        builder?.removeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        builder?.cancelAlarm()
    }

    override fun perform(context: Context, intent: Intent) {
        Log.i("lsc", "Do your work here " + intent.action)
    }
}