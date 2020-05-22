package com.fund.iam.ui.intro;

import android.content.Context;

import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.VersionPage;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.fund.iam.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import retrofit2.Response;

public class IntroViewModel extends BaseViewModel<IntroNavigator> {

    private Context context;

    public IntroViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        this.context = context;
        getCompositeDisposable().add(
                getDataManager().getVersion(/*context.getPackageName()*/"com.fundroid.offstand")
                        .flatMapObservable(this::getInitialData)
                        .subscribe(timeSet -> checkAuth(), onError -> Logger.e("initialData Error")));

    }

    private Observable<Long> getInitialData(Response<VersionPage> version) {

        if (version.isSuccessful()) {
            for (String v : version.body().getVersions()) {
//                    if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", version)) {     // [major].[minor].[patch] 형태
                if (Pattern.matches("^[0-9]{1}.[0-9]{1}$", v)) {
                    getDataManager().setMarketVersion(v);
                }
            }

            return Observable.combineLatest(getDataManager().postJobs().toObservable(), getDataManager().postLocations().toObservable(), Observable.timer(2, TimeUnit.SECONDS), (jobs, locations, timeSet) -> {
                getDataManager().setJobs(jobs.body());
                getDataManager().setLocations(locations.body());
                return timeSet;
            });
        } else {
            return Observable.error(new Exception("version error"));
        }
    }

    private void checkAuth() {
        if (/*FirebaseAuth.getInstance().getCurrentUser() == null || */getDataManager().getAuthEmail() == null) {
            getNavigator().startLoginActivity();
        } else {
            getCompositeDisposable().add(getDataManager().postLogin(new User(getDataManager().getAuthEmail(), getDataManager().getPushToken(), getDataManager().getAuthSnsType()))
                    .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                    .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .subscribe(portFolio -> {
                        Logger.d("portFolio " + portFolio.isSuccessful());
                        if (portFolio.isSuccessful()) {
                            getDataManager().setMyPortfolios(portFolio.body());
                            getNavigator().startMainActivity();
                        } else {
                            Logger.e("Login Error");
                        }

                    }, onError -> getNavigator().handleError(onError)));
        }
    }


}
