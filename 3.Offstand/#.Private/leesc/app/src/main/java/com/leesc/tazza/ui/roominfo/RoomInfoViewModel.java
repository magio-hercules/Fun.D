package com.leesc.tazza.ui.roominfo;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.leesc.tazza.R;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.data.remote.ConnectionManager;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.RxEventBus;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;

public class RoomInfoViewModel extends BaseViewModel<RoomInfoNavigator> {

    private SchedulerProvider schedulerProvider;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private ResourceProvider resourceProvider;

    public RoomInfoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, Context context, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        this.context = context;
        this.schedulerProvider = schedulerProvider;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.resourceProvider = resourceProvider;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pInfo.class)
                .filter(info -> ((WifiP2pInfo) info).groupFormed && ((WifiP2pInfo) info).isGroupOwner)
                //Todo : 서버 소켓 옵저버블 변환...
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        info -> {
                            Log.d("lsc", "RoomInfoViewModel info " + info);
//                            createSocket(8080);
                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        message -> {
                            Log.d("lsc", "RoomInfoViewModel message " + message);
                            Toast.makeText(context, (String) message, Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    public void createGroup() {
        Log.d("lsc", "RoomInfoViewModel createGroup");
        try {
            Method setDeviceName = wifiP2pManager.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            setDeviceName.setAccessible(true);
            //Todo : 방이름 UI
            setDeviceName.invoke(wifiP2pManager, channel, resourceProvider.getString(R.string.key_room_prefix) + Build.MODEL, new WifiP2pManager.ActionListener() {

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
        getCompositeDisposable().add(serverThreadObservable(8080)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(onNext -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket onNext " + onNext);
                    Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                }, onError -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket onError " + onError.getMessage());
                }, () -> {
                    Log.d("lsc", "RoomInfoViewModel createSocket terminated");
                })
        );
    }

    public void removeGroup() {
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

    private Observable<String> serverThreadObservable(int roomPort) {
        return ConnectionManager.serverThreadObservable(roomPort);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "RoomInfoViewModel onCleared");
    }
}
