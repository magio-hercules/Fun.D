package com.fund.iam;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MessageBus {

    private static MessageBus messageBus;
    private final PublishSubject<String> messagePublishBus;


    private MessageBus() {
        messagePublishBus = PublishSubject.create();
    }

    public static MessageBus getInstance() {
        if (messageBus == null) {
            messageBus = new MessageBus();
        }
        return messageBus;
    }


    public void sendMessageEvent(String message) {
        messagePublishBus.onNext(message);
    }

    public Observable<String> getMessageEvent() {
        return messagePublishBus.ofType(String.class);
    }
}
