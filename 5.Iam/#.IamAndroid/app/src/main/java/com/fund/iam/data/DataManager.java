package com.fund.iam.data;

import com.fund.iam.data.local.prefs.PreferencesHelper;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.remote.ApiHelper;

import java.util.List;

public interface DataManager extends PreferencesHelper, ApiHelper/*, DbHelper*/ {

    void setJobs(List<Job> jobs);

    void setLocations(List<Location> locations);

    List<Job> getJobs();

    List<Location> getLocations();
}
