package com.fund.iam.ui.main;


import androidx.databinding.ObservableField;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.NavigateBus;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

public class MainViewModel extends BaseViewModel<MainNavigator> {

//    public ObservableField<Boolean> toolbarVisible = new ObservableField<>(false);
//    public ObservableField<String> toolbarTitle = new ObservableField<>();

    private String testString = "testString";

    public String getTestString() {
        return testString;
    }

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        Logger.d("MainViewModel constructor");
        subscribeEvent();

    }

    private void subscribeEvent() {
        getCompositeDisposable().add(NavigateBus.getInstance().getDestination()
                .subscribe(destination -> getNavigator().actionNavigate(destination)));
        
    }


}
