package com.fund.iam.ui.main.channel;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ChannelViewModel extends BaseViewModel<ChannelNavigator> {

    public List<Channel> channel = null;
    public List<ChannelUser> channelUsers = null;
    public List<ChannelUser> channelUserInsertResult = null;
    public int channelId = 0;
    public int isJoin = 0;

    public ChannelViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }


    private void subscribeEvent() {

    }

    public void getChannelInfo(int id) {

        Logger.i("getChannelInfo");

        getCompositeDisposable().add(
                getDataManager().postChannel(id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postChannelInfo success");
                            channel = result.body();
                            Logger.d("result.body"+channel);

                            getNavigator().getChannelInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }

    public void getChannelUserInfo(int channel_id) {

        Logger.i("getChannelUserInfo");

        getCompositeDisposable().add(
                getDataManager().postChannelUsers(channel_id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postChannelUsers success");
                            channelUsers = result.body();
                            Logger.d("result.body"+channelUsers);

                            getNavigator().getUsersInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }

    public void getChannelUserInsert(int channel_id, int user_id) {

        Logger.i("getChannelUserInsert");

        getCompositeDisposable().add(
                getDataManager().postChannelUserInsert(channel_id, user_id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postChannelUserInsert success");
                            channelUserInsertResult = result.body();
                            Logger.d("result.body"+channelUserInsertResult);

                            getNavigator().getUsersInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }
}
