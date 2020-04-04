package com.fund.iam.ui.main.search;


import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class SearchViewModel extends BaseViewModel<SearchNavigator> {

    public List<Channel> channels = null;
    public List<User> users = null;


    public SearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }


    public void getChannelsInfo() {

        Logger.i("getChannelInfo");

        getCompositeDisposable().add(
                getDataManager().postChannels()
                .observeOn(getSchedulerProvider().io())
                .subscribeOn(getSchedulerProvider().computation())
                .subscribe(result -> {
                    Logger.d("postChannels success");
                    //TODO 여기부터 시작...
                    channels = result.body();
                    Logger.d("result.body"+channels);

                    getNavigator().updateChannels();

                }, onError -> getNavigator().handleError(onError))
        );

    }

    public void getUsersInfo() {

        Logger.i("getUsersInfo");

        getCompositeDisposable().add(
                getDataManager().postUsersAll()
                        .observeOn(getSchedulerProvider().io())
                        .subscribeOn(getSchedulerProvider().computation())
                        .subscribe(result -> {
                            Logger.d("postUsers success");
                            //TODO 여기부터 시작...
                            users = result.body();
                            Logger.d("result.body"+users);

                            getNavigator().updateUsers();

                        }, onError -> getNavigator().handleError(onError))
        );

    }

}
