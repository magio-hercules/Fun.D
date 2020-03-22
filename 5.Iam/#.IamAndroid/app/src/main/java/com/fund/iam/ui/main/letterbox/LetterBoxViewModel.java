package com.fund.iam.ui.main.letterbox;


import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

import java.util.ArrayList;

import io.reactivex.Maybe;

public class LetterBoxViewModel extends BaseViewModel<LetterBoxNavigator> {


    public LetterBoxViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }

    public void dummyLetterBoxes() {
        LetterBox dummyLetterBox1 = new LetterBox();
        LetterBox dummyLetterBox2 = new LetterBox();
        LetterBox dummyLetterBox3 = new LetterBox();
        ArrayList<LetterBox> dummyLetterBoxes = new ArrayList<>();
        dummyLetterBoxes.add(dummyLetterBox1);
        dummyLetterBoxes.add(dummyLetterBox2);
        dummyLetterBoxes.add(dummyLetterBox3);
        getCompositeDisposable().add(Maybe.just(dummyLetterBoxes).subscribe(letterBoxes -> {
            getNavigator().onRepositoriesChanged(letterBoxes);
        }));
    }

}
