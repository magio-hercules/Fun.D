package com.fund.iam.ui.main.search;

import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.base.BaseNavigator;

import java.util.List;

public interface SearchNavigator extends BaseNavigator {
    
    void goBack();
    void updateChannels(List<Channel> channels);
    void updateUsers(List<User> users, List<Job> jobs);
    void updateJobs();
    void updateLocations();
}
