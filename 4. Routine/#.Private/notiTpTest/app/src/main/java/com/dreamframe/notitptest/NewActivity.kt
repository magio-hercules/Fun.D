package com.dreamframe.notitptest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

//내부알림 버튼 테스트 액티비티
class NewActivity : AppCompatActivity() {
    //동반자 객체 class 내부정보 접근 하기 위해 함수에 사용하기위한 것
    companion object {
        const val TAG = "MainActivity"
        const val NOTIFICATION_ID = 1001
        const val NOTIFICATION_ID_2 = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
    }

    //기본
    fun basic(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "기본 알림"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    //    빅테스트스타일 하단 드래그로 긴텍스트내용 적용됨
    fun bigTextStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "긴 텍스트 알림"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bigText = "텍스트 긴 텍스트 샬라샬라샬라 텍스트 긴 텍스트 샬라샬라샬라 텍스트 긴 텍스트 샬라샬라샬라 텍스트 긴 텍스트 샬라샬라샬라"
        val style = NotificationCompat.BigTextStyle()
        style.bigText(bigText)

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    //빅픽쳐스타일 하단 드래그로 이미지를 추가할수 잇다
    fun bigPictureStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "타이틀"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val style = NotificationCompat.BigPictureStyle()
        style.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.castle))

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun inboxStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "3 Mails"
        val content = "+5 Mails"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val style = NotificationCompat.InboxStyle()
        style.addLine("Mail1 ...")
        style.addLine("Mail2 ...")
        style.addLine("Mail3 ...")

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun messagingStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "타이틀"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val userIcon1 = IconCompat.createWithResource(this, R.drawable.android)
        val userIcon2 = IconCompat.createWithResource(this, R.drawable.android)
        val userIcon3 = IconCompat.createWithResource(this, R.drawable.android)
        val userName1 = "김사람"
        val userName2 = "이사람"
        val userName3 = "안사람"
        val timestamp = System.currentTimeMillis()
        val user1 = Person.Builder().setIcon(userIcon1).setName(userName1).build()
        val user2 = Person.Builder().setIcon(userIcon2).setName(userName2).build()
        val user3 = Person.Builder().setIcon(userIcon3).setName(userName3).build()
        val style = NotificationCompat.MessagingStyle(user3)
        style.addMessage("김김김김김", timestamp, user1)
        style.addMessage("이이이이이이", timestamp, user2)

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun mediaStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "타이틀"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.castle))
        builder.addAction(
            NotificationCompat.Action(
                R.drawable.android, "skip prev", pendingIntent
            )
        )
        builder.addAction(
            NotificationCompat.Action(
                R.drawable.android, "skip prev", pendingIntent
            )
        )
        builder.addAction(
            NotificationCompat.Action(
                R.drawable.android, "pause", pendingIntent
            )
        )
        builder.addAction(
            NotificationCompat.Action(
                R.drawable.android, "skip next", pendingIntent
            )
        )
        builder.addAction(
            NotificationCompat.Action(
                R.drawable.android, "skip prev", pendingIntent
            )
        )
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                1,
                2,
                3
            )
        )
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)


        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun inlineReplay(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "3 Messages"
        val content = "+5 Messages"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val replyLabel = "답장은 여기로 보내세요"
        val remoteInput = RemoteInput.Builder("답장 키")
            .setLabel(replyLabel)
            .build()
        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.sym_action_chat, "답장", pendingIntent
        )
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        val userIcon1 = IconCompat.createWithResource(this, R.drawable.ic_codechacha)
        val userIcon2 = IconCompat.createWithResource(this, R.drawable.android)
        val userIcon3 = IconCompat.createWithResource(this, R.drawable.android)
        val userName1 = "유저_1"
        val userName2 = "유저_2"
        val userName3 = "유저_3"
        val timestamp = System.currentTimeMillis()
        val user1 = Person.Builder().setIcon(userIcon1).setName(userName1).build()
        val user2 = Person.Builder().setIcon(userIcon2).setName(userName2).build()
        val user3 = Person.Builder().setIcon(userIcon3).setName(userName3).build()
        val style = NotificationCompat.MessagingStyle(user3)
        style.addMessage("설명에대 말하시오", timestamp, user1)
        style.addMessage("무엇을 알고 싶은가", timestamp, user2)

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        builder.addAction(replyAction)
        builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "디스미스", pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun headUp(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "타이틀"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.ic_codechacha)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setAutoCancel(true)
        builder.setFullScreenIntent(fullScreenPendingIntent, true)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID_2, builder.build())
    }

    fun createPicturelinkStyle(v: View) {
        clearExistingNotifications(NOTIFICATION_ID)
        clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(
            this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "앱 노티피케이션 채널"
        )

        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "타이틀"
        val content = "오늘 신제품은 ㅇㅇㅇ 입니다 P"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val style = NotificationCompat.BigPictureStyle()
        style.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.castle))

        val fullScreenPendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.android)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setStyle(style)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        builder.setFullScreenIntent(fullScreenPendingIntent, true)
        builder.addAction(android.R.drawable.btn_star,"링크",pendingIntent)
        builder.addAction(android.R.drawable.btn_star,"링크",pendingIntent)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(
        context: Context, importance: Int, showBadge: Boolean,
        name: String, description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun clearExistingNotifications(notificationId: Int) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }
}