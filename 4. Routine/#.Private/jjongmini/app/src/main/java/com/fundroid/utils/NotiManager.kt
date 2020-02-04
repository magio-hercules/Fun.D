package com.fundroid.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import com.fundroid.notitest.R
import com.fundroid.notitest.SettingActivity

class NotiManager  {

    var NOTIFICATION_ID_NORMAL = 1;
    var NOTIFICATION_ID_BIGTEXT = 2;
    var NOTIFICATION_ID_BIGPICTURE = 3;

    // Notification Type
    // ENUM으로 변경하기

    private var context:Context
    private var name:String
    private var description: String

    constructor(_context: Context, _name:String, _desc: String) {
        context = _context
        name = _name
        description = _desc
    }


    fun generateNotification(type: Int, title: String, content: String) {
        when(type) {
            NotiType.NORMAL.ordinal -> generateNormalNotification(title, content)
//            NotiType.BIG_TEXT.ordinal -> generateBigTextNotification(title, content)
        }
    }


    fun generateNormalNotification(title: String, content: String) {
        createNotificationChannel(context, NotificationManagerCompat.IMPORTANCE_DEFAULT,
            false, name, description)

        val channelId = "${context.packageName}-${name}"
        val intent = Intent(context, SettingActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // 노티를 터치했을때 액티비티 실행
        var stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(SettingActivity::class.java)
        stackBuilder.addNextIntent(intent)

//        val pendingIntent= PendingIntent.getActivity(context, 0,
//            intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var pendingIntent: PendingIntent
                = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(android.R.drawable.ic_menu_info_details)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        //true이면 사용자가 노티를 터치했을때 사라짐, false면 눌러도 사라지지 않음
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        // noti id가 같으면 1개의 노티만 생성, 다르면 여러개의 노티 생성
        notificationManager.notify(NotiType.NORMAL.ordinal, builder.build())
    }

    private fun createNotificationChannel(context: Context,
                                          importance: Int,
                                          showBadge: Boolean,
                                          name: String,
                                          description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
//            channel.enableLights(true)
//            channel.lightColor = Color.RED
//            channel.enableVibration(true)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {

        }
    }
}