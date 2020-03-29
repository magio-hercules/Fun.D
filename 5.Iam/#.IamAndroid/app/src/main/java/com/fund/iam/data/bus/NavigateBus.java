package com.fund.iam.data.bus;

import androidx.annotation.IdRes;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class NavigateBus {

    private static NavigateBus navigateBus;
    private final PublishSubject<Integer> publishSubject;

    private NavigateBus() {
        publishSubject = PublishSubject.create();
    }

    public static NavigateBus getInstance() {
        if (navigateBus == null) {
            navigateBus = new NavigateBus();
        }
        return navigateBus;
    }

    public void sendDestination(@IdRes int destinationId) {
        publishSubject.onNext(destinationId);
    }

    public Observable<Integer> getDestination() {
        return publishSubject.ofType(Integer.class);
    }

}
