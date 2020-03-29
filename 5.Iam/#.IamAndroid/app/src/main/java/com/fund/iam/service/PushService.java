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

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PushService extends FirebaseMessagingService {

    @Inject
    DataManager dataManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        dataManager.setPushToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LetterBus.getInstance().sendLetter(new Letter(LetterType.REMOTE.getLetterType(), remoteMessage.getData().get("message")));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }
}
