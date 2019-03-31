package com.leesc.tazza.ui.roominfo;


import com.leesc.tazza.data.model.Room;

import java.util.List;

public interface RoomInfoNavigator {

    void showToast(String message);

    void handleError(Throwable throwable);
}
