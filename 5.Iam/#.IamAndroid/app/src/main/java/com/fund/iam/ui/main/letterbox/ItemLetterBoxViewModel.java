package com.fund.iam.ui.main.letterbox;

import android.content.Context;
import android.graphics.Color;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.bus.LetterBus;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.orhanobut.logger.Logger;

public class ItemLetterBoxViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<Integer> jobColor = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    private LetterBox letterBox;
    private Context context;

    private LetterBoxListener mListener;

    public interface LetterBoxListener {
        void onRead();
    }

    public ItemLetterBoxViewModel(Context context, LetterBox letterBox, LetterBoxListener listener) {
        Logger.d("ItemLetterBoxViewModel " + letterBox);
        this.context = context;
        this.letterBox = letterBox;
        mListener = listener;
        imageUrl.set(letterBox.getImageUrl());
        job.set(letterBox.getJobTitle());
        jobColor.set(Color.parseColor(letterBox.getJobColor()));
        name.set(letterBox.getName());
        email.set(letterBox.getEmail());

    }

    public void onItemClick() {
        mListener.onRead();
        LetterBoxBus.getInstance().sendLetterBox(new LetterBox(new User(letterBox.getFriendId(),letterBox.getName(), letterBox.getImageUrl(), letterBox.getToken())));
        LetterActivity.start(context);
    }
}
