package com.fundroid.routinesc

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class AlarmBuilder {

    //alarm variables
    private var alarmManager: AlarmManager? = null
    private var bootCompleteReceiverComponentName: ComponentName? = null
    private var pendingIntent: PendingIntent? = null
    private var alarmIntent: Intent? = null
    private var packageManager: PackageManager? = null

    //builder variables
    private var context: Context? = null
    private var id: String? = null
    private var timeInMilliSeconds: Long = 0
    private var specificTimeInMilliSeconds: Long = 0

    companion object {
        private const val REQUEST_CODE = 111131
        var className: Class<Activity>? = null
    }

    fun with(context: Context): AlarmBuilder {
        this.context = context
        this.alarmManager = this.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        this.packageManager = context!!.getPackageManager()
        bootCompleteReceiverComponentName = ComponentName(context!!, BootCompleteReceiver::class.java!!)
        alarmIntent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        return this
    }

    fun setTargetActivity(target: Class<Activity>?): AlarmBuilder {
        Log.d("lsc","setTargetActivity ${target}")
        className = target
        alarmIntent?.putExtra("target",className.toString())
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        return this
    }

    fun setAlarm(calendar: Calendar, isWeekendOk: Boolean, type: AlarmType): AlarmBuilder {
        if (alarmManager != null) {

            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }

        // 부팅 후 실행되는 리시버 사용가능하게 설정
        packageManager?.setComponentEnabledSetting(
            bootCompleteReceiverComponentName!!,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        return this
    }

}