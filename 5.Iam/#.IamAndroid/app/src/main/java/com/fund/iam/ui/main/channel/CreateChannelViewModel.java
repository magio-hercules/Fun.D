package com.fund.iam.ui.main.channel;

import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

public class CreateChannelViewModel extends BaseViewModel<CreateChannelNavigator> {
    // TODO: Implement the ViewModel

    public CreateChannelViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }
}
