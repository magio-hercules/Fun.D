package com.fund.iam.ui.main.more.setting.contact_us;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.navigation.Navigation;


import com.fund.iam.data.model.User;
import com.fund.iam.ui.main.bookmark.BookmarkFragmentDirections;
import com.orhanobut.logger.Logger;

public class ItemContactViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<Integer> jobColor = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    private User letterBox;
    private Context context;
    private View view;

    public ItemContactViewModel(View view, Context context, User user) {
        Logger.d("ItemContactViewModel " + user);
        this.context = context;
        this.view = view;
        this.letterBox = user;
        imageUrl.set(user.getImageUrl());
        job.set(user.getJobList());
//        jobColor.set(Color.parseColor(user.getJobColor()));
        name.set(user.getUserName());
        email.set(user.getEmail());

    }


}
