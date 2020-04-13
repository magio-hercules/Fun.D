package com.fund.iam.ui.main.search;


import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class SearchViewModel extends BaseViewModel<SearchNavigator> {

    public List<Channel> channels = null;
    public List<User> users = null;
    public int TabState = 1;

    public String[] spinner_str_user_region = {" 선택없음 ","서울특별시", "부산광역시","인천광역시","대구광역시", "광주광역시", "대전광역시", " 울산광역시", "세종시", "경기도", "강원도", "충청남도", "충청북도", "경상북도","경상남도", "전라북도","전라남도", "제주도"};
    public String[] spinner_str_user_job =  {" 선택없음 ","기획자","개발자", "디자이너", "마케터"};
    public String[] spinner_str_user_gender = {" 선택없음 ","남자","여자"};
    public String[] spinner_str_user_age = {" 선택없음 ","10대", "20대","30대","40대", "50대 이상"};


    public SearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }


    public void getChannelsInfo() {

        Logger.i("getChannelsInfo");

        getCompositeDisposable().add(
                getDataManager().postChannels()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    Logger.d("postChannels success");
                    channels = result.body();
                    Logger.d("result.body"+channels);

                    getNavigator().updateChannels();

                }, onError -> getNavigator().handleError(onError))
        );
    }

    public void getUsersInfo() {

        Logger.i("getUsersInfo");

        getCompositeDisposable().add(
                getDataManager().postUsersAll()
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postUsers success");
                            users = result.body();
                            Logger.d("result.body"+users);

                            getNavigator().updateUsers();

                        }, onError -> getNavigator().handleError(onError))
        );

    }

}
