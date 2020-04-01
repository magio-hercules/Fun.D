package com.fund.iam.ui.letter;


import androidx.databinding.ObservableField;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.request.PushBody;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

public class LetterViewModel extends BaseViewModel<LetterNavigator> {

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> input = new ObservableField<>();
    private PushBody pushBody = new PushBody();
    private Letter localLetter = new Letter();

    public LetterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();

    }

    private void subscribeEvent() {
        getCompositeDisposable().add(LetterBoxBus.getInstance().getLetterBox().subscribe(
                letterBox -> {
                    Logger.d(letterBox);
                    title.set(letterBox.getName());
                }
        ));

        getCompositeDisposable().add(LetterBus.getInstance().getLetter()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(letter -> getNavigator().onLetterAdd(letter)));
    }

    public void onSend() {
        pushBody.setTo("dsnuJqHkS_e6YIDezKM0Fa:APA91bHQRzW6cLrxziJUA3JLvoiq255lh6cBuqI4TszhvvOEjqKnkVurmhoqma2R1_c4ni6jSN7YZBdu97K9Tugnd-Bjj-Nf4sXpdKyZaNjtQi_9dnLE7T-gsBAym0lkEbABc5MGoK7z");       // to J5
//        pushBody.setTo("f-KfKqLhQfi7MVEDbW7sJQ:APA91bGKoRd7t_R-TgEtWbDwYSQuNRdb0btgcHiZBqJ8S3rNmZP7ln1AKWBJa1yAtiVIy5HHcUjGGx7Y_xgrJoEZ9R7o4MPnUnNetdHKpSgozAGpu6oVDFfiTmFIL9aIEfc7NsgrBJ4p");       // to 갤10
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
