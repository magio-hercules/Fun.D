package com.leesc.tazza.ui.splash;


import android.util.Log;

import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        Log.d("lsc", "SplashViewModel constructor");
        getCompositeDisposable()
                .add(Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(getSchedulerProvider().ui())
//                        .subscribe(time -> getNavigator().startMainActivity()));
                        .subscribe(time -> getNavigator().startLobbyActivity()));


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "SplashViewModel onCleared");
    }
}
