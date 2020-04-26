package com.fund.iam.ui.letter;


import androidx.databinding.ObservableField;

import com.annimon.stream.Stream;
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

import java.util.List;

public class LetterViewModel extends BaseViewModel<LetterNavigator> {

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> input = new ObservableField<>();
    private PushBody pushBody = new PushBody();
    private Letter localLetter = new Letter();
    private int remoteUserId;
    private String remoteToken;
    private String remoteImageUrl;

    public LetterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();

    }

    private void subscribeEvent() {
        getCompositeDisposable().add(LetterBoxBus.getInstance().getLetterBox().subscribe(
                letterBox -> {
                    Logger.d(letterBox);
                    title.set(letterBox.getName());
                    remoteToken = letterBox.getToken();
                    remoteImageUrl = letterBox.getImageUrl();
                    remoteUserId = letterBox.getId();
                }
        ));

        getCompositeDisposable().add(LetterBus.getInstance().getLetter()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(letter -> {
                    letter.setImageUrl(remoteImageUrl);
                    getNavigator().onLetterAdd(letter);
                }));

        getCompositeDisposable().add(getDataManager().postMessage(getDataManager().getMyInfo().getId(), remoteUserId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    if (result.isSuccessful()) {
                        List<Letter> letters = result.body();
                        letters = Stream.of(letters).map(message -> {
                            message.setType(message.getMessageOwner() != 0 ? LetterType.LOCAL.getLetterType() : LetterType.REMOTE.getLetterType());
                            message.setImageUrl(message.getMessageOwner() != 0 ? null : remoteImageUrl);
                            return message;
                        }).toList();
                        getNavigator().onLetterSet(letters);
                    }

                }));
    }

    public void onSend() {
        if (input.get() == null || input.get().length() == 0) return;
        pushBody.setTo(remoteToken);
        pushBody.setPriority("high");
        localLetter.setMessage(input.get());
        pushBody.setData(localLetter);
        getCompositeDisposable().add(
                getDataManager().postMessageInsert(getDataManager().getMyInfo().getId(), remoteUserId, input.get())
                        .flatMap(result -> getDataManager().postFcmSend(pushBody))
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
