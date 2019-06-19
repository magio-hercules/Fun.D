package com.fundroid.offstand.ui.lobby.findroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.fundroid.offstand.data.model.Room;

import java.util.List;

public class ItemRoomDetails extends ItemDetailsLookup.ItemDetails {

    private final int adapterPosition;
    private final Room selectionKey;

    public ItemRoomDetails(int adapterPosition, Room selectionKey) {
        this.adapterPosition = adapterPosition;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return selectionKey;
    }
}
