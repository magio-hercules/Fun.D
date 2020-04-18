
package com.fund.iam.data;

import com.fund.iam.data.local.prefs.PreferencesHelper;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.Notice;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.VersionPage;
import com.fund.iam.data.model.request.PushBody;
import com.fund.iam.data.remote.ApiHelper;

import org.w3c.dom.Document;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

@Singleton
public class AppDataManager implements DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mAwsApiHelper;
    private final ApiHelper mGoogleApiHelper;
    private final ApiHelper mFirebaseApiHelper;

    private String marketVersion;
    private List<Job> jobs;
    private List<Location> locations;
    private User myInfo;
    private List<Portfolio> myPortfolios;


    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, @Named("aws") ApiHelper awsApiHelper, @Named("firebase") ApiHelper firebaseApiHelper, @Named("google") ApiHelper googleApiHelper) {
        mPreferencesHelper = preferencesHelper;
        mAwsApiHelper = awsApiHelper;
        mGoogleApiHelper = googleApiHelper;
        mFirebaseApiHelper = firebaseApiHelper;
    }

    @Override
    public Single<Response<Void>> postFcmSend(PushBody pushBody) {
        return mFirebaseApiHelper.postFcmSend(pushBody);
    }

    @Override
    public Single<Response<List<User>>> postUserInfo(int userId) {
        return mAwsApiHelper.postUserInfo(userId);
    }

    @Override
    public Single<Response<List<User>>> postUserUpdate(User user) {
        return mAwsApiHelper.postUserUpdate(user);
    }

    @Override
    public Single<Response<List<Portfolio>>> postPortfolios(int userId) {
        return mAwsApiHelper.postPortfolios(userId);
    }

    @Override
    public Single<Response<List<User>>> postLogin(User loginBody) {
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

    // 특정채널 전체유저 조회
    @Override
    public Single<Response<List<ChannelUser>>> postChannelUsers(int id) {
        return mAwsApiHelper.postChannelUsers(id);
    }
    // 특정채널 유저 가입하기
    @Override
    public Single<Response<List<ChannelUser>>> postChannelUserInsert(int channel_id, int user_id) {
        return mAwsApiHelper.postChannelUserInsert(channel_id, user_id);
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
    public Single<Response<Void>> postUpdatePortfolio( int id, int userId, int type, String text) {
        return mAwsApiHelper.postUpdatePortfolio(id, userId, type, text);
    }

    @Override
    public Single<Response<String>> postUploadImage(MultipartBody.Part image, RequestBody fileName) {
        return mAwsApiHelper.postUploadImage(image, fileName);
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

    @Override
    public Single<Response<List<LetterBox>>> postLetterBoxes(int userId) {
        return mAwsApiHelper.postLetterBoxes(userId);
    }

    @Override
    public Single<Response<List<Letter>>> postMessage(int userId, int friendId) {
        return mAwsApiHelper.postMessage(userId, friendId);
    }

    @Override
    public Single<Response<Void>> postMessageInsert(int userId, int friendId, String message) {
        return mAwsApiHelper.postMessageInsert(userId, friendId, message);
    }

    @Override
    public Single<Response<VersionPage>> getVersion(String packageName) {
        return mGoogleApiHelper.getVersion(packageName);
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

    @Override
    public void setMarketVersion(String version) {
        this.marketVersion = version;
    }

    @Override
    public String getMarketVersion() {
        return marketVersion;
    }

    @Override
    public void setMyInfo(User user) {
        myInfo = user;
        setAuthEmail(myInfo.getEmail());
        setAuthSnsType(myInfo.getSnsType());
    }

    @Override
    public User getMyInfo() {
        return myInfo;
    }

    @Override
    public List<Portfolio> getMyPortfolios() {
        return myPortfolios;
    }

    @Override
    public void setMyPortfolios(List<Portfolio> portfolios) {
        myPortfolios = portfolios;
    }

    @Override
    public void setAuthEmail(String value) {
        mPreferencesHelper.setAuthEmail(value);
    }

    @Override
    public String getAuthEmail() {
        return mPreferencesHelper.getAuthEmail();
    }

    @Override
    public void setAuthSnsType(String value) {
        mPreferencesHelper.setAuthSnsType(value);
    }

    @Override
    public String getAuthSnsType() {
        return mPreferencesHelper.getAuthSnsType();
    }
}
