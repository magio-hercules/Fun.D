package com.fund.iam.ui.main.channel;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ChannelUserListViewModel extends BaseViewModel<ChannelUserListNavigator> {

    public int channelId = 0;
    public List<ChannelUser> channelUsers = null;

    public ChannelUserListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();
    }

    private void subscribeEvent() {

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

}
