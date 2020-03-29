package com.fund.iam.ui.main.more.notice;

import com.fund.iam.data.model.Notice;
import com.fund.iam.ui.base.BaseNavigator;

import java.util.List;

public interface NoticeNavigator extends BaseNavigator {

    void onRepositoriesChanged(List<Notice> notices);
    
    void goBack();
}
