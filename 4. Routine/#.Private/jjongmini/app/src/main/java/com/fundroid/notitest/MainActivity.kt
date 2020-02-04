package com.fundroid.notitest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.fundroid.utils.NotiManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var NOTIFICATION_ID_NORMAL = 1;
    var NOTIFICATION_ID_BIGTEXT = 2;
    var NOTIFICATION_ID_BIGPICTURE = 3;
    val NOTIFICATION_ID_HEADUP = 4;

    var notiManager: NotiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        init()

        notiNormal.setOnClickListener { view ->
//            var not = getNotificationBuilder("channel1", "1st channel")
//            not.setTicker("Ticker")
//            not.setSmallIcon(android.R.drawable.ic_menu_search)
//
//            var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//            not.setLargeIcon(bitmap)
//            not.setNumber(100)
//            not.setAutoCancel(true)
//            not.setContentTitle("Content Title")
//            not.setContentText("Content Text")
//
//            var notification = not.build()
//
//            var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotiManager
//            mng.notify(10, notification)

            notiManager?.generateNormalNotification("이거슨 Normal 스타일",
                "Fundroid 4번째 프로젝트 루틴!!")

        }

        notiBigText.setOnClickListener { view ->
            generateBigTextNotification()
        }

        notiBigPicture.setOnClickListener { view ->
            generateBigPictureNotification()
        }

        notiHeadUp.setOnClickListener { view ->
            generateHeadUpNotification()
        }

    }

    fun init() {
        notiManager = NotiManager(this,
//            getString(R.string.app_name),
            "Routine: Fundroid 4th Project",
            "Fundroid Routine Notification")
    }

    fun getNotificationBuilder(id:String, name:String) : NotificationCompat.Builder {
        var not : NotificationCompat.Builder? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
//            channel.enableLights(true)
//            channel.lightColor = Color.RED
//            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            not = NotificationCompat.Builder(this, id)

        }else{
            not = NotificationCompat.Builder(this)
        }
        return not
    }

    private fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                          name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
//            channel.enableLights(true)
//            channel.lightColor = Color.RED
//            channel.enableVibration(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {

        }
    }

    private fun generateNormalNotification() {
//        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT,
//            false, getString(R.string.app_name), "App notification channel")
//
//        val channelId = "$packageName-${getString(R.string.app_name)}"
//        val title = "이거슨 Normal 스타일"
//        val content = "Fundroid 4번째 프로젝트 루틴!!"
//
//        val intent = Intent(baseContext, SettingActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        // 노티를 터치했을때 액티비티 실행
//        val pendingIntent = PendingIntent.getActivity(baseContext, 0,
//            intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val builder = NotificationCompat.Builder(this, channelId)
//        builder.setSmallIcon(android.R.drawable.ic_menu_info_details)
//        builder.setContentTitle(title)
//        builder.setContentText(content)
//        builder.priority = NotificationCompat.PRIORITY_HIGH
//        //true이면 사용자가 노티를 터치했을때 사라짐, false면 눌러도 사라지지 않음
//        builder.setAutoCancel(true)
//        builder.setContentIntent(pendingIntent)
//
//        val notificationManager = NotificationManagerCompat.from(this)
//        // noti id가 같으면 1개의 노티만 생성, 다르면 여러개의 노티 생성
//        notificationManager.notify(NOTIFICATION_ID_NORMAL, builder.build())
    }

    private fun generateBigTextNotification() {
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel 2")

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "이거슨 BigText 스타일"
        val content = "열어보시오~"

        val intent = Intent(baseContext, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bigText = "(1절)"+
                "동해물과 백두산이 마르고 닳도록\n" +
                "하느님이 보우하사 우리나라만세\n" +
                "(후렴)무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세\n" +
                "(2절)" +
                "남산위에 저 소나무 철갑을 두른듯\n" +
                "바람서리 불변함은 우리기상 일세\n" +
                "(후렴)무궁화 삼천리 화려강산 대한사람 대한으로 길이보전하세\n" +
                "(3절)" +
                "가을하늘 공활한데 높고 구름없이 \n" +
                "밝은달은 우리가슴 일편단심일세\n" +
                "(후렴)무궁화 삼천리 화려강산 대한사람 대한으로 길이보전하세\n" +
                "(4절)" +
                "이 기상과 이 맘으로 충성을 다하여\n" +
                "괴로우나 즐거우나 나라사랑하세\n" +
                "(후렴)무궁화 삼천리 화려강산 대한사람 대한으로 길이보전하세\n"
        val style = NotificationCompat.BigTextStyle()   // 1
        style.bigText(bigText)    // 2

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(android.R.drawable.ic_menu_help)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)   // 3
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID_BIGTEXT, builder.build())
    }

    private fun generateBigPictureNotification() {
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel 3")

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "BigPicture 스타일"
        val content = "지와니 사진 ㅋㅋ"

        val intent = Intent(baseContext, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val style = NotificationCompat.BigPictureStyle()    // 1
        style.bigPicture(
            BitmapFactory.decodeResource(resources, R.drawable.jiwan))   // 2

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(android.R.drawable.ic_menu_camera)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)   // 3
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID_BIGPICTURE, builder.build())
    }

    private fun generateHeadUpNotification() {
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
            getString(R.string.app_name), "App notification channel HeadUp")   // 1

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "Android Developer"
        val content = "Notifications in Android P"

        val intent = Intent(baseContext, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 2

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_HIGH   // 3
        builder.setAutoCancel(true)
        builder.setFullScreenIntent(fullScreenPendingIntent, true)   // 4

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID_HEADUP, builder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



}
