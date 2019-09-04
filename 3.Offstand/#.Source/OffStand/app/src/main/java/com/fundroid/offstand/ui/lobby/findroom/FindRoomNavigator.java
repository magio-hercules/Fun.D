package com.fundroid.offstand.ui.lobby.findroom;


import com.fundroid.offstand.ui.base.BaseNavigator;

public interface FindRoomNavigator extends BaseNavigator {

    void showWifiAlertDialog();

    void goToRoomActivity();

    void showProgress();

    void dismissProgress();

    void goBack();

    void showToast(String message);
}
