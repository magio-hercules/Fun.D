package com.fund.iam;

import android.os.Message;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.fund.iam.model.MessageData;
import com.fund.iam.model.MessageSendBody;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {
    private static final String TAG = "lsc";

    public ChatNavigator navigator;
    public ObservableField<String> input = new ObservableField<>();
    private MessageSendBody messageSendBody = new MessageSendBody();
    private MessageData messageData = new MessageData();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ChatViewModel() {
        Log.d(TAG, "ChatViewModel constructor");
        subscribeEvent();
    }

    private void subscribeEvent() {
        compositeDisposable.add(MessageBus.getInstance().getMessageEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Log.d("lsc", "message bus subscribe " + message);
                    navigator.onMessageAdd(new com.fund.iam.model.Message(1, message));
                }, onError -> {
                    Log.d("lsc", "message bus subscribe error  " + onError);
                }, () -> {
                    Log.d("lsc", "message bus subscribe terminated");
                }));
    }

    private List<Message> getPrevMessages() {
        return null;
    }

    public void onSend() {
        Log.d(TAG, "ChatViewModel onSend " + input);
        testSend();
    }

    private void testSend() {
//        messageSendBody.setTo("ePT67k1HR9mXGvWCyfx1OG:APA91bF3Lj7YMgJSuhlcQQWQ-8W6PxaMfiR2s-vZsRPiiqXQB17_dDWL7Qdo0nFyr5Upri96LBRQRwvP2Hli4ZtB115_bYLUk3p9H3aET2eW2LDUKp92STW1WU5l8UIagJJfzR8CLwlb");       // to J600
        messageSendBody.setTo("dQQrvN6BSVetYlJwi01_o5:APA91bECGnbqFtqKzWZP9OqAm-gfcuGbkYfVHjDc7Ali4Nya4cThpvntrFNpNu2roSqyL1TXfHEEV5Y9hWbAbL6CpIGM8td_9s4N1ZzlhTJMRsgD2yUHjqIiUQfL5IhYFNjrs6Wjgk3s");       // to 갤10
        messageSendBody.setPriority("high");
        messageData.setTitle("TestTitle");
        messageData.setMessage(input.get());
        messageSendBody.setData(messageData);
        compositeDisposable.add(RetrofitCreator.firebaseService().postFcmSend(messageSendBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Log.d(TAG, "testSend result " + result);
                    if (result.isSuccessful()) {
                        navigator.onMessageAdd(new com.fund.iam.model.Message(0, input.get()));
                        input.set("");
                    }
                }, onError -> {
                    Log.e(TAG, "testSend result " + onError.getLocalizedMessage());
                    input.set("");
                    navigator.handleError(onError);
                }));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ChatViewModel onCleared");
    }

    public void setNavigator(ChatNavigator navigator) {
        this.navigator = navigator;
    }
}
