package com.fundroid.offstand.utils.rx;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

public class ReplaySubjectBus {

    private static ReplaySubjectBus replaySubjectBus;
    private final ReplaySubject<Object> replaySubject;

    private ReplaySubjectBus() {
        replaySubject = ReplaySubject.create();
    }

    public static ReplaySubjectBus getInstance() {
        if (replaySubjectBus == null) {
            replaySubjectBus = new ReplaySubjectBus();
        }
        return replaySubjectBus;
    }

    public void sendEvent(Object object) {
        replaySubject.onNext(object);
    }

    public <T> Observable getEvents(Class<T> tClass) {
//        Log.d("lsc","ReplaySubjectBus getEvents " + replaySubject.ofType(tClass));
        return replaySubject.ofType(tClass);
    }

}
