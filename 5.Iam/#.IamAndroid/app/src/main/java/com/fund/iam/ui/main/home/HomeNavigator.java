package com.fund.iam.ui.main.home;

import com.fund.iam.data.model.User;
import com.fund.iam.ui.base.BaseNavigator;

public interface HomeNavigator extends BaseNavigator {
    
    void goBack();

    void startLetterActivity();

    void updateUser();
    void updatePortfolio();
}
