package com.fundroid.routinesc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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

        //알람 액티비화면 노출
        goAlarm.setOnClickListener {
            activity?.startActivity(Intent(activity, AlarmActivity::class.java))
        }

        setAlarm.setOnClickListener {
            Log.d("lsc", "onClick setAlarm")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis() + 10000
            //  builder?.setTargetActivity(javaClass)
            builder?.setAlarm(calendar, true, AlarmType.ALARM)
        }

//        switch_fragments.setOnClickListener {
//            Log.d("kyo","onClick chage fras")
//            //화면 전환 프래그먼 선언 및 초기화면 설정
//            var fragmentTransaction:FragmentTransaction
//   }
        builder = AlarmBuilder().with(context!!)

        //UI 텍스트
        UI_TextManager()

        //UI 이미지
        UI_ImageMAnager()

    }

    fun UI_TextManager() {
        tv_Title.text = getString(R.string.title_businessman)
        tv_Contents.text = getString(R.string.contents_businessman)
    }

    fun UI_ImageMAnager() {
        clickFragment()
        switch_fragments.setOnClickListener {
            Log.d("move the fragment ", "프래그먼트이동")
        }

    }

    fun clickFragment() {


        //view?.findNavController()?.navigate(R.id.navigation_test)

        //var nav: NavController? = null

//            if (nav != null) {
//                Navigation.findNavController(view!!)
//            }

        //nav.navigate(R.id.navigation_test)


//            var fm:FragmentManager = activity!!.supportFragmentManager
//            var transaction = fm.beginTransaction()
//            transaction.replace(R.id.nav_host_fragment, detailfragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
    }
}


