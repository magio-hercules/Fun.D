package com.fundroid.offstand.utils.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PublishSubjectBus {

    private static PublishSubjectBus publishSubjectBus;
    private final PublishSubject<Object> publishSubject;

    private PublishSubjectBus() {
        publishSubject = PublishSubject.create();
    }

    public static PublishSubjectBus getInstance() {
        if(publishSubjectBus == null) {
            publishSubjectBus = new PublishSubjectBus();
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
