package com.fund.iam.ui.main.more.setting.contact_us;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.data.model.User;
import com.fund.iam.databinding.ItemContactUsBinding;
import com.fund.iam.ui.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<User> contacts;
    private Context context;

    public ContactAdapter(List<User> contacts) {
        this.contacts = contacts;
    }

    public void addItems(List<User> letters) {
        this.contacts.addAll(letters);
        notifyDataSetChanged();
    }

    public void addItem(User message) {
        this.contacts.add(message);
        notifyDataSetChanged();
    }

    public void clearItems() {
        contacts.clear();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Logger.d("onCreateViewHolder viewType " + viewType);
        context = parent.getContext();
        ItemContactUsBinding itemContactUsBinding = ItemContactUsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(itemContactUsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Logger.d("onBindViewHolder " + position);
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends BaseViewHolder {

        private ItemContactUsBinding letterLocalBinding;

        public ContactViewHolder(ItemContactUsBinding binding) {
            super(binding.getRoot());
            this.letterLocalBinding = binding;
        }


        @Override
        public void onBind(int position) {
            final User user = contacts.get(position);
            ItemContactViewModel letterBoxViewModel = new ItemContactViewModel(letterLocalBinding.getRoot(), context, user);
            letterLocalBinding.setViewModel(letterBoxViewModel);
        }



    }


}