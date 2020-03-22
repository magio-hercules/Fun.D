package com.fund.iam.ui.letter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.databinding.ItemLetterLocalBinding;
import com.fund.iam.databinding.ItemLetterRemoteBinding;
import com.fund.iam.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Letter> letters = new ArrayList<>();

    public LetterAdapter(List<Letter> letters) {
        this.letters = letters;
    }

    public void addItems(List<Letter> letters) {
        this.letters.addAll(letters);
        notifyDataSetChanged();
    }

    public void addItem(Letter message) {
        this.letters.add(message);
        notifyDataSetChanged();
    }

    public void clearItems() {
        letters.clear();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                ItemLetterLocalBinding letterLocalBinding = ItemLetterLocalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new LetterViewHolder(letterLocalBinding);
            case 1:
            default:
                ItemLetterRemoteBinding letterRemoteBinding = ItemLetterRemoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new LetterViewHolder(letterRemoteBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return letters.size();
    }

    @Override
    public int getItemViewType(int position) {
        return letters.get(position).getType();
    }

    public class LetterViewHolder extends BaseViewHolder {

        private ItemLetterLocalBinding letterLocalBinding;
        private ItemLetterRemoteBinding letterRemoteBinding;

        public LetterViewHolder(ItemLetterLocalBinding binding) {
            super(binding.getRoot());
            this.letterLocalBinding = binding;
        }

        public LetterViewHolder(ItemLetterRemoteBinding binding) {
            super(binding.getRoot());
            this.letterRemoteBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final Letter letter = letters.get(position);
            ItemLetterViewModel letterViewModel = new ItemLetterViewModel(letter);
            switch (getItemViewType()) {
                case 0:
                    letterLocalBinding.setViewModel(letterViewModel);
                    break;
                case 1:
                    letterRemoteBinding.setViewModel(letterViewModel);
                    break;
            }

        }
    }
}