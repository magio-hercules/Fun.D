package com.fund.iam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.fund.iam.R;
import com.fund.iam.core.IamApplication;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.fund.iam.core.AppConstants.NOTIFICATION_CHANNEL_ID_PUSH;
import static com.fund.iam.core.AppConstants.NOTIFICATION_CHANNEL_NAME_PUSH;

public class PushService extends FirebaseMessagingService {

    @Inject
    DataManager dataManager;

    @Inject
    NotificationManager notificationManager;

//    private Intent pendingIntent;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        dataManager.setPushToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LetterBus.getInstance().sendLetter(new Letter(LetterType.REMOTE.getLetterType(), remoteMessage.getData().get("message")));
        User user = new Gson().fromJson(remoteMessage.getData().get("user"), User.class);
        User friend = new Gson().fromJson(remoteMessage.getData().get("friend"), User.class);
        LetterBus.getInstance().sendBadge(user.getId());
//        pendingIntent.putExtra("user", remoteMessage.getData().get("user"));
//        pendingIntent.putExtra("friend", remoteMessage.getData().get("friend"));
        LetterBoxBus.getInstance().sendLetterBox(new LetterBox(friend));
        LetterBoxBus.getInstance().sendLetterBox(new LetterBox(user));
        if (((IamApplication) getApplication()).getCurrentActivity().getClass() != LetterActivity.class) {
            createNotification("메시지가 도착하였습니다.", remoteMessage.getData().get("message"), PendingIntent.getActivity(this, 1, new Intent(this, LetterActivity.class), PendingIntent.FLAG_UPDATE_CURRENT), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
    }

    private void createNotification(String title, String body, PendingIntent pendingIntent, /*PendingIntent deleteIntent, */Uri soundUri) {
        Logger.d("createNotification " + title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_PUSH, NOTIFICATION_CHANNEL_NAME_PUSH, NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
            Notification notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID_PUSH)
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
//        pendingIntent = new Intent(this, LetterActivity.class);

    }
}
