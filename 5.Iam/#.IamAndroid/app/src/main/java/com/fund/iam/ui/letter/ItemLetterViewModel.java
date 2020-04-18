package com.fund.iam.ui.letter;


import androidx.databinding.ObservableField;

import com.fund.iam.data.model.Letter;
import com.orhanobut.logger.Logger;

public class ItemLetterViewModel {

    public ObservableField<String> letterMessage = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();

    public ItemLetterViewModel(Letter letter) {
        Logger.d("ItemLetterViewModel " + letter);
        letterMessage.set(letter.getMessage());
        imageUrl.set(letter.getImageUrl());
    }

}
