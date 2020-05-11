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

    public void setChannelUserInsert(int channel_id, int user_id) {

        Logger.i("setChannelUserInsert");

        getCompositeDisposable().add(
                getDataManager().postChannelUserInsert(channel_id, user_id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postChannelUserInsert success: channel_id: "+channel_id+" user_id: "+ user_id);

                            getNavigator().getChannelInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }

    public void setChannelUserDelete(int channel_id, int user_id) {
        Logger.i("setChannelUserDelete");

        getCompositeDisposable().add(
                getDataManager().postChannelUserDelete(channel_id, user_id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postChannelUserDelete success: channel_id: "+channel_id+" user_id: "+ user_id);

                            getNavigator().getChannelInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }
}
