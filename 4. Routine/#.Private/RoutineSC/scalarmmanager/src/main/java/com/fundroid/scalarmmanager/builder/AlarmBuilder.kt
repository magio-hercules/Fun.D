package com.fundroid.scalarmmanager.builder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.fundroid.scalarmmanager.enums.AlarmType
import com.fundroid.scalarmmanager.interfaces.IAlarmListener
import java.util.*

class AlarmBuilder {

    //alarm variables
    private var alarmManager: AlarmManager? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var pendingIntent: PendingIntent? = null
    private var alarmListenerSet: MutableSet<IAlarmListener>? = null

    //builder variables
    private var context: Context? = null
    private var id: String? = null
    private var timeInMilliSeconds: Long = 0
    private var specificTimeInMilliSeconds: Long = 0
    private var alarmType = AlarmType.ONE_TIME
    private var alarmListener: IAlarmListener? = null

    companion object {
        private const val REQUEST_CODE = 111131
    }

    fun with(context: Context): AlarmBuilder {
        this.context = context
        this.alarmListenerSet = HashSet()
        this.alarmManager = this.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return this
    }

    fun setId(id: String): AlarmBuilder {
        this.id = id
        return this
    }

    fun setSpecificTimeInMilliSeconds(specificTimeInMilliSeconds: Long): AlarmBuilder {
        this.specificTimeInMilliSeconds = specificTimeInMilliSeconds
        return this
    }

    fun setTimeInMilliSeconds(timeInMilliSeconds: Long): AlarmBuilder {
        this.timeInMilliSeconds = timeInMilliSeconds
        return this
    }

    fun setAlarmType(alarmType: AlarmType): AlarmBuilder {
        this.alarmType = alarmType
        return this
    }

    fun setSpecificTimeAlarm(): AlarmBuilder {
        val alarm = Alarm(context, id, specificTimeInMilliSeconds, AlarmType.REPEAT, alarmListener)

        if (alarm.context == null) {
            throw IllegalStateException("Context can't be null!")
        }

        if (alarm.id == null) {
            throw IllegalStateException("Id can't be null!")
        }

        initAlarm()

        return this
    }

    fun setAlarm(): AlarmBuilder {

        val alarm = Alarm(context, id, timeInMilliSeconds, alarmType, alarmListener)

        if (alarm.context == null) {
            throw IllegalStateException("Context can't be null!")
        }

        if (alarm.id == null) {
            throw IllegalStateException("Id can't be null!")
        }

        initAlarm()

        return this
    }

    //General Methods:------------------------------------------------------------------------------
    private fun initAlarm() {

        //creating intent
        val intent = Intent(this.id)
        val alarmRunning = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null

        //setting broadcast
        this.broadcastReceiver = this.getBroadcastReceiver()
        this.context!!.registerReceiver(this.broadcastReceiver, IntentFilter(this.id))

        //setting alarm
        val ensurePositiveTime = Math.max(this.timeInMilliSeconds, 0L)
        Log.d("lsc","ensurePositiveTime ${ensurePositiveTime}")
        this.pendingIntent = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Check if alarm is already running
        if (!alarmRunning) {
            Log.d("lsc","alarmType ${this.alarmType}")
            when (this.alarmType) {
                AlarmType.REPEAT -> {
                    Log.d("lsc","initAlarm REPEAT ${System.currentTimeMillis()}")
                    this.alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, this.pendingIntent)
                }
                AlarmType.ONE_TIME -> this.alarmManager!!.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ensurePositiveTime, this.pendingIntent)
            }
        } else {
            Log.e("lsc", "Alarm already running.!")
            updateAlarm()
        }
    }

    fun cancelAlarm() {

        this.alarmListenerSet?.clear()

        if(this.pendingIntent != null) {
            this.alarmManager?.cancel(this.pendingIntent)
        }

        if(this.broadcastReceiver != null) {
            this.context?.unregisterReceiver(this.broadcastReceiver)
            this.broadcastReceiver = null
        }

        Log.e("lsc", "Alarm has been canceled..!")
    }

    private fun updateAlarm() {
        Log.e("lsc", "updateAlarm")
        //calculating alarm time and creating pending intent
        val intent = Intent(this.id)
        val ensurePositiveTime = Math.max(this.timeInMilliSeconds, 0L)
        this.pendingIntent = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //removing previously running alarm
        this.alarmManager!!.cancel(this.pendingIntent)
        this.context!!.unregisterReceiver(this.broadcastReceiver)

        //setting broadcast
        this.broadcastReceiver = this.getBroadcastReceiver()
        this.context!!.registerReceiver(this.broadcastReceiver, IntentFilter(this.id))

        //Check if alarm is already running
        when (this.alarmType) {
            AlarmType.REPEAT -> {
                Log.d("lsc","updateAlarm REPEAT ${System.currentTimeMillis()}")
                this.alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+30000, AlarmManager.INTERVAL_DAY, this.pendingIntent)
            }
            AlarmType.ONE_TIME -> this.alarmManager!!.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ensurePositiveTime, this.pendingIntent)
        }

//        //set timer you want alarm to work (here I have set it to 8.30)
//        val timeOff9 = Calendar.getInstance()
//        timeOff9.set(Calendar.HOUR_OF_DAY, 8)
//        timeOff9.set(Calendar.MINUTE, 30)
//        timeOff9.set(Calendar.SECOND, 0)

        Log.e("lsc", "Alarm updated..!")

    }

    //Listeners:------------------------------------------------------------------------------------
    @Synchronized
    fun addListener(alarmListener: IAlarmListener?) {
        if (alarmListener == null) {
            throw IllegalStateException("Listener can't be null!")
        }

        this.alarmListenerSet!!.add(alarmListener)
    }

    @Synchronized
    fun removeListener(alarmListener: IAlarmListener?) {
        if (alarmListener == null) {
            throw IllegalStateException("Listener can't be null!")
        }

        this.alarmListenerSet!!.remove(alarmListener)
    }

    //Broadcast Receiver:---------------------------------------------------------------------------
    private fun getBroadcastReceiver(): BroadcastReceiver {

        return object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                Log.d("lsc","AlarmBuilder onReceive intent ${intent}")
                for (alarmListener in this@AlarmBuilder.alarmListenerSet!!) {
                    alarmListener.perform(context, intent)
                }
            }
        }
    }
}