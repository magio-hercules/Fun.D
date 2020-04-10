package com.fund.iam.ui.main;


import androidx.databinding.ObservableField;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.NavigateBus;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;

public class MainViewModel extends BaseViewModel<MainNavigator> {

//    public ObservableField<Boolean> toolbarVisible = new ObservableField<>(false);
//    public ObservableField<String> toolbarTitle = new ObservableField<>();


    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        subscribeEvent();

    }

    private void subscribeEvent() {
        getCompositeDisposable().add(NavigateBus.getInstance().getDestination()
                .subscribe(destination -> getNavigator().actionNavigate(destination)));
        
    }

    public void getInitialData() {
        getCompositeDisposable().add(getDataManager().postJobs()
                .observeOn(getSchedulerProvider().io())
                .subscribe(jobs -> getDataManager().setJobs(jobs.body())));

        getCompositeDisposable().add(getDataManager().postLocations()
                .observeOn(getSchedulerProvider().io())
                .subscribe(locations -> getDataManager().setLocations(locations.body())));

    }


}
