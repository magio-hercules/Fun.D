package com.leesc.tazza.ui.main;


import com.leesc.tazza.data.model.Room;

import java.util.List;

public interface MainNavigator {

    void onRepositoriesChanged(List<Room> rooms);

    void handleError(Throwable throwable);
}
