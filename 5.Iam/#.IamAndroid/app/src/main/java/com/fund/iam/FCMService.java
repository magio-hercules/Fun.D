package com.fund.iam;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("lsc","onNewToken " + token);
        PrefAuth.setFcmToken(this, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("lsc","onMessageReceived " + remoteMessage.getData().get("title") + ", " + remoteMessage.getData().get("message") + ", " + remoteMessage.getData().get("data") + ", " + remoteMessage.getData().get("Message"));
        MessageBus.getInstance().sendMessageEvent(remoteMessage.getData().get("message"));
    }
}
