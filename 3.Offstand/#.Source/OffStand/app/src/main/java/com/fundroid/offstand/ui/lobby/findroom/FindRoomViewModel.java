package com.fundroid.offstand.ui.lobby.findroom;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.RxEventBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;

public class FindRoomViewModel extends BaseViewModel<FindRoomNavigator> {

    private SchedulerProvider schedulerProvider;

    public FindRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(message -> {
                    Log.d("lsc","FindRoomViewModel message " + message);
                })
        );
    }

    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.clientThreadObservable(roomAddress, roomPort)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(onNext -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom thread " + Thread.currentThread().getName());
                    Log.d("lsc", "FindRoomViewModel enterRoom onNext " + onNext);
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onError " + onError.getMessage());
                }, () -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom terminated");
                }));

    }

    public void onEnterRoomClick() {
        byte[] ipAddr = new byte[]{(byte) 192, (byte) 168, (byte) 40, (byte) 197};
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
}
