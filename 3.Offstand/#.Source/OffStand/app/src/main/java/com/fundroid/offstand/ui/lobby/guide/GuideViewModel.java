package com.fundroid.offstand.ui.lobby.guide;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class GuideViewModel extends BaseViewModel<GuideNavigator> {

    public GuideViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc","GuideViewModel onCleared");
    }
}
