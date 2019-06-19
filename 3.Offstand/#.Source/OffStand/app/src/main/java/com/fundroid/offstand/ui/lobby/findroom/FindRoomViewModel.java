package com.fundroid.offstand.ui.lobby.findroom;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.model.User;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.BehaviorSubjectBus;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.Completable;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;

public class FindRoomViewModel extends BaseViewModel<FindRoomNavigator> {

    private Context context;
    private final MutableLiveData<List<Room>> rooms;

    public MutableLiveData<List<Room>> getRooms() {
        return rooms;
    }

    public FindRoomViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        this.context = context;
        rooms = new MutableLiveData<>();
        subscribeEvents();
    }

    private void subscribeEvents() {
        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(String.class)
                .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(result -> {
                    Log.d("lsc", "FindRoomViewModel result " + result);
                    switch (((ApiBody) result).getNo()) {
                        case API_ROOM_INFO:
                            BehaviorSubjectBus.getInstance().sendEvent(result);
                            getNavigator().goToRoomActivity();
                            break;
                    }
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel onError " + onError);
                })
        );

        getCompositeDisposable().add(ConnectionManager.selectRooms()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(querySnapshot ->
                                rooms.setValue(Stream.of(((QuerySnapshot) querySnapshot).getDocuments()).map(queryDocumentSnapshot ->
                                        new Room((String) ((QueryDocumentSnapshot) queryDocumentSnapshot).getData().get("name"),
                                                (String) ((QueryDocumentSnapshot) queryDocumentSnapshot).getData().get("address")))
                                        .toList())
                        , onError -> {
                            Log.d("lsc", "FindRoomViewModel onRefreshClick onError " + onError);
                        }));
    }

    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .andThen(Completable.timer(1000, TimeUnit.MILLISECONDS))
                .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new User(-1, false, getDataManager().getUserName(), getDataManager().getUserAvatar(), getDataManager().getUserTotal(), getDataManager().getUserWin()))))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom result");
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onError " + onError);
                }));

    }

    public void onRefreshClick() {
        Log.d("lsc", "FindRoomViewModel onRefreshClick");

    }

    public void onEnterRoomClick() {
        MediaPlayer.create(context, R.raw.mouth_interface_button).start();
//        byte[] ipAddr = new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 163};
        byte[] ipAddr = new byte[]{(byte) 121, (byte) 133, (byte) 212, (byte) 120};//http://121.133.212.120
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
        Log.d("lsc", "FindRoomVIewModel onCleared");
    }
}
