package com.leesc.tazza.ui.lobby;


import com.leesc.tazza.data.model.Room;

import java.util.List;

public interface LobbyNavigator {

    void goToRoomInfoActivity();

    void goToSettingActivity();

    void onRepositoriesChanged(List<Room> rooms);

    void showToast(String message);

    void handleError(Throwable throwable);
}
