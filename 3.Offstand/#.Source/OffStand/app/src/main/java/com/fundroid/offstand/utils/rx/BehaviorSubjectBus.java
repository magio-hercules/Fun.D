package com.fundroid.offstand.utils.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BehaviorSubjectBus {

    private static BehaviorSubjectBus behaviorSubjectBus;
    private final BehaviorSubject<Object> behaviorSubject;

    private BehaviorSubjectBus() {
        behaviorSubject = BehaviorSubject.create();
    }

    public static BehaviorSubjectBus getInstance() {
        if (behaviorSubjectBus == null) {
            behaviorSubjectBus = new BehaviorSubjectBus();
        }
        return behaviorSubjectBus;
    }

    public void sendEvent(Object object) {
        behaviorSubject.onNext(object);
    }

    public <T> Observable getEvents(Class<T> tClass) {
        return behaviorSubject.ofType(tClass);
    }

}
