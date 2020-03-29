package com.fund.iam.ui.letter;


import androidx.databinding.ObservableField;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.request.PushBody;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

public class LetterViewModel extends BaseViewModel<LetterNavigator> {

    public ObservableField<String> input = new ObservableField<>();
    private PushBody pushBody = new PushBody();
    private Letter localLetter = new Letter();

    public LetterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();

    }

    private void subscribeEvent() {
        getCompositeDisposable().add(LetterBus.getInstance().getLetter()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(letter -> getNavigator().onLetterAdd(letter)));
    }

    public void onSend() {
//        pushBody.setTo("ePT67k1HR9mXGvWCyfx1OG:APA91bF3Lj7YMgJSuhlcQQWQ-8W6PxaMfiR2s-vZsRPiiqXQB17_dDWL7Qdo0nFyr5Upri96LBRQRwvP2Hli4ZtB115_bYLUk3p9H3aET2eW2LDUKp92STW1WU5l8UIagJJfzR8CLwlb");       // to J600
        pushBody.setTo("fADEQ0JXSC2vP0RZMkwE1M:APA91bFNOl_92NN_dT-MpEqzrhRSSXdDh8l7Cz2IbxuEUnjv65Gx93jc1qg6vtEh5lx7doU_Yi_kfwr7_7BcI9M0DwD4INmDqLk_-eoBwxyq8U7Ka35ZjL38F5Xg9meToPf_j9EPhWQm");       // to 갤10
        pushBody.setPriority("high");
        localLetter.setMessage(input.get());
        pushBody.setData(localLetter);
        getCompositeDisposable().add(getDataManager().postFcmSend(pushBody)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    if (result.isSuccessful()) {
                        getNavigator().onLetterAdd(new Letter(LetterType.LOCAL.getLetterType(), input.get()));
                        input.set("");
                    }
                }));

    }
}
