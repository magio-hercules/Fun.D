package com.fundroid.offstand.ui.lobby.findroom;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

public class ItemRoomViewModel {

    private Context context;

    public final ObservableField<String> name;
    public final ObservableBoolean isChecked = new ObservableBoolean(false);
    private final Room room;

    public ItemRoomViewModel(Context context, Room room) {
        this.context = context;
        this.room = room;
        name = new ObservableField<>(room.getName());

    }

    public void onItemClick() {
        Log.d("lsc", "ItemRoomViewModel onItemClick " + room.getName() + ", " + room.getAddress() + ", " + isChecked.get());
//        isChecked.set(!isChecked.get());
        ClientPublishSubjectBus.getInstance().sendEvent(room);

    }

}
