package com.fundroid.routinesc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fundroid.routinesc.RoutineApplication.Companion.database
import com.fundroid.routinesc.data.Alarm
import com.fundroid.routinesc.data.MyAlarm
import com.fundroid.routinesc.data.Routine
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailFragment : Fragment(), AlarmAdapter.OnItemClick, MyAlarmAdapter.OnItemClick {

    var builder: AlarmBuilder? = null

    val scope = CoroutineScope(Dispatchers.IO)
    var myAlarmSize: Int = 0
    lateinit var alarms: List<Alarm>
    lateinit var myAlarms: List<MyAlarm>
    lateinit var routine: Routine

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("lsc", "arguments $arguments")
        runBlocking {
            val ioJob = scope.launch {
                myAlarmSize = database.myAlarmDao().getMyAlarms().size
                if (myAlarmSize == 0) {
                    routine = database.routineDao()
                        .getRoutineById(arguments!!.getInt("routineId"))
                    alarms = database.alarmDao()
                        .getAlarms(routine.id)

                    val alarmAdapter = AlarmAdapter(alarms, routine)
                    alarmAdapter.setOnItemClick(this@DetailFragment)
                    rv_details.apply {
                        adapter = alarmAdapter
                    }
                } else {
                    myAlarms = database.myAlarmDao().getMyAlarms()

                    val alarmAdapter = MyAlarmAdapter(
                        myAlarms,
                        database.routineDao().getRoutineById(myAlarms.get(0).routineId)
                    )
                    alarmAdapter.setOnItemClick(this@DetailFragment)
                    rv_details.apply {
                        adapter = alarmAdapter
                    }
                }
            }
            ioJob.join()
            builder = AlarmBuilder().with(context!!)
        }


//        goAlarm.setOnClickListener{
//            activity?.startActivity(Intent(activity, AlarmActivity::class.java))
//        }
//
//        setAlarm.setOnClickListener {
//            Log.d("lsc","onClick setAlarm")
//            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = System.currentTimeMillis() + 10000
//            builder?.setAlarm(calendar, true, AlarmType.ALARM)
//        }
//
//        builder = AlarmBuilder().with(context!!)
    }

    override fun onChoice() {
        (activity as MainActivity).setDetailEnable(true)
        runBlocking {
            val ioJob = scope.launch {
                database.myAlarmDao().nukeTable()

                if (myAlarmSize == 0) {
                    Log.d("lsc", "alarms ${alarms.size}")
                    for (i in alarms.indices) {
                        database.myAlarmDao().createMyAlarm(
                            MyAlarm(
                                alarms[i].localTime,
                                alarms[i].comment,
                                alarms[i].isUse,
                                alarms[i].routineType,
                                alarms[i].routineId
                            )
                        )
                    }
                } else {
                    Log.d("lsc", "myAlarms ${myAlarms.size}")
                    for (i in myAlarms.indices) {
                        database.myAlarmDao().createMyAlarm(
                            MyAlarm(
                                myAlarms[i].localTime,
                                myAlarms[i].comment,
                                myAlarms[i].isUse,
                                myAlarms[i].routineType,
                                myAlarms[i].routineId
                            )
                        )
                    }
                }
            }
            ioJob.join()
        }

//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis() + 10000
//        builder?.setAlarm(calendar, true, AlarmType.ALARM)
//https://stackoverflow.com/questions/29346725/android-alarm-manager-not-triggering-event
    }

}
