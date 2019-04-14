package com.fundroid.offstand.ui.lobby.roominfo;


public interface RoomInfoNavigator {

    void showToast(String message);

    void handleError(Throwable throwable);

    void dismissDialog();
}
