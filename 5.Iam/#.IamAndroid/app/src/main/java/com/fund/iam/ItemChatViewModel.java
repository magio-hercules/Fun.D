package com.fund.iam;

import androidx.databinding.ObservableField;

import com.fund.iam.model.Message;

public class ItemChatViewModel {

    public ObservableField<String> messageContent = new ObservableField<>();

    public ItemChatViewModel(Message message) {
        messageContent.set(message.getMessage());
    }
}
