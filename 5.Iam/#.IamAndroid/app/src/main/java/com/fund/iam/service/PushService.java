package com.fund.iam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.ui.letter.LetterActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PushService extends FirebaseMessagingService {

    @Inject
    DataManager dataManager;

    @Inject
    NotificationManager notificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        dataManager.setPushToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LetterBus.getInstance().sendLetter(new Letter(LetterType.REMOTE.getLetterType(), remoteMessage.getData().get("message")));
        Logger.d("onMessageReceived " + remoteMessage.getData().get("message"));
        createNotification("testTitle", "메시지가 도착했습니다.", PendingIntent.getActivity(this, 1, new Intent(this, LetterActivity.class), PendingIntent.FLAG_UPDATE_CURRENT), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

    }

    private void createNotification(String title, String body, PendingIntent pendingIntent, /*PendingIntent deleteIntent, */Uri soundUri) {
        Logger.d("createNotification " + title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, "PUSH")
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
//                    .setDeleteIntent(deleteIntent)
                    .build();
            notificationManager.notify(0, notification);
        } else {
            Notification notification = new Notification.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .build();
            notificationManager.notify(0, notification);
        }
        Logger.d("createNotification end");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }
}
