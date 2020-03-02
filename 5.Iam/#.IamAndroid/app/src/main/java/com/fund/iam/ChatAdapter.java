package com.fund.iam;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.databinding.ItemChatBinding;
import com.fund.iam.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_LOCAL = 0;
    public static final int VIEW_TYPE_REMOTE = 1;

    private List<Message> messages = new ArrayList<>();

    public void addItems(List<Message> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void clearItems() {
        messages.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding itemChatBinding = ItemChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatViewHolder(itemChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getType() == VIEW_TYPE_LOCAL) {
            return VIEW_TYPE_LOCAL;
        } else if (messages.get(position).getType() == VIEW_TYPE_REMOTE) {
            return VIEW_TYPE_REMOTE;
        } else {
            Log.e("lsc", "not defined type");
            return -1;
        }
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private ItemChatBinding binding;

        public ChatViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            final Message message = messages.get(position);
            ItemChatViewModel itemChatViewModel = new ItemChatViewModel(message);
            binding.setViewModel(itemChatViewModel);
        }
    }
}
