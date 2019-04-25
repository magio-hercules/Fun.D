package com.fundroid.offstand.ui.lobby.makeroom;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.net.InetAddress;

import io.reactivex.Observable;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;

public class MakeRoomViewModel extends BaseViewModel<MakeRoomNavigator> {

    private SchedulerProvider schedulerProvider;

    public MakeRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;
    }

    private Observable<Integer> serverThreadObservable(int roomPort, int roomMaxAttendee) {
        return ConnectionManager.serverThreadObservable(roomPort, roomMaxAttendee);
    }

    public void makeRoomClick() {
        //Test
        createSocket(ROOM_PORT, 5);
    }

    public void createSocket(int roomPort, int roomMaxAttendee) {
        Log.d("lsc", "MakeRoomViewModel createSocket");
        getCompositeDisposable().add(serverThreadObservable(roomPort, roomMaxAttendee)
                .flatMap(userCount -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket userCount " + userCount);
                    return ConnectionManager.clientThreadObservable(InetAddress.getLocalHost(), ROOM_PORT);
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(message -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket message " + message);
                }, onError -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket onError " + onError.getMessage());
                }, () -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket terminated");
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

    public void onNavBackClick() {
        getNavigator().goBack();
    }
}
