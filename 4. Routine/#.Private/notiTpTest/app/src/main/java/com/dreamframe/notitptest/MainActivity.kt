package com.dreamframe.notitptest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val url = "https://smartstore.naver.com/yangpum"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹뷰 객세 셋팅
        val settings = web_view.settings

        //웹뷰안에 자바스크립트 허용
        settings.javaScriptEnabled = true

        //웹뷰 캐시 사용 과 설치
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)

        //웹뷰 확대 사용
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true

        //확대 뷰 글자 사이즈
        settings.textZoom = 125
        //추가적 웹뷰 기능셋팅
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true //api 26
        }

        //setting.pluginState = WebSetting.pluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false

        //추가적 셋팅, 입맛대로 적용할것들
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        //웹뷰 셋팅
        web_view.fitsSystemWindows = true

        web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        //웹뷰 윈도우 화면으로 설정
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                //랜딩 페이지 시작
                toast("페이지 로딩중")

                //백 버튼 사용방법
                button_back.isEnabled = web_view.canGoBack()
                button_forward.isEnabled = web_view.canGoForward()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                toast("Page Loaded: ${view?.title}")
                //
                //백 버튼 사용방법
                button_back.isEnabled = web_view.canGoBack()
                button_forward.isEnabled = web_view.canGoForward()
            }
        }
        //ui 클릭모음
        ui_OnClick()
    }

    //클릭 모음
    fun ui_OnClick() {
        button_load.setOnClickListener {
            web_view.loadUrl(url)

            button_back.setOnClickListener {
                if (web_view.canGoBack()) {
                    web_view.goBack()
                }
            }

            button_forward.setOnClickListener {
                if (web_view.canGoForward()) {
                    web_view.goForward()
                }
            }
        }
        //액티비티 이동 클릭버튼
        PushButton.setOnClickListener {
            //액티비티 이동
            val nextIntent = Intent(this, NewActivity::class.java)
            startActivity(nextIntent)
        }
    }

    //보여지는 앱의 다이얼로그 나가게 해주는 함수
    private fun showAppExitDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Please confirm")
        builder.setMessage("No back history found, want to exit the app?")
        builder.setCancelable(true)
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
            //toast("전 내역으로 돌아가기")
        } else {
            showAppExitDialog()
        }
    }

}
//보여질 텍스트 확장함수
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}