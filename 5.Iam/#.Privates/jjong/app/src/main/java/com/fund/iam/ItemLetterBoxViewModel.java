package com.fund.iam;

import androidx.databinding.ObservableField;

import com.fund.iam.model.LetterBox;
import com.fund.iam.model.Message;

public class ItemLetterBoxViewModel {

    public ObservableField<String> job = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();

    public ItemLetterBoxViewModel(LetterBox letterBox) {
        job.set("디자이너");
        name.set("이승철");
        email.set("slee8789@naver.com");
    }
}
