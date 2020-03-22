package com.fund.iam.ui.main.letterbox;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.data.enums.LetterType;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.databinding.ItemEmptyBinding;
import com.fund.iam.databinding.ItemLetterLocalBinding;
import com.fund.iam.databinding.ItemLetterRemoteBinding;
import com.fund.iam.databinding.ItemLetterboxBinding;
import com.fund.iam.ui.base.BaseViewHolder;
import com.fund.iam.ui.letter.ItemLetterViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.telecom.DisconnectCause.LOCAL;
import static android.telecom.DisconnectCause.REMOTE;
import static com.fund.iam.ui.base.BaseViewHolder.VIEW_TYPE_EMPTY;
import static com.fund.iam.ui.base.BaseViewHolder.VIEW_TYPE_NORMAL;

public class LetterBoxAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<LetterBox> letterBoxes;

    public LetterBoxAdapter(List<LetterBox> letterBoxes) {
        this.letterBoxes = letterBoxes;
    }

    public void addItems(List<LetterBox> letters) {
        this.letterBoxes.addAll(letters);
        notifyDataSetChanged();
    }

    public void addItem(LetterBox message) {
        this.letterBoxes.add(message);
        notifyDataSetChanged();
    }

    public void clearItems() {
        letterBoxes.clear();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Logger.d("onCreateViewHolder viewType " + viewType);
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemLetterboxBinding itemNoticeBinding = ItemLetterboxBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new LetterBoxViewHolder(itemNoticeBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemEmptyBinding emptyViewBinding = ItemEmptyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ItemEmptyViewHolder(emptyViewBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Logger.d("onBindViewHolder " + position);
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (letterBoxes != null && letterBoxes.size() > 0) {
            Logger.d("getItemCount " + letterBoxes.size());
            return letterBoxes.size();
        } else {
            Logger.d("getItemCount empty");
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (letterBoxes != null && !letterBoxes.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }


    public class LetterBoxViewHolder extends BaseViewHolder {

        private ItemLetterboxBinding letterLocalBinding;

        public LetterBoxViewHolder(ItemLetterboxBinding binding) {
            super(binding.getRoot());
            this.letterLocalBinding = binding;
        }


        @Override
        public void onBind(int position) {
            final LetterBox letterBox = letterBoxes.get(position);
            ItemLetterBoxViewModel letterBoxViewModel = new ItemLetterBoxViewModel(letterBox);
            switch (getItemViewType()) {
                case VIEW_TYPE_NORMAL:
                    letterLocalBinding.setViewModel(letterBoxViewModel);
                    break;
                case VIEW_TYPE_EMPTY:
//                    ItemEmptyBinding.setViewModel(letterBoxViewModel);
                    break;
            }

        }
    }

    public class ItemEmptyViewHolder extends BaseViewHolder {

        private ItemEmptyBinding binding;

        public ItemEmptyViewHolder(ItemEmptyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onBind(int position) {
//            ItemEmptyViewModel emptyViewModel = new ItemEmptyViewModel();
//            binding.setViewModel(emptyViewModel);
        }
    }
}