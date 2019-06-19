package com.fundroid.offstand.ui.lobby.findroom;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fundroid.offstand.data.model.Room;

import java.util.List;

public class ItemRoomLookup extends ItemDetailsLookup {


    private final RecyclerView recyclerView;

    public ItemRoomLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
//        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
//        if (view != null) {
//            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
//            if (viewHolder instanceof RoomAdapter.HistoryViewHolder) {
//                return ((RoomAdapter.HistoryViewHolder) viewHolder).ge();
//            }
//        }
        return null;
    }
}
