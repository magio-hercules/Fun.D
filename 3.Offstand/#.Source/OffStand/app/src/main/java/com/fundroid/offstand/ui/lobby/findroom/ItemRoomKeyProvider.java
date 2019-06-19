package com.fundroid.offstand.ui.lobby.findroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.fundroid.offstand.data.model.Room;

import java.util.List;

public class ItemRoomKeyProvider extends ItemKeyProvider {

    private final List<Room> roomList;

    public ItemRoomKeyProvider(int scope, List<Room> roomList) {
        super(scope);
        this.roomList = roomList;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return roomList.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return roomList.indexOf(key);
    }
}
