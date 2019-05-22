package com.fundroid.offstand.utils.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ServerPublishSubjectBus {

    private static ServerPublishSubjectBus publishSubjectBus;
    private final PublishSubject<Object> publishSubject;

    private ServerPublishSubjectBus() {
        publishSubject = PublishSubject.create();
    }

    public static ServerPublishSubjectBus getInstance() {
        if(publishSubjectBus == null) {
            publishSubjectBus = new ServerPublishSubjectBus();
        }
        return publishSubjectBus;
    }

    public void sendEvent(Object object) {
        publishSubject.onNext(object);
    }

    public <T> Observable getEvents(Class<T> tClass) {
        return publishSubject.ofType(tClass);
    }

}
