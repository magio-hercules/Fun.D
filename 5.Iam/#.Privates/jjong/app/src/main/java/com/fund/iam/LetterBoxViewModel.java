package com.fund.iam;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.fund.iam.model.LetterBox;

import java.util.ArrayList;

import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;

public class LetterBoxViewModel extends ViewModel {
    private static final String TAG = "lsc";

    public LetterBoxNavigator navigator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LetterBoxViewModel() {
        Log.d(TAG, "ChatListViewModel constructor");
        subscribeEvent();
    }

    private void subscribeEvent() {
//        dummyLetterBoxes();
    }

    public void dummyLetterBoxes() {
        LetterBox dummyLetterBox1 = new LetterBox();
        LetterBox dummyLetterBox2 = new LetterBox();
        LetterBox dummyLetterBox3 = new LetterBox();
        ArrayList<LetterBox> dummyLetterBoxes = new ArrayList<>();
        dummyLetterBoxes.add(dummyLetterBox1);
        dummyLetterBoxes.add(dummyLetterBox2);
        dummyLetterBoxes.add(dummyLetterBox3);
        compositeDisposable.add(Maybe.just(dummyLetterBoxes).subscribe(letterBoxes -> {
            Log.d("lsc","letterBoxes 1 " + letterBoxes.size());
            Log.d("lsc","letterBoxes 2 " + navigator);
            navigator.onRepositoriesChanged(letterBoxes);
        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ChatListViewModel onCleared");
        compositeDisposable.clear();
    }

    public void setNavigator(LetterBoxNavigator navigator) {
        this.navigator = navigator;
    }
}
