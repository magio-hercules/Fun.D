
package com.fund.iam.data;

import com.fund.iam.data.local.prefs.PreferencesHelper;
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
import retrofit2.Response;

@Singleton
public class AppDataManager implements DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mAwsApiHelper;
    private final ApiHelper mFirebaseApiHelper;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, @Named("aws") ApiHelper awsApiHelper, @Named("firebase") ApiHelper firebaseApiHelper) {
        mPreferencesHelper = preferencesHelper;
        mAwsApiHelper = awsApiHelper;
        mFirebaseApiHelper = firebaseApiHelper;
    }

    @Override
    public Maybe<Response<Void>> postFcmSend(PushBody pushBody) {
        return mFirebaseApiHelper.postFcmSend(pushBody);
    }

    @Override
    public Maybe<Response<List<User>>> postUsers(int userId) {
        return mAwsApiHelper.postUsers(userId);
    }

    @Override
    public Maybe<Response<List<Portfolio>>> postPortfolios(int userId) {
        return mAwsApiHelper.postPortfolios(userId);
    }

    @Override
    public Maybe<Response<Void>> postLogin(LoginBody loginBody) {
        return mAwsApiHelper.postLogin(loginBody);
    }

    @Override
    public Maybe<Response<User>> postVerifyToken(String token) {
        return mAwsApiHelper.postVerifyToken(token);
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
}
