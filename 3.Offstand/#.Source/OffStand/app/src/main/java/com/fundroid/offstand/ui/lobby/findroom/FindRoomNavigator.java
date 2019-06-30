package com.fundroid.offstand.ui.lobby.findroom;


public interface FindRoomNavigator {

    void goToRoomActivity();

    void showProgress();

    void dismissProgress();

    void goBack();

    void showToast(String message);
}
