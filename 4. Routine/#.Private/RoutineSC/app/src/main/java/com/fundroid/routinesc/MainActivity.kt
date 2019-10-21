package com.fundroid.routinesc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.fundroid.routinesc.RoutineApplication.Companion.database
import com.fundroid.routinesc.data.Routine
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    val scope = CoroutineScope(Dispatchers.IO)
    var myAlarmSize: Int = 0
    lateinit var navController: NavController
    var selectedRoutineId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lsc", "MainActivity onCreate")
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_main,
            R.id.navigation_detail,
            R.id.navigation_setting
        ).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(nav_view, navController)

        runBlocking {
            val ioJob = scope.launch {
                myAlarmSize = database.myAlarmDao().getMyAlarms().size
                Log.d("lsc","1 myAlarmSize ${myAlarmSize}")
                if(myAlarmSize != 0 ) selectedRoutineId = database.myAlarmDao().getMyAlarms().get(0).routineId
            }
            ioJob.join()

        }
        Log.d("lsc","2 myAlarmSize ${myAlarmSize}")
        if(myAlarmSize == 0) {
            setDetailEnable(false)
        } else {
            setDetailEnable(true)
            goDetail(selectedRoutineId)
        }

    }

    open fun setDetailEnable(isSet : Boolean) {
        nav_view.menu.getItem(1).setCheckable(isSet)
        nav_view.menu.getItem(1).setEnabled(isSet)
    }

    open fun goDetail(routineId: Int) {
        val bundle = Bundle()
        bundle.putInt("routineId", routineId)
        navController!!.navigate(R.id.navigation_detail, bundle)
    }
}
