package com.fund.iam.ui.base;

public interface BaseNavigator {
    void showToast(String message);

    void handleError(Throwable throwable);
}
