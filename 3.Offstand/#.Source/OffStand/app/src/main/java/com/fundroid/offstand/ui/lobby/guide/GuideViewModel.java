package com.fundroid.offstand.ui.lobby.guide;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.model.User;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.fundroid.offstand.utils.rx.ReplaySubjectBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;

public class GuideViewModel extends BaseViewModel<GuideNavigator> {

    public GuideViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc","GuideViewModel onCleared");
    }
}
