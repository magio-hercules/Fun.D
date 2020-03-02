package com.fund.iam;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fund.iam.model.Message;

import java.util.List;


public interface ChatNavigator {

    void onRepositoriesChanged(List<Message> messages);

    void showToast(String message);

    void handleError(Throwable throwable);

}
