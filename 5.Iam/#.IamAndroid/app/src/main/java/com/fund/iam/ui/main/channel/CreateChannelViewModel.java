package com.fund.iam.ui.main.channel;

import android.view.View;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class CreateChannelViewModel extends BaseViewModel<CreateChannelNavigator> {

    public Channel channel = null;

    public CreateChannelViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }

    public void getNewChannelInfo(View view, int ownerId, String name, String purpose, String location, String description, String password) {

        Logger.i("getNewChannelInfo");

        getCompositeDisposable().add(
                getDataManager().postCreateChannel(ownerId, name, purpose, location, description, password)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postCreateChannels success");
                            channel = result.body();
                            Logger.d("result.body"+channel);

                            getNavigator().createChannel(view);

                        }, onError -> getNavigator().handleError(onError))
        );
    }
}
