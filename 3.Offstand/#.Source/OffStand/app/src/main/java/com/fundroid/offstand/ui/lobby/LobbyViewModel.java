package com.fundroid.offstand.ui.lobby;

import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class LobbyViewModel extends BaseViewModel<LobbyNavigator> {

    private ResourceProvider resourceProvider;

    public LobbyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        Log.v("lsc","SplashViewModel Constructor");
        this.resourceProvider = resourceProvider;


    }

    public void goToSetting() {
        getNavigator().goToSettingActivity();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.v("lsc", "SplashViewModel onCleared");
    }
}
