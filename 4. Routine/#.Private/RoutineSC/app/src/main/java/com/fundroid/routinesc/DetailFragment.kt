package com.fundroid.routinesc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

class DetailFragment : Fragment() {

    var builder: AlarmBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goAlarm.setOnClickListener {
            activity?.startActivity(Intent(activity, AlarmActivity::class.java))
        }

        setAlarm.setOnClickListener {
            Log.d("lsc", "onClick setAlarm")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis() + 10000
//            builder?.setTargetActivity(javaClass)
            builder?.setAlarm(calendar, true, AlarmType.ALARM)
        }

        builder = AlarmBuilder().with(context!!)



        //UI 텍스트
        UI_TextManager()

        //UI 이미지
        UI_ImageMAnager()
        
        //스위치버튼 알람 ON/OFF
        AlarmSwitchManager()
    }

    fun UI_TextManager() {
        tv_Title.text = getString(R.string.title_businessman)
        tv_Contents.text = getString(R.string.contents_businessman)
    }

    fun UI_ImageMAnager() {


    }
    //알림스위치
    fun AlarmSwitchManager(){
        //스위치 버튼
        sc_Alarm1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked)
            {
                Toast.makeText(this.context,"알람1_스위치온",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context,"알람1_스위치오프",Toast.LENGTH_SHORT).show()
            }
        }
        sc_Alarm2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked)
            {
                Toast.makeText(this.context,"알람2_스위치온",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context,"알림2_스위치오프",Toast.LENGTH_SHORT).show()
            }
        }
        sc_Alarm3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked)
            {
                Toast.makeText(this.context,"알람3_스위치온",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context,"알람3_스위치오프",Toast.LENGTH_SHORT).show()
            }
        }
    }
}