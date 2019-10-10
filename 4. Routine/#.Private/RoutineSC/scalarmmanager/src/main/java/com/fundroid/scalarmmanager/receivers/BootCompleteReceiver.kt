package com.fundroid.scalarmmanager.receivers;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fundroid.scalarmmanager.builder.AlarmBuilder
import com.fundroid.scalarmmanager.enums.AlarmType

import java.util.concurrent.TimeUnit

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmBuilder().with(context)
                    .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
                    .setId("UPDATE_INFO_SYSTEM_SERVICE")
                    .setAlarmType(AlarmType.REPEAT)
                    .setAlarm()
        }
    }
}
