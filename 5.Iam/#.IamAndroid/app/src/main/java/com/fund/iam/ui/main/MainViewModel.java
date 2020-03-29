package com.fund.iam.ui.main;


import androidx.databinding.ObservableField;

import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

public class MainViewModel extends BaseViewModel<MainNavigator> {

    public ObservableField<Boolean> toolbarVisible = new ObservableField<>(false);


    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();

    }

    private void subscribeEvent() {

    }


}
