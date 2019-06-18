package com.fundroid.offstand.ui.lobby.findroom;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.fundroid.offstand.data.model.Room;

public class ItemRoomViewModel {

    private Context context;

    public final ObservableField<String> name;


    private final Room room;

    public ItemRoomViewModel(Context context, Room room) {
        this.context = context;
        this.room = room;
        name = new ObservableField<>(room.getName());

    }

    public void onItemClick() {
        Log.d("lsc", "ItemRoomViewModel onItemClick " + room.getName() + ", " + room.getAddress());
    }

}
