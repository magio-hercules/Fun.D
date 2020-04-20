package com.fund.iam.ui.letter;

import com.fund.iam.data.model.Letter;
import com.fund.iam.ui.base.BaseNavigator;

import java.util.List;

public interface LetterNavigator extends BaseNavigator {

    void onLetterSet(List<Letter> letters);

    void onLetterAdd(Letter letter);

}
