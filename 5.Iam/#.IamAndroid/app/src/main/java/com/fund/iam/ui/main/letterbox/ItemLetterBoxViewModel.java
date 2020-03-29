package com.fund.iam.ui.main.letterbox;

import androidx.databinding.ObservableField;

import com.fund.iam.data.model.LetterBox;
import com.orhanobut.logger.Logger;

public class ItemLetterBoxViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();

    public ItemLetterBoxViewModel(LetterBox letterBox) {
        Logger.d("ItemLetterBoxViewModel " + letterBox);
        job.set("디자이너");
        job.set(letterBox.getJob());
        name.set("이승철");
        email.set("slee8789@naver.com");

    }
}
