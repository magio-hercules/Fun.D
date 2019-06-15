package com.fundroid.offstand.ui.lobby;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.databinding.ItemRoomBinding;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private List<Room> rooms;

    public void setDatas(List<Room> rooms) {
        Log.d("lsc", "setDatas rooms " + rooms.size());
        RoomDiffCallback roomDiffCallback = new RoomDiffCallback(this.rooms, rooms);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(roomDiffCallback);
        this.rooms = rooms;
        diffResult.dispatchUpdatesTo(this);
    }

    public RoomAdapter() {
        this.rooms = Collections.emptyList();
    }

    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_room, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, int position) {
        holder.bindRepository(rooms.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemRoomBinding binding;

        ViewHolder(ItemRoomBinding binding) {
            super(binding.itemWifi);
            this.binding = binding;
        }

        void bindRepository(Room room) {
            if (binding.getRoom() == null) {
                binding.setRoom(room);
            } else {
                binding.getRoom();

            }

        }

    }

    class RoomDiffCallback extends DiffUtil.Callback {

        private final List<Room> oldRooms, newRooms;

        RoomDiffCallback(List<Room> oldRooms, List<Room> newRooms) {
            this.oldRooms = oldRooms;
            this.newRooms = newRooms;
        }

        @Override
        public int getOldListSize() {
            return oldRooms.size();
        }

        @Override
        public int getNewListSize() {
            return newRooms.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldRooms.get(oldItemPosition).equals(newRooms.get(newItemPosition));
        }
    }
}
