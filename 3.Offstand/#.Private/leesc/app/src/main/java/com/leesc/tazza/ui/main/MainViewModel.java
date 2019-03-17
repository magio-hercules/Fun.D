package com.leesc.tazza.ui.main;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.data.model.Room;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.ui.roominfo.ServerThread;
import com.leesc.tazza.utils.rx.RxEventBus;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class MainViewModel extends BaseViewModel<MainNavigator> {

    private ServerSocket serverSocket;
    private ServerSocket serverSocket2;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
    private IntentFilter intentFilter;
    private SchedulerProvider schedulerProvider;
    private Context context;

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, Context context) {
        super(dataManager, schedulerProvider);
        this.context = context;
        this.schedulerProvider = schedulerProvider;
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(context, context.getMainLooper(), null);
        wifiDirectBroadcastReceiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, peerListListener, connectionInfoListener);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        context.registerReceiver(wifiDirectBroadcastReceiver, intentFilter);

        try {
            Method setDeviceName = wifiP2pManager.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            setDeviceName.setAccessible(true);
            setDeviceName.invoke(wifiP2pManager, channel, "오프섯다 룸", new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("lsc", "LobbyViewModel setDeviceName onSuccess");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("lsc", "LobbyViewModel setDeviceName onFailure " + reason);
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(Room.class)
                .subscribeOn(schedulerProvider.ui())
                .subscribe(
                        room -> {
                            Log.d("lsc", "room info " + ((Room) room).deviceName + ", " + ((Room) room).deviceAddress);
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

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(Room.class)
                .subscribeOn(schedulerProvider.ui())
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

    }


    public void makeRoom(int roomPort) {
        Log.d("lsc", "makeRoom ");
        getCompositeDisposable()
                .add(serverThreadObservable(roomPort)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(onNext -> {
                            Log.d("lsc", "makeRoom onNext " + onNext);
                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                        }, onError -> {
                            Log.d("lsc", "makeRoom onError " + onError.getMessage());
                        }, () -> {
                            Log.d("lsc", "makeRoom terminated");
                        }));
    }

    public void makeRoom2() {
        Log.d("lsc", "makeRoom2 ");
        getCompositeDisposable()
                .add(serverThreadObservable2()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(onNext -> {
                            Log.d("lsc", "makeRoom2 onNext " + onNext);
                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                        }, onError -> {
                            Log.d("lsc", "makeRoom2 onError " + onError.getMessage());
                        }, () -> {
                            Log.d("lsc", "makeRoom2 terminated");
                        }));
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

    public void stopDiscover() {
        wifiP2pManager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "LobbyViewModel stopDiscover onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "LobbyViewModel stopDiscover onFailure " + reason);
            }

        });
    }

    public void createGroup() {
        Log.d("lsc", "createGroup");
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "LobbyViewModel createGroup onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "LobbyViewModel createGroup onFailure " + reason);
            }
        });
    }

    public void removeGroup() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "LobbyViewModel removeGroup onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "LobbyViewModel removeGroup onFailure " + reason);
            }
        });
    }

    public void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "enterRoom " + roomAddress);
        getCompositeDisposable()
                .add(clientThreadObservable(roomAddress, roomPort)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(onNext -> {
                            Log.d("lsc", "enterRoom onNext " + onNext);
                        }, onError -> {
                            Log.d("lsc", "enterRoom onError " + onError.getMessage());
                        }, () -> {
                            Log.d("lsc", "enterRoom terminated");
                        }));
    }

    public void sendToClient() {
        getCompositeDisposable().add(sendToClientCompletable().subscribeOn(schedulerProvider.io()).subscribe());
    }

    public void sendToServer() {
        getCompositeDisposable().add(sendToServerCompletable().subscribeOn(schedulerProvider.io()).subscribe(
                () -> {
                    Log.d("lsc", "sendToServer terminated");
                },
                onError -> {
                    Log.d("lsc", "sendToServer onError " + onError.getMessage());
                }
        ));
    }

    private DataInputStream streamByClient = null;
    private DataInputStream streamByClient2 = null;
    private DataOutputStream streamToClient = null;
    private DataOutputStream streamToClient2 = null;
    private DataInputStream streamByServer = null;
    private DataOutputStream streamToServer = null;

    private Observable<String> serverThreadObservable(int roomPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "serverThreadObservable create");
            AsyncServer asyncServer = new AsyncServer();

            serverSocket = new ServerSocket(roomPort);
            ServerThread serverThread = new ServerThread(serverSocket);
            Socket client = serverSocket.accept();
            streamByClient = new DataInputStream(client.getInputStream());
            streamToClient = new DataOutputStream(client.getOutputStream());
            while (serverSocket != null) {
                try {
                    subscriber.onNext(streamByClient.readUTF());

                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<String> serverThreadObservable2() {
        return Observable.create(subscriber -> {
            Log.d("lsc", "serverThreadObservable2 create");
            serverSocket2 = new ServerSocket(8081);
            streamByClient2 = new DataInputStream(serverSocket2.accept().getInputStream());
            streamToClient2 = new DataOutputStream(serverSocket2.accept().getOutputStream());
            while (serverSocket2 != null) {
                try {
                    subscriber.onNext(streamByClient2.readUTF());
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<String> clientThreadObservable(InetAddress serverIp, int serverPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "clientThreadObservable create");

            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(serverIp, serverPort), 500);
                streamByServer = new DataInputStream(socket.getInputStream());
                streamToServer = new DataOutputStream(socket.getOutputStream());

                while (socket != null) {
                    try {
                        subscriber.onNext(streamByServer.readUTF());
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


//            Socket socket = new Socket(serverIp, serverPort);
//            String messageFromServer;
//            streamByServer = new DataInputStream(socket.getInputStream());
//            streamToServer = new DataOutputStream(socket.getOutputStream());
//            messageFromServer = streamByServer.readUTF();
//            subscriber.onNext(messageFromServer);
        });
    }

    private Completable sendToClientCompletable() {
        try {
            streamToClient.writeUTF("testByServer");
        } catch (IOException e) {
            getNavigator().handleError(e);
        }
        return Completable.complete();
    }

    private Completable sendToServerCompletable() {
        try {
            streamToServer.writeUTF("testByClient");
        } catch (IOException e) {
            getNavigator().handleError(e);
        }
        return Completable.complete();
    }

    private WifiP2pManager.PeerListListener peerListListener = (WifiP2pDeviceList wifiP2pDeviceList) -> {
        Log.d("lsc", "LobbyViewModel peerListListener " + wifiP2pDeviceList.getDeviceList());
        getNavigator()
                .onRepositoriesChanged(Stream.of(wifiP2pDeviceList.getDeviceList())
                        .map(wifiP2pDevice -> new Room(wifiP2pDevice.deviceName, wifiP2pDevice.deviceAddress))
                        .collect(Collectors.toList())
                );
    };

    private boolean test = true;

    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {
        Log.d("lsc", "LobbyViewModel connectionInfoListener " + info.groupOwnerAddress + ", isGroupOwner " + info.isGroupOwner + ", wifiP2pInfo.groupFormed " + info.groupFormed);
        if (info.isGroupOwner) {
            if(test) {
                makeRoom(8080);
                test = false;
            }

        } else {
            enterRoom(info.groupOwnerAddress, 8080);
        }

    };

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "LobbyViewModel onCleared");
        context.unregisterReceiver(wifiDirectBroadcastReceiver);
    }
}
