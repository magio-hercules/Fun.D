package com.fund.iam.data.bus;

import com.fund.iam.data.model.LetterBox;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class LetterBoxBus {

    private static LetterBoxBus letterBoxBus;
    private final BehaviorSubject<LetterBox> publishSubject;

    private LetterBoxBus() {
        publishSubject = BehaviorSubject.create();
    }

    public static LetterBoxBus getInstance() {
        if (letterBoxBus == null) {
            letterBoxBus = new LetterBoxBus();
        }
        return letterBoxBus;
    }

    public void sendLetterBox(LetterBox letter) {
        publishSubject.onNext(letter);
    }

    public Observable<LetterBox> getLetterBox() {
        return publishSubject.ofType(LetterBox.class);
    }

}
