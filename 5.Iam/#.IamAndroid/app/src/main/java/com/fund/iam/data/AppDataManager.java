
package com.fund.iam.data;

import com.fund.iam.data.local.prefs.PreferencesHelper;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.Notice;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.request.LoginBody;
import com.fund.iam.data.model.request.PushBody;
import com.fund.iam.data.remote.ApiHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

@Singleton
public class AppDataManager implements DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mAwsApiHelper;
    private final ApiHelper mFirebaseApiHelper;

    private List<Job> jobs;
    private List<Location> locations;


    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, @Named("aws") ApiHelper awsApiHelper, @Named("firebase") ApiHelper firebaseApiHelper) {
        mPreferencesHelper = preferencesHelper;
        mAwsApiHelper = awsApiHelper;
        mFirebaseApiHelper = firebaseApiHelper;
    }

    @Override
    public Single<Response<Void>> postFcmSend(PushBody pushBody) {
        return mFirebaseApiHelper.postFcmSend(pushBody);
    }

    @Override
    public Single<Response<List<User>>> postUsers(int userId) {
        return mAwsApiHelper.postUsers(userId);
    }

    @Override
    public Single<Response<List<Portfolio>>> postPortfolios(int userId) {
        return mAwsApiHelper.postPortfolios(userId);
    }

    @Override
    public Single<Response<Void>> postLogin(LoginBody loginBody) {
        return mAwsApiHelper.postLogin(loginBody);
    }

    @Override
    public Single<Response<User>> postVerifyToken(String token) {
        return mAwsApiHelper.postVerifyToken(token);
    }

    // 전체채널 조회
    @Override
    public Single<Response<List<Channel>>> postChannels() {
        return mAwsApiHelper.postChannels();
    }

    // 전체유저 조회
    @Override
    public Single<Response<List<User>>> postUsersAll() {
        return mAwsApiHelper.postUsersAll();
    }

    // 신규채널 생성
    @Override
    public Single<Response<Channel>> postCreateChannel(int ownerId, String name, String purpose, String location, String description, String password) {
        return mAwsApiHelper.postCreateChannel(ownerId, name, purpose, location, description, password);
    }

    // 특정채널 조회
    @Override
    public Single<Response<List<Channel>>> postChannel(int id) {
        return mAwsApiHelper.postChannel(id);
    }

    @Override
    public Single<Response<Job>> postJobInfo(int jobId) {
        return mAwsApiHelper.postJobInfo(jobId);
    }

    @Override
    public Single<Response<Void>> postInsertPortfolio(int user_id, int type, String text) {
        return mAwsApiHelper.postInsertPortfolio(user_id, type, text);
    }

    @Override
    public Single<Response<Void>> postDeletePortfolio(int id) {
        return mAwsApiHelper.postDeletePortfolio(id);
    }

    @Override
    public Single<Response<List<Location>>> postLocations() {
        return mAwsApiHelper.postLocations();
    }

    @Override
    public Single<Response<List<Job>>> postJobs() {
        return mAwsApiHelper.postJobs();
    }

    @Override
    public Single<Response<List<Notice>>> postNotices() {
        return mAwsApiHelper.postNotices();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setPushToken(String value) {
        mPreferencesHelper.setPushToken(value);
    }

    @Override
    public String getPushToken() {
        return mPreferencesHelper.getPushToken();
    }

    @Override
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public List<Job> getJobs() {
        return jobs;
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }
}
