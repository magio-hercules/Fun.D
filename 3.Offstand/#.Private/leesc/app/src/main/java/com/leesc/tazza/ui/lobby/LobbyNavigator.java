package com.leesc.tazza.ui.lobby;


import com.leesc.tazza.data.model.Room;

import java.util.List;

public interface LobbyNavigator {

    void goToRoomInfoActivity();

    void onRepositoriesChanged(List<Room> rooms);

    void handleError(Throwable throwable);
}
