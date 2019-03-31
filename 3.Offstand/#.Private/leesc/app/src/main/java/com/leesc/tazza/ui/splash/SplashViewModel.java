package com.leesc.tazza.ui.splash;


import android.util.Log;

import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import pl.droidsonroids.gif.GifDrawable;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        Log.d("lsc", "SplashViewModel constructor");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "SplashViewModel onCleared");
    }
}
