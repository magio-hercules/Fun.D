package com.fundroid.offstand.ui.lobby.wifialert;


import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class WifiAlertViewModel extends BaseViewModel<WifiAlertNavigator> {

    public WifiAlertViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        subscribeEvent();
    }

    public void onCancelClick() {
        getNavigator().dismissDialog();
    }

    private void subscribeEvent() {

    }

    public void onConfirmClick() {
        getNavigator().goToWifiSetting();
    }
}
