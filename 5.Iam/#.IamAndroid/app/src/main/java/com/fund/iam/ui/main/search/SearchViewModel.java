package com.fund.iam.ui.main.search;


import com.annimon.stream.Stream;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends BaseViewModel<SearchNavigator> {

    public List<Channel> channels = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public List<Job> jobs = new ArrayList<>();
    public List<Location> locations = null;
    // 필터값 순서: 지역, 직종, 성별, 나이대
    public List<Integer> user_filters = new ArrayList<>();
    public int TabState = 1;

    public String[] spinner_str_user_gender = {"  선택없음  ","남자","여자"};
    public String[] spinner_str_user_age = {"  선택없음  ","10대", "20대","30대","40대", "50대", "60대 이상"};


    public SearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {
        // 필터값 저장 초기값 설정: 지역, 직종, 성별, 나이대 순..
        user_filters.add(0);
        user_filters.add(0);
        user_filters.add(0);
        user_filters.add(0);

    }

    public void getChannelsInfo() {

        Logger.i("getChannelsInfo");

        getCompositeDisposable().add(
                getDataManager().postChannels()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().newThread())
                .subscribe(result -> {
                    Logger.d("postChannels success");
                    channels = result.body();
                    Logger.d("result.body"+channels);

                    getNavigator().updateChannels(channels);

                }, onError -> getNavigator().handleError(onError))
        );
    }

    public void getUsersInfo() {

        Logger.i("getUsersInfo");

        getCompositeDisposable().add(
                getDataManager().postUsersAll()

                        .subscribeOn(getSchedulerProvider().newThread())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(result -> {
                            Logger.d("postUsers success");
                            if(result.isSuccessful()) {
                                users = result.body();
                                Logger.d("result.body"+users);
                                getNavigator().updateUsers(Stream.of(users).filterNot(user -> user.getId() == getDataManager().getMyInfo().getId()).toList(), getDataManager().getJobs());
                            } else {
                                getNavigator().handleError(new Throwable("user api error"));
                            }


                        }, onError -> getNavigator().handleError(onError))
        );
    }

}
