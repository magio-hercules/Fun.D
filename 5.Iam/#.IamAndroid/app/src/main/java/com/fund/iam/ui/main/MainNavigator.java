package com.fund.iam.ui.main;

import androidx.annotation.IdRes;

import com.fund.iam.ui.base.BaseNavigator;

public interface MainNavigator extends BaseNavigator {
    void actionNavigate(@IdRes int resourceId);
}
