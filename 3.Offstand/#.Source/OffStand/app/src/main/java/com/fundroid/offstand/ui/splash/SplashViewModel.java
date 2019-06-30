package com.fundroid.offstand.ui.splash;

import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        Log.v("lsc","SplashViewModel Constructor");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.v("lsc", "SplashViewModel onCleared");
    }
}
