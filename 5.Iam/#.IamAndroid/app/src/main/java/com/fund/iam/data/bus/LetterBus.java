package com.fund.iam.data.bus;

import com.fund.iam.data.model.Letter;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class LetterBus {

    private static LetterBus letterBus;
    private final PublishSubject<Letter> publishSubject;
    private final BehaviorSubject<Integer> badgeSubject;

    private LetterBus() {
        publishSubject = PublishSubject.create();
        badgeSubject = BehaviorSubject.create();
    }

    public static LetterBus getInstance() {
        if (letterBus == null) {
            letterBus = new LetterBus();
        }
        return letterBus;
    }

    public void sendLetter(Letter letter) {
        publishSubject.onNext(letter);
    }

    public Observable<Letter> getLetter() {
        return publishSubject.ofType(Letter.class);
    }

    public void sendBadge(int userId) {
        badgeSubject.onNext(userId);
    }

    public Observable<Integer> getBadge() {
        return badgeSubject.ofType(Integer.class);
    }

}
