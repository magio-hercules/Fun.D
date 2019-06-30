package com.fundroid.offstand.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.VideoView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.ui.lobby.findroom.RoomAdapter;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter("onTouch")
    public static void onTouch(View self, View.OnTouchListener onTouchListener) {
        self.setOnTouchListener(onTouchListener);
    }

    @BindingAdapter("setZOrder")
    public static void setZOrder(VideoView videoView, boolean value) {
        Log.d("lsc","setZOrder " + value);
//        videoView.setZOrderOnTop(value);
        videoView.setZOrderOnTop(true);
    }

    @BindingAdapter({"setRoomAdapter"})
    public static void setRoomAdapter(RecyclerView recyclerView, List<Room> rooms) {
        RoomAdapter adapter = (RoomAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(rooms);
        }
    }

}
