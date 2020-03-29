package com.fund.iam.ui.main.more;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.databinding.ItemMoreBinding;
import com.fund.iam.ui.base.BaseViewHolder;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;

    private List<String> titles;

    public MoreAdapter(List<String> titles) {
        this.titles = titles;
    }

    public void addItems(List<String> letters) {
        this.titles.addAll(letters);
        notifyDataSetChanged();
    }

    public void addItem(String message) {
        this.titles.add(message);
        notifyDataSetChanged();
    }

    public void clearItems() {
        titles.clear();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemMoreBinding itemMoreBinding = ItemMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MoreViewHolder(itemMoreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MoreViewHolder extends BaseViewHolder {

        private ItemMoreBinding itemMoreBinding;

        public MoreViewHolder(ItemMoreBinding binding) {
            super(binding.getRoot());
            this.itemMoreBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final String title = titles.get(position);
            ItemMoreViewModel moreViewModel = new ItemMoreViewModel(mContext, title);
            itemMoreBinding.setViewModel(moreViewModel);
        }
    }
}