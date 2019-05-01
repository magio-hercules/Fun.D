package com.fundroid.offstand.ui.lobby.makeroom;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Attendee;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.RxEventBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.fundroid.offstand.core.AppConstant.RESULT_OK;
import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;
import static com.fundroid.offstand.data.model.Attendee.EnumAvatar.FEB;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;

public class MakeRoomViewModel extends BaseViewModel<MakeRoomNavigator> {

    private SchedulerProvider schedulerProvider;

    public MakeRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .flatMap(json -> {
                    Log.d("lsc", "MakeRoomViewModel flatMap " + json);
                    return ConnectionManager.serverProcessor((String) json);
                })
                .subscribe(result -> {
                    Log.d("lsc", "MakeRoomViewModel result " + result);
                    switch ((int) result) {
                        case RESULT_OK:
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
        try {
            createSocket(ROOM_PORT, 5);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void createSocket(int roomPort, int roomMaxAttendee) throws UnknownHostException {
        Log.d("lsc", "MakeRoomViewModel createSocket");
        getCompositeDisposable().add(ConnectionManager.createServerThread(roomPort, roomMaxAttendee)
                .andThen(ConnectionManager.createClientThread(InetAddress.getLocalHost(), ROOM_PORT))
                .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new Attendee("홍길동", FEB, 1, 10))))
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
