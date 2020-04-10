package com.fund.iam.ui.main.channel;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ChannelViewModel extends BaseViewModel<ChannelNavigator> {

    public List<Channel> channel = null;

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
                            Logger.d("postChannels success");
                            channel = result.body();
                            Logger.d("result.body"+channel);

                            getNavigator().getChannelInfo();

                        }, onError -> getNavigator().handleError(onError))
        );
    }
}
