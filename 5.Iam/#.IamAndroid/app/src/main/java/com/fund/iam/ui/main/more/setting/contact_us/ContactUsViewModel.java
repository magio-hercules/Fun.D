package com.fund.iam.ui.main.more.setting.contact_us;


import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

public class ContactUsViewModel extends BaseViewModel<ContactUsNavigator> {

    public ContactUsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

    }
}
