package com.fundroid.offstand.ui.lobby.makeroom;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.model.User;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.RxEventBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.fundroid.offstand.core.AppConstant.RESULT_OK;
import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;
import static com.fundroid.offstand.model.User.EnumAvatar.FEB;

public class MakeRoomViewModel extends BaseViewModel<MakeRoomNavigator> {

    private SchedulerProvider schedulerProvider;

    public MakeRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .flatMap(json -> ConnectionManager.serverProcessor((String) json))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(result -> {
                    Log.d("lsc", "MakeRoomViewModel result " + result);
                    switch (((ApiBody) result).getNo()) {
                        case API_ROOM_INFO:
                            getNavigator().goToRoomActivity();
                            break;
                    }

                }, onError -> {
                    Log.d("lsc", "MakeRoomViewModel onError " + onError);
                }, () -> Log.d("lsc", "MakeRoomViewModel onCompleted"))
        );
    }

    public void makeRoomClick() {
        //Test
        createSocket(ROOM_PORT, 4);
    }

    public void createSocket(int roomPort, int roomMaxAttendee) {
        Log.d("lsc", "MakeRoomViewModel createSocket");
        getCompositeDisposable().add(ConnectionManager.createServerThread(roomPort, roomMaxAttendee)
                .andThen(ConnectionManager.createClientThread(null, ROOM_PORT))
                .andThen(Completable.timer(500, TimeUnit.MILLISECONDS))
                .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new User(true, getDataManager().getUserName(), getDataManager().getUserAvatar(), getDataManager().getUserWin(), 10))))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .subscribe(() -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket result ");
                }, onError -> {
                    Log.d("lsc", "MakeRoomViewModel createSocket onError " + onError);
                })
        );
    }


    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    Log.d("lsc", "MakeRoomViewModel enterRoom onNext ");
                }, onError -> {
                    Log.d("lsc", "MakeRoomViewModel enterRoom onError " + onError.getMessage());
                }));

    }

    public void onNavBackClick() {
        getNavigator().goBack();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "FindRoomViewModel enterRoom onCleared");
    }
}
