package com.fund.iam.ui.main.letterbox;

import android.content.Context;

import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.ui.letter.LetterActivity;
import com.orhanobut.logger.Logger;

public class ItemLetterBoxViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    private LetterBox letterBox;
    private Context context;

    public ItemLetterBoxViewModel(Context context, LetterBox letterBox) {
        Logger.d("ItemLetterBoxViewModel " + letterBox);
        this.context = context;
        this.letterBox = letterBox;
        imageUrl.set(letterBox.getImageUrl());
        job.set(letterBox.getJob());
        name.set(letterBox.getName());
        email.set(letterBox.getEmail());

    }

    public void onItemClick() {
        LetterBoxBus.getInstance().sendLetterBox(letterBox);
        LetterActivity.start(context);
    }
}
