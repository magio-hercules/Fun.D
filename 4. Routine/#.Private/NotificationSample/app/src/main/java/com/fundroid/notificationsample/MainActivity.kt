package com.fundroid.notificationsample

import android.app.*
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.nio.channels.Channel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var notificationManager : NotificationManager? = null
    var bubble : Button? = null
    var noti1 : Button? = null
    lateinit var channel1 : NotificationChannel
    lateinit var channel2 : NotificationChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bubble = findViewById(R.id.bubble)
        bubble?.setOnClickListener(this)
        noti1 = findViewById(R.id.noti1)
        noti1?.setOnClickListener(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        channel1 = createNotificationChannel("TestId1","TestChannelName1","TestDescription1")
        channel2 = createNotificationChannel("TestId2","TestChannelName2","TestDescription2")
        val channelGroup = NotificationChannelGroup("GroupId", "GroupName")
        notificationManager?.createNotificationChannelGroup(channelGroup)
    }

    private fun createNotificationChannel(id: String, name: String, description: String) : NotificationChannel {

        // Channel Importance : IMPORTANCE_DEFAULT,IMPORTANCE_HIGH,IMPORTANCE_LOW,IMPORTANCE_MAX,IMPORTANCE_MIN,IMPORTANCE_NONE,IMPORTANCE_UNSPECIFIED
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id,name,importance)
        channel.group
        channel.audioAttributes
        channel.vibrationPattern = longArrayOf(100,200,300,400,500,400,300,200,100)
        channel.lightColor = Color.RED
        channel.enableLights(true)
        channel.description = description
        channel.id
        channel.importance
        channel.lockscreenVisibility
        channel.name
        channel.sound
        channel.enableVibration(true)
        channel.hasUserSetImportance()
        channel.setAllowBubbles(true)

        val audioAttributes:AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),audioAttributes)
        channel.shouldShowLights()
        channel.shouldVibrate()
        Log.d("lsc", "canBubble " + channel.canBubble())
        Log.d("lsc", "canBypassDnd " + channel.canBypassDnd())
        Log.d("lsc", "canShowBadge " + channel.canShowBadge())

        return channel
    }

    private fun sendNotification() {
        Log.d("lsc", "sendNotification ")
        val channelId = "TestId"

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("ContentTitle")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        notificationManager?.createNotificationChannel(channel1)
        notificationManager?.notify(3,notification)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.bubble -> {
                Log.d("lsc", "bubble click ")
                val target = Intent(this@MainActivity, MainActivity::class.java)
                val bubbleIntent :PendingIntent = PendingIntent.getActivity(this@MainActivity,0,target, PendingIntent.FLAG_UPDATE_CURRENT)

                var bubbleData : Notification.BubbleMetadata = Notification.BubbleMetadata.Builder()
                    .setIcon(Icon.createWithResource(this@MainActivity, R.mipmap.ic_launcher))
                    .setIntent(bubbleIntent)
                    .setDesiredHeight(600).build()

                var builder : Notification.Builder = Notification.Builder(this@MainActivity, channel1?.id)
                    .setContentTitle("BubbleContentTitle")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setBubbleMetadata(bubbleData)

                notificationManager?.createNotificationChannel(channel1)
                notificationManager?.notify(2,builder.build())
            }

            R.id.noti1 -> {
                Log.d("lsc", "noti1 click ")
                sendNotification()
            }

        }
    }
}
