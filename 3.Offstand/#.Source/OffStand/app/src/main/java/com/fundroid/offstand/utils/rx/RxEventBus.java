package com.fundroid.offstand.utils.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {

    private static RxEventBus rxEventBus;
    private final PublishSubject<Object> publishSubject;

    private RxEventBus() {
        publishSubject = PublishSubject.create();
    }

    public static RxEventBus getInstance() {
        if(rxEventBus == null) {
            rxEventBus = new RxEventBus();
        }
        return rxEventBus;
    }

    public void sendEvent(Object object) {
        publishSubject.onNext(object);
    }

    public Observable getEvents(Class tClass) {
        return publishSubject.ofType(tClass);
    }

}
