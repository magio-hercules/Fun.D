package com.fund.iam.ui.main.home;


import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {


    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }


    public void testUsersApi() {
        getCompositeDisposable().add(getDataManager().getUsers().subscribe(result -> {
            Logger.d(result.body());
        }));
    }

    public void testPortfolioApi() {
        getCompositeDisposable().add(getDataManager().getPortfolios().subscribe(result -> {
            Logger.d(result.body());
        }));
    }

}
