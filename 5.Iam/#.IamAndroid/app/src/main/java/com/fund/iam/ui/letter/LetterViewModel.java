package com.fund.iam.ui.letter;


import androidx.databinding.ObservableField;

import com.annimon.stream.Stream;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.Notification;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.VersionPage;
import com.fund.iam.data.model.request.PushBody;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import retrofit2.Response;

public class LetterViewModel extends BaseViewModel<LetterNavigator> {

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> input = new ObservableField<>();
    private PushBody pushBody = new PushBody();
    private Letter localLetter = new Letter();
    private Notification notification = new Notification();
    private User remoteUser;

    public LetterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();
    }

    private void subscribeEvent() {
        getCompositeDisposable().add(LetterBoxBus.getInstance().getLetterBox().subscribe(
                letterBox -> {
                    Logger.d(letterBox);
                    if(letterBox.getUser().getId() == getDataManager().getMyInfo().getId()) {
                        postMessage(letterBox.getUser().getId());
                    } else {
                        remoteUser = letterBox.getUser();
                        title.set(remoteUser.getUserName());
                    }
                }
        ));

        getCompositeDisposable().add(LetterBus.getInstance().getLetter()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(letter -> {
                    letter.setImageUrl(remoteUser.getImageUrl());
                    getNavigator().onLetterAdd(letter);
                }));
    }

    public void getInitialData() {
        getCompositeDisposable().add(Observable.combineLatest(
                getDataManager().postJobs().toObservable(),
                getDataManager().postLocations().toObservable(),
                getDataManager().postUserInfo(getDataManager().getAuthEmail(),
                        getDataManager().getAuthSnsType()).toObservable(),
                (jobs, locations, userInfo) -> {
                    getDataManager().setJobs(jobs.body());
                    getDataManager().setLocations(locations.body());
                    getDataManager().setMyInfo(userInfo.body().get(0));
                    return userInfo.body().get(0).getId();
                })
                .flatMapSingle(userId -> getDataManager().postPortfolios(userId))
                .subscribe(portfolios -> {
                    if (portfolios.isSuccessful()) {
                        getDataManager().setMyPortfolios(portfolios.body());
                    }
                }));

    }

    public void postMessage(int userId) {
        Logger.d("postMessage " + remoteUser);
        getCompositeDisposable().add(getDataManager().postMessage(userId, remoteUser.getId())
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    if (result.isSuccessful()) {
                        List<Letter> letters = result.body();
                        letters = Stream.of(letters).map(message -> {
                            message.setType(message.getMessageOwner() != 0 ? LetterType.LOCAL.getLetterType() : LetterType.REMOTE.getLetterType());
                            message.setImageUrl(message.getMessageOwner() != 0 ? null : remoteUser.getImageUrl());
                            return message;
                        }).toList();
                        getNavigator().onLetterSet(letters);
                    }

                }, onError -> getNavigator().handleError(onError)));
    }

    public void onSend() {
        if (input.get() == null || input.get().length() == 0) return;
        notification.setBody(input.get());
        pushBody.setTo(remoteUser.getToken());
        pushBody.setNotification(notification);
        pushBody.setPriority("high");
        localLetter.setUser(getDataManager().getMyInfo());
        localLetter.setFriend(remoteUser);
        localLetter.setMessage(input.get());
        pushBody.setData(localLetter);
        getCompositeDisposable().add(
                getDataManager().postMessageInsert(getDataManager().getMyInfo().getId(), remoteUser.getId(), input.get())
                        .flatMap(result -> getDataManager().postFcmSend(pushBody))
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("result " + result.isSuccessful());
                            if (result.isSuccessful()) {
                                getNavigator().onLetterAdd(new Letter(LetterType.LOCAL.getLetterType(), input.get()));
                                input.set("");
                            }
                        }, onError -> getNavigator().handleError(onError)));

    }
}
