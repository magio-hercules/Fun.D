package com.fundroid.offstand.ui.lobby.makeroom;


import android.util.Log;

import androidx.databinding.ObservableField;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.data.model.User;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.BehaviorSubjectBus;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_AVAILABLE;
import static com.fundroid.offstand.utils.CommonUtils.getRandomString;

public class MakeRoomViewModel extends BaseViewModel<MakeRoomNavigator> {

    public ObservableField<String> roomName = new ObservableField<>();

    public MakeRoomViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

        roomName.set(getDataManager().getUserName());

        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(String.class)
                .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(result -> {
                    Log.d("lsc", "MakeRoomViewModel result " + result);
                    switch (((ApiBody) result).getNo()) {
                        case API_ROOM_INFO:
                            getNavigator().goToRoomActivity();
                            break;

                        case API_SHUFFLE_AVAILABLE:
                            BehaviorSubjectBus.getInstance().sendEvent(result);
                            break;
                    }

                }, onError -> {
                    Log.d("lsc", "MakeRoomViewModel onError " + onError);
                }, () -> Log.d("lsc", "MakeRoomViewModel onCompleted"))
        );
    }

    public void makeRoomClick() {
        Log.d("lsc", "MakeRoomCViewModel makeRoomClick " + roomName.get());
        getDataManager().setRoomName(roomName.get() + "-" + getRandomString(4));
        createSocket(ROOM_PORT, 3);
    }

    public void createSocket(int roomPort, int roomMaxAttendee) {
        getCompositeDisposable().add(
                ConnectionManager.insertRoom(getDataManager().getRoomName())
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().io())
                        .andThen(ConnectionManager.createServerThread(roomPort, roomMaxAttendee))
                        .andThen(ConnectionManager.createClientThread(null, ROOM_PORT))
                        .andThen(Completable.timer(500, TimeUnit.MILLISECONDS))
                        .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new User(-1, true, getDataManager().getUserName(), getDataManager().getUserAvatar(), getDataManager().getUserTotal(), getDataManager().getUserWin()))))
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(() -> {
                            Log.d("lsc", "MakeRoomViewModel createSocket result ");
                        }, onError -> {
                            getNavigator().handleError(onError);
                        })
        );

    }


    public void onNavBackClick() {
        getNavigator().goBack();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "MakeRoomViewModel onCleared");
    }
}
