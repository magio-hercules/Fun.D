package com.fund.iam;

import com.fund.iam.model.LetterBox;
import com.fund.iam.model.Message;

import java.util.List;


public interface LetterBoxNavigator {

    void onRepositoriesChanged(List<LetterBox> letterBoxes);

    void showToast(String message);

    void handleError(Throwable throwable);

}
