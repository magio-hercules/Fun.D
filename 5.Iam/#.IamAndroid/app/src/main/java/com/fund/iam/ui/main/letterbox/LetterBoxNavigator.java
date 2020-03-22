package com.fund.iam.ui.main.letterbox;

import com.fund.iam.data.model.LetterBox;
import com.fund.iam.ui.base.BaseNavigator;

import java.util.List;

public interface LetterBoxNavigator extends BaseNavigator {

    void onRepositoriesChanged(List<LetterBox> letterBoxes);

    void goBack();

}
