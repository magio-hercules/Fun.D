package com.leesc.tazza.ui.lobby;


import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.leesc.tazza.R;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.data.model.Room;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.receiver.WifiDirectReceiver;
import com.leesc.tazza.service.WifiP2pService;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.RxEventBus;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class LobbyViewModel extends BaseViewModel<LobbyNavigator> {

    private SchedulerProvider schedulerProvider;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiP2pService wifiP2pService;

    public LobbyViewModel(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          Context context,
                          WifiP2pManager wifiP2pManager,
                          WifiP2pManager.Channel channel,
                          WifiP2pService wifiP2pService,
                          WifiDirectReceiver wifiDirectReceiver,
                          ResourceProvider resourceProvider) {

        super(dataManager, schedulerProvider);
        this.context = context;
        this.schedulerProvider = schedulerProvider;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.wifiP2pService = wifiP2pService;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pDeviceList.class)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        peers -> {
                            Log.d("lsc", "LobbyViewModel peers " + ((WifiP2pDeviceList) peers).getDeviceList());
                            getNavigator().onRepositoriesChanged(Stream.of(((WifiP2pDeviceList) peers).getDeviceList())
                                    .map(wifiP2pDevice -> new Room(wifiP2pDevice.deviceName, wifiP2pDevice.deviceAddress))
                                    .filter(room -> room.getDeviceName().contains(resourceProvider.getString(R.string.key_room_prefix)))
                                    .collect(Collectors.toList())
                            );
                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(Room.class)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        room -> {
                            Log.d("lsc", "LobbyViewModel room info " + ((Room) room).deviceName + ", " + ((Room) room).deviceAddress);
                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = ((Room) room).deviceAddress;
                            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("lsc", "LobbyViewModel connect onSuccess");
                                }

                                @Override
                                public void onFailure(int reason) {
                                    Log.d("lsc", "LobbyViewModel connect onFailure " + reason);
                                }
                            });
                        }
                ));

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pInfo.class)
                .filter(info -> !(((WifiP2pInfo) info).isGroupOwner))
                //Todo : 서버 소켓 옵저버블 변환...
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        info -> {
                            Log.d("lsc", "LobbyViewModel info " + info);
                            enterRoom(((WifiP2pInfo) info).groupOwnerAddress, 8080);
                        }
                )
        );

        discover();

    }

    public void discover() {
        Log.d("lsc", "discover");
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "LobbyViewModel discoverPeers onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "LobbyViewModel discoverPeers onFailure " + reason);
            }

        });
    }

    public void goToRoomInfo() {
        getNavigator().goToRoomInfoActivity();
    }

    public void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "enterRoom " + roomAddress);
        getCompositeDisposable()
                .add(clientThreadObservable(roomAddress, roomPort)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(onNext -> {
                            Log.d("lsc", "enterRoom onNext " + onNext);
//                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                        }, onError -> {
                            Log.d("lsc", "enterRoom onError " + onError.getMessage());
                        }, () -> {
                            Log.d("lsc", "enterRoom terminated");
                        }));
    }

    DataInputStream streamByServer = null;
    DataOutputStream streamToServer = null;

    private Observable<String> clientThreadObservable(InetAddress serverIp, int serverPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "clientThreadObservable create");

            try {
                Socket socket = new Socket();
                Log.d("lsc", "clientThreadObservable create 1");
                socket.connect(new InetSocketAddress(serverIp, serverPort), 30000);
                Log.d("lsc", "clientThreadObservable create 2");
                streamByServer = new DataInputStream(socket.getInputStream());
                Log.d("lsc", "clientThreadObservable create 3");
                streamToServer = new DataOutputStream(socket.getOutputStream());
                Log.d("lsc", "clientThreadObservable create 4");
                while (socket != null) {
                    try {
                        subscriber.onNext(streamByServer.readUTF());
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }

            } catch (IOException e) {
                subscriber.onError(e);
            }


//            Socket socket = new Socket(serverIp, serverPort);
//            String messageFromServer;
//            streamByServer = new DataInputStream(socket.getInputStream());
//            streamToServer = new DataOutputStream(socket.getOutputStream());
//            messageFromServer = streamByServer.readUTF();
//            subscriber.onNext(messageFromServer);
        });
    }

    private Single sendToClientCompletable() {
        return Single.just("messageByClient");
    }

    public void sendMessage() {
        getCompositeDisposable().add(sendToClientCompletable()
                .observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.io()).subscribe(
                        message -> {
                            Log.d("lsc","sendMessage onNext");
                            streamToServer.writeUTF(message.toString());
                        },
                        error -> {
                            Log.d("lsc","sendMessage error " + error.toString());
                        }
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "LobbyViewModel onCleared");
    }
}
