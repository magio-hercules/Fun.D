package com.fundroid.offstand.ui.roominfo;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.RxEventBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.databinding.ObservableField;
import io.reactivex.Observable;

public class RoomInfoViewModel extends BaseViewModel<RoomInfoNavigator> {

    private SchedulerProvider schedulerProvider;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private ResourceProvider resourceProvider;

    public ObservableField<String> roomName = new ObservableField<>();
    public ObservableField<String> roomMaxAttendee = new ObservableField<>();
    //    public ObservableField<Boolean> isSatisfy = new ObservableField<>();
    private boolean testBoolean = false;

    public RoomInfoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.resourceProvider = resourceProvider;

        //test
        roomName.set("테스트방장");
        roomMaxAttendee.set("2");

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pInfo.class)
                .filter(info -> ((WifiP2pInfo) info).groupFormed && ((WifiP2pInfo) info).isGroupOwner)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        info -> {
                            Log.d("lsc", "RoomInfoViewModel info " + info);
                            if (testBoolean) {
                                testBoolean = false;
                                createSocket(8080, Integer.parseInt(roomMaxAttendee.get()));
                            }
                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(message -> getNavigator().showToast((String) message))
        );
    }

    public void createGroup() {
        Log.d("lsc", "RoomInfoViewModel createGroup " + (wifiP2pManager == null));
        testBoolean = true;
        try {
            Method setDeviceName = wifiP2pManager.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            setDeviceName.setAccessible(true);
            setDeviceName.invoke(wifiP2pManager, channel, resourceProvider.getString(R.string.key_room_prefix) + roomName.get(), new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("lsc", "RoomInfoViewModel setDeviceName onSuccess");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("lsc", "RoomInfoViewModel setDeviceName onFailure " + reason);
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            getNavigator().handleError(e);
        }

        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "RoomInfoViewModel createGroup onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "RoomInfoViewModel createGroup onFailure " + reason);
            }
        });
    }

    public void createSocket() {
        createSocket(8080, 5);
    }

    public void createSocket(int roomPort, int roomMaxAttendee) {
        getCompositeDisposable().add(serverThreadObservable(roomPort, roomMaxAttendee)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(message -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket message " + message);
                    getNavigator().showToast(message);
                }, onError -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket onError " + onError.getMessage());
                }, () -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket terminated");
                })
        );
    }

    public void removeGroup() {
        testBoolean = false;
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "RoomInfoViewModel removeGroup onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "RoomInfoViewModel removeGroup onFailure " + reason);
            }
        });
    }

    public void sendMessage() {
        Log.d("lsc", "RoomInfoViewModel sendMessage ");
        getCompositeDisposable().add(ConnectionManager.broadcastMessageCompletable("testByServer")
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        () -> {
                            Log.d("lsc", "RoomInfoViewModel sendMessage onCompleted");
                        },
                        error -> {
                            getNavigator().handleError(error);
                        }
                ));
    }

    private Observable<String> serverThreadObservable(int roomPort, int roomMaxAttendee) {
        return ConnectionManager.serverThreadObservable(roomPort, roomMaxAttendee);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "RoomInfoViewModel onCleared");
    }
}
