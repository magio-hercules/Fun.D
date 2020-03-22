package com.fund.iam.data.bus;

import com.fund.iam.data.model.Letter;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LetterBus {

    private static LetterBus letterBus;
    private final PublishSubject<Letter> publishSubject;

    private LetterBus() {
        publishSubject = PublishSubject.create();
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

}
