package com.fund.iam;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.databinding.ActivityChatBinding;
import com.fund.iam.model.Message;

import java.util.List;


public class ChatActivity extends AppCompatActivity implements ChatNavigator {
    private static final String TAG = "lsc";

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();

    private ChatViewModel chatViewModel;
    private ActivityChatBinding chatBinding;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        if (viewModelFactory == null) {
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }

        chatViewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        chatViewModel.setNavigator(this);
        chatBinding.setViewModel(chatViewModel);
        initViews();
    }

    private void initViews() {
        chatAdapter = new ChatAdapter();
        chatBinding.messages.setLayoutManager(new LinearLayoutManager(this));
        chatBinding.messages.setAdapter(chatAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    public void onRepositoriesChanged(List<Message> messages) {
        chatAdapter.clearItems();
        chatAdapter.addItems(messages);
    }

    @Override
    public void onMessageAdd(Message message) {
        chatAdapter.addItem(message);
    }

    @Override
    public void showToast(String message) {
        Log.d(TAG, "showToast " + message);
    }

    @Override
    public void handleError(Throwable throwable) {
        Log.d(TAG, "throwable " + throwable.getMessage());
    }
}
