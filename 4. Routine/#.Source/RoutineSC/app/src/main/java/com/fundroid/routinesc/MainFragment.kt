package com.fundroid.routinesc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fundroid.routinesc.RoutineApplication.Companion.database
import com.fundroid.routinesc.data.Routine
import kotlinx.android.synthetic.main.fragment_main.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

class MainFragment : Fragment(), RoutineAdapter.OnItemClick {

    val scope = CoroutineScope(Dispatchers.IO)
    lateinit var routines: List<Routine>
    var myAlarmSize: Int = 0
    var selectedRoutineId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lsc","MainFragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RoutineApplication.prefs.test = "leesc"
        runBlocking {
            val ioJob = scope.launch {
                routines = database.routineDao().getRoutines()
                myAlarmSize = database.myAlarmDao().getMyAlarms().size
                if (myAlarmSize != 0) {
                    selectedRoutineId = database.myAlarmDao().getMyAlarms().get(0).routineId
                }
            }
            ioJob.join()

        }
        val routineAdapter = RoutineAdapter(routines)
        routineAdapter.setOnItemClick(this)
        vp2_routines.apply {
            adapter = routineAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("lsc", "onPageSelected ${position} selectedRoutineId ${selectedRoutineId}")
                    title_routine.setText(routines[position].topComment)
                    footer_routine.setText(routines[position].bottomComment)
                    when (selectedRoutineId) {
                        -1 -> switch_routine.isChecked = false
                        position + 1 -> switch_routine.isChecked = true
                        else -> switch_routine.isChecked = false
                    }
                }
            })
        }
    }

    override fun onRoutineClick(routine: Routine) {
        (activity as MainActivity).goDetail(routine.id)

    }
}
