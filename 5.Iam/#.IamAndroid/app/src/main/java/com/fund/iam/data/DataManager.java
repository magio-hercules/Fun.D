package com.fund.iam.data;

import com.fund.iam.data.local.prefs.PreferencesHelper;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.remote.ApiHelper;

import java.util.List;

public interface DataManager extends PreferencesHelper, ApiHelper/*, DbHelper*/ {

    void setMarketVersion(String version);

    String getMarketVersion();

    void setMyInfo(User user);

    void setJobs(List<Job> jobs);

    void setLocations(List<Location> locations);

    void setMyPortfolios(List<Portfolio> portfolios);

    User getMyInfo();

    List<Job> getJobs();

    List<Location> getLocations();

    List<Portfolio> getMyPortfolios();



}
