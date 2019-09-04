package com.fundroid.offstand.ui.lobby.findroom;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.data.model.User;
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
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT_SELF;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;

public class FindRoomViewModel extends BaseViewModel<FindRoomNavigator> {

    private Context context;
    private final MutableLiveData<List<Room>> rooms;
    private Room selectedRoom;
    public ObservableField<Boolean> enterRoomEnable = new ObservableField<>(false);

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
        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(Room.class)
                .subscribe(selectedRoom -> {

                    for (Room room : Stream.of(rooms.getValue()).toList()) {
                        if (((Room) selectedRoom).getId().equals(room.getId())) {
                            room.setSelected(true);
                            this.selectedRoom = room;
                            enterRoomEnable.set(true);
                        } else {
                            room.setSelected(false);
                        }
                    }
                    rooms.setValue(rooms.getValue());
                }));

        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(String.class)
                        .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(result -> {
                            Log.d("lsc", "FindRoomViewModel result " + result);

                            switch (((ApiBody) result).getNo()) {
                                case API_ROOM_INFO:
                                    getNavigator().dismissProgress();
                                    BehaviorSubjectBus.getInstance().sendEvent(result);
                                    getNavigator().goToRoomActivity();
                                    break;

                                case API_OUT_SELF:
                                    getNavigator().showWifiAlertDialog();
                                    break;
                            }
                        }, onError -> {
                            Log.d("lsc", "FindRoomViewModel onError " + onError);
                        })
        );
    }

    void syncRooms() {
        getCompositeDisposable().add(ConnectionManager.syncRooms()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(this::setRooms
                        , onError -> getNavigator().handleError(onError)));
    }

    public void selectRooms() {
        selectedRoom = null;
        enterRoomEnable.set(false);
        getCompositeDisposable().add(ConnectionManager.selectRooms()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(querySnapshot -> setRooms((QuerySnapshot) querySnapshot)
                        , onError -> getNavigator().handleError(new Throwable(onError.toString()))));
    }

    private void setRooms(QuerySnapshot queryDocumentSnapshots) {
        rooms.setValue(Stream.of(queryDocumentSnapshots.getDocuments()).map(queryDocumentSnapshot ->
                new Room((String) ((QueryDocumentSnapshot) queryDocumentSnapshot).getData().get("id"),
                        (String) ((QueryDocumentSnapshot) queryDocumentSnapshot).getData().get("name"),
                        (String) ((QueryDocumentSnapshot) queryDocumentSnapshot).getData().get("address")))
                .toList());
    }

    private void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "FindRoomViewModel enterRoom " + roomAddress);

        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .andThen(Completable.timer(3000, TimeUnit.MILLISECONDS))
                .andThen(ConnectionManager.sendMessage(new ApiBody(API_ENTER_ROOM, new User(-1, false, getDataManager().getUserName(), getDataManager().getUserAvatar(), getDataManager().getUserTotal(), getDataManager().getUserWin()))))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom result");
                }, onError -> {
                    Log.d("lsc", "FindRoomViewModel enterRoom onError " + onError);
                    selectRooms();
//                    getNavigator().showToast(onError.getMessage());
                    getNavigator().dismissProgress();
                }));

    }

    public void onRefreshClick() {
        Log.d("lsc", "FindRoomViewModel onRefreshClick");
        selectRooms();

    }

    public void onEnterRoomClick() {
        MediaPlayer.create(context, R.raw.mouth_interface_button).start();
        if (selectedRoom == null) {
            getNavigator().showToast(context.getString(R.string.msg_room_not_selected));
        } else {
            getNavigator().showProgress();
            enterRoomEnable.set(false);
            try {
                enterRoom(InetAddress.getByName(selectedRoom.getAddress()), ROOM_PORT);
//                enterRoom(InetAddress.getByName(selectedRoom.getAddress()), 43514);
            } catch (UnknownHostException e) {
                getNavigator().handleError(e);
            }
        }
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
