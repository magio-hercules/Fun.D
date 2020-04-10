package com.fund.iam;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.databinding.ItemLocalChatBinding;
import com.fund.iam.databinding.ItemRemoteChatBinding;
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

    public void addItem(Message message) {
        Log.d("lsc","addItem " + message);
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public void clearItems() {
        messages.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LOCAL:
                ItemLocalChatBinding itemLocalChatBinding = ItemLocalChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ChatViewHolder(itemLocalChatBinding);

            case VIEW_TYPE_REMOTE:
                ItemRemoteChatBinding itemRemoteChatBinding = ItemRemoteChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ChatViewHolder(itemRemoteChatBinding);

            default:
//                ItemEmptyViewBinding emptyViewBinding = ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//                return new ItemEmptyViewHolder(emptyViewBinding);
                //Todo: 비었을 때 뷰 만들기.
                return null;

        }
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

        private ItemLocalChatBinding localChatBinding;

        private ItemRemoteChatBinding remoteChatBinding;

        public ChatViewHolder(ItemLocalChatBinding localChatBinding) {
            super(localChatBinding.getRoot());
            this.localChatBinding = localChatBinding;
        }

        public ChatViewHolder(ItemRemoteChatBinding remoteChatBinding) {
            super(remoteChatBinding.getRoot());
            this.remoteChatBinding = remoteChatBinding;
        }

        public void onBind(int position) {
            final Message message = messages.get(position);
            ItemChatViewModel itemChatViewModel = new ItemChatViewModel(message);
            switch (getItemViewType()) {
                case VIEW_TYPE_LOCAL:
                    localChatBinding.setViewModel(itemChatViewModel);
                    break;
                case VIEW_TYPE_REMOTE:
                    remoteChatBinding.setViewModel(itemChatViewModel);
                    break;
            }
        }
    }
}
