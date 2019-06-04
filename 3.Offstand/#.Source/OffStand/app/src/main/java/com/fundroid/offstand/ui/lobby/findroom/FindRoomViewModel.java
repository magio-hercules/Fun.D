package com.fundroid.offstand.ui.lobby.findroom;


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

public class FindRoomViewModel extends BaseViewModel<FindRoomNavigator> {

    public FindRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(String.class)
                .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(result -> {
                    Log.d("lsc","FindRoomViewModel result " + result);
                    switch (((ApiBody) result).getNo()) {
                        case API_ROOM_INFO:
                            ReplaySubjectBus.getInstance().sendEvent(((ApiBody)result).getUsers());
                            getNavigator().goToRoomActivity();
                            break;
                    }
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel onError " + onError);
                })
        );
    }

    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .andThen(Completable.timer(500, TimeUnit.MILLISECONDS))
                .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new User(0, false, getDataManager().getUserName(), getDataManager().getUserAvatar(), getDataManager().getUserTotal(), getDataManager().getUserWin()))))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom result");
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onError " + onError);
                }));

    }

    public void onEnterRoomClick() {
        byte[] ipAddr = new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 4};
//        byte[] ipAddr = new byte[]{(byte) 125, (byte) 178, (byte) 199, (byte) 5};
//        byte[] ipAddr = new byte[]{(byte) 121, (byte) 133, (byte) 212, (byte) 120};//http://121.133.212.120
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(ipAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        enterRoom(addr, ROOM_PORT);
    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc","FindRoomVIewModel onCleared");
    }
}
