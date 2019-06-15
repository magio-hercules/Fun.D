package com.fundroid.offstand.ui.lobby;

public interface LobbyNavigator {

    void goToSettingActivity();

    void showToast(String message);

    void handleError(Throwable throwable);
}
