package com.fund.iam.ui.main.home;

import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.base.BaseNavigator;

import java.util.List;

public interface HomeNavigator extends BaseNavigator {
    
    void goBack();

    void startLetterActivity();

    void updateUser();
    void updatePortfolio();
    void updateUser(User userInfo);
    void updatePortfolio(List<Portfolio> portfolioList);

    void insertProfile();
    void insertImage();
    void insertText();

    void onSuccess();
}
