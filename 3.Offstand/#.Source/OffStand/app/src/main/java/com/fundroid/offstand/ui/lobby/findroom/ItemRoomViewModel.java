package com.fundroid.offstand.ui.lobby.findroom;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

public class ItemRoomViewModel {

    private Context context;

    public final ObservableField<String> name;
    public final ObservableField<Integer> backgroundColor = new ObservableField();
    private final Room room;

    public ItemRoomViewModel(Context context, Room room) {
        this.context = context;
        this.room = room;
        name = new ObservableField<>(room.getName());
        if (room.isSelected()) {
            backgroundColor.set(ContextCompat.getColor(context, R.color.holo_green_light));
        } else {
            backgroundColor.set(ContextCompat.getColor(context, R.color.white));
        }

    }

    public void onItemClick() {
        Log.d("lsc", "ItemRoomViewModel onItemClick " + room.getName() + ", " + room.getAddress() + ", " + room.getId());
//        isChecked.set(!isChecked.get());
        ClientPublishSubjectBus.getInstance().sendEvent(room);

    }

}
