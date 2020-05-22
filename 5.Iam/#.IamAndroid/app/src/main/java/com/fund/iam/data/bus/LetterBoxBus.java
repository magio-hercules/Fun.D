package com.fund.iam.data.bus;

import com.fund.iam.data.model.LetterBox;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LetterBoxBus {

    private static LetterBoxBus letterBoxBus;
    private final BehaviorSubject<LetterBox> behaviorSubject;

    private LetterBoxBus() {
        behaviorSubject = BehaviorSubject.create();
    }

    public static LetterBoxBus getInstance() {
        if (letterBoxBus == null) {
            letterBoxBus = new LetterBoxBus();
        }
        return letterBoxBus;
    }

    public void sendLetterBox(LetterBox letter) {
        behaviorSubject.onNext(letter);
    }

    public Observable<LetterBox> getLetterBox() {
        return behaviorSubject.ofType(LetterBox.class);
    }

}
