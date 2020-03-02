package com.fund.iam;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class ChatViewModel extends ViewModel {
    private static final String TAG = "lsc";

    public ChatNavigator navigator;
    public String input;

    public ChatViewModel() {
        Log.d(TAG,"ChatViewModel constructor");
    }

    private List<Message> getPrevMessages() {
        return null;
    }

//    public void onSend() {
//        Log.d(TAG,"ChatViewModel onSend " + input);
//    }

    public View.OnClickListener onSend() {
        return v -> {
            Log.d(TAG,"ChatViewModel onSend " + input);
        };
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG,"ChatViewModel onCleared");
    }

    public void setNavigator(ChatNavigator navigator) {
        this.navigator = navigator;
    }
}
