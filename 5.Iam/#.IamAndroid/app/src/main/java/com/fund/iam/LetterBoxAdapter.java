package com.fund.iam;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.databinding.ItemLetterboxBinding;
import com.fund.iam.databinding.ItemLocalChatBinding;
import com.fund.iam.databinding.ItemRemoteChatBinding;
import com.fund.iam.model.LetterBox;

import java.util.ArrayList;
import java.util.List;

public class LetterBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LetterBox> letterBoxes = new ArrayList<>();

    public void addItems(List<LetterBox> messages) {
        this.letterBoxes.addAll(messages);
        notifyDataSetChanged();
    }

    public void clearItems() {
        letterBoxes.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLetterboxBinding itemLetterboxBinding = ItemLetterboxBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LetterBoxViewHolder(itemLetterboxBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((LetterBoxViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return letterBoxes.size();
    }

    public class LetterBoxViewHolder extends RecyclerView.ViewHolder {

        private ItemLetterboxBinding itemLetterboxBinding;

        public LetterBoxViewHolder(ItemLetterboxBinding itemLetterboxBinding) {
            super(itemLetterboxBinding.getRoot());
            this.itemLetterboxBinding = itemLetterboxBinding;
        }

        public void onBind(int position) {
            final LetterBox letterBox = letterBoxes.get(position);
            ItemLetterBoxViewModel itemLetterBoxViewModel = new ItemLetterBoxViewModel(letterBox);
            itemLetterboxBinding.setViewModel(itemLetterBoxViewModel);
        }
    }
}
