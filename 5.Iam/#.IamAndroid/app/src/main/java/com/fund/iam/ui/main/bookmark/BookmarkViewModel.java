package com.fund.iam.ui.main.bookmark;


import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class BookmarkViewModel extends BaseViewModel<BookmarkNavigator> {

    public List<Channel> channels = null;
    public List<User> users = null;
    public List<Job> jobs = null;

    public BookmarkViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }

    public void getBookmarkChannelsInfo(int id) {

        Logger.i("getChannelInfo");

        getCompositeDisposable().add(
                getDataManager().postBookmarkChannels(id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("post BookmarkChannels success");
                            //TODO 여기부터 시작...
                            channels = result.body();
                            Logger.d("result.body"+channels);

                            getNavigator().updateChannels();

                        }, onError -> getNavigator().handleError(onError))
        );

    }

    public void getBookmarkUsersInfo(int id) {

        Logger.i("getUsersInfo");

        getCompositeDisposable().add(
                getDataManager().postBookmarkUsers(id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("post BookmarkUsers success");
                            //TODO 여기부터 시작...
                            users = result.body();
                            Logger.d("result.body"+users);

                            getNavigator().updateUsers();

                        }, onError -> getNavigator().handleError(onError))
        );

    }

    public void getJobsInfo() {

        Logger.i("getJobsInfo");

        getCompositeDisposable().add(
                getDataManager().postJobs()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(result -> {
                            Logger.d("postJobs success");
                            jobs = result.body();
                            Logger.d("result.body"+jobs);

                        }, onError -> getNavigator().handleError(onError))
        );

    }
}
