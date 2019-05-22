package com.fundroid.offstand.utils.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ClientPublishSubjectBus {

    private static ClientPublishSubjectBus clientPublishSubjectBus;
    private final PublishSubject<Object> publishSubject;

    private ClientPublishSubjectBus() {
        publishSubject = PublishSubject.create();
    }

    public static ClientPublishSubjectBus getInstance() {
        if(clientPublishSubjectBus == null) {
            clientPublishSubjectBus = new ClientPublishSubjectBus();
        }
        return clientPublishSubjectBus;
    }

    public void sendEvent(Object object) {
        publishSubject.onNext(object);
    }

    public <T> Observable getEvents(Class<T> tClass) {
        return publishSubject.ofType(tClass);
    }

}
