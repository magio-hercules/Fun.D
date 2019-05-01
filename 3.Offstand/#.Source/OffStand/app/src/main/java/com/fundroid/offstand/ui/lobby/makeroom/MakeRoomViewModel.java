package com.fundroid.offstand.ui.lobby.makeroom;


import android.util.Log;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Attendee;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.RxEventBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.gson.Gson;

import java.net.InetAddress;

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

    private Single<Integer> serverThreadObservable(int roomPort, int roomMaxAttendee) {
        return ConnectionManager.createServerThread(roomPort, roomMaxAttendee);
    }

    public void makeRoomClick() {
        //Test
        createSocket(ROOM_PORT, 5);
    }

    public void createSocket(int roomPort, int roomMaxAttendee) {
        Log.d("lsc", "MakeRoomViewModel createSocket");
        getCompositeDisposable().add(serverThreadObservable(roomPort, roomMaxAttendee)
//                .flatMap(userCount -> ConnectionManager.createClientThread(InetAddress.getLocalHost(), ROOM_PORT))
//                .flatMap(result -> ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new Attendee("홍길동", FEB, 1, 10))))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(result -> {
                            Log.d("lsc", "MakeRoomViewModel createSocket result " + result);
                            ConnectionManager.createClientThread(InetAddress.getLocalHost(), ROOM_PORT)
                                    .flatMap(integer -> ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new Attendee("홍길동", FEB, 1, 10)))).subscribe(
                                    test -> {
                                        Log.d("lsc", "MakeRoomViewModel 2 test " + test);
                                    }, onError -> {
                                        Log.d("lsc", "MakeRoomViewModel 2 onError " + onError);
                                    }
                            );


                        }, onError -> {
                            Log.d("lsc", "MakeRoomViewModel createSocket onError " + onError);
                        })
        );
    }

//    public void createSocket(int roomPort, int roomMaxAttendee) {
//        Log.d("lsc", "MakeRoomViewModel createSocket");
//        getCompositeDisposable().add(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new Attendee("이승철", JAN, 10, 1)))
//                .concatWith(serverThreadObservable(roomPort, roomMaxAttendee).flatMap(userCount -> ConnectionManager.createClientThread(InetAddress.getLocalHost(), ROOM_PORT)))
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(message -> {
//                    Log.d("lsc", "MakeRoomViewModel createSocket message " + message);
//                }, onError -> {
//                    Log.d("lsc", "MakeRoomViewModel createSocket onError " + onError);
//                }));
//    }

    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(onNext -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onNext " + onNext);
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onError " + onError.getMessage());
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
