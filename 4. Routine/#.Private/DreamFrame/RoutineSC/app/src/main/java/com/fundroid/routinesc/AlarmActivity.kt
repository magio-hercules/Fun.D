package com.fundroid.routinesc

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //윈도우의 특성읠 변경하는 메소드
        //FEATURE_NO_TITLE 커스텀 타이틀을 사용
        //https://diyall.tistory.com/789 사용정보
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //레이아웃 서비스 일경우
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        setContentView(R.layout.activity_alarm)
    }
}