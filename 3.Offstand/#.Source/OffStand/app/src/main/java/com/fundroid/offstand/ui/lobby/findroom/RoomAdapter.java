package com.fundroid.offstand.ui.lobby.findroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.databinding.ItemRoomBinding;
import com.fundroid.offstand.ui.base.BaseViewHolder;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Room> rooms;

    private Context context;

    public RoomAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemRoomBinding itemRoomBinding = ItemRoomBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(itemRoomBinding);
    }

    public void addItems(List<Room> rooms) {
        this.rooms.addAll(rooms);
        notifyDataSetChanged();
    }

    public void clearItems() {
        rooms.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class HistoryViewHolder extends BaseViewHolder {

        private ItemRoomBinding binding;

        public HistoryViewHolder(ItemRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onBind(int position) {
            final Room room = rooms.get(position);
            ItemRoomViewModel itemRoomViewModel = new ItemRoomViewModel(context, room);
            binding.setRoom(itemRoomViewModel);
            binding.executePendingBindings();
        }
    }

}
