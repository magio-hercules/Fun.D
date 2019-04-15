package com.fundroid.offstand.ui.lobby;

import com.fundroid.offstand.data.model.Room;

import java.util.List;

public interface LobbyNavigator {

    void goToSettingActivity();

    void onRepositoriesChanged(List<Room> rooms);

    void showToast(String message);

    void handleError(Throwable throwable);
}
