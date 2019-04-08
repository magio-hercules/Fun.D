package com.leesc.tazza.ui.lobby;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.leesc.tazza.R;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.data.model.Room;
import com.leesc.tazza.data.remote.ConnectionManager;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.RxEventBus;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LobbyViewModel extends BaseViewModel<LobbyNavigator> {

    private SchedulerProvider schedulerProvider;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;

    public LobbyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        this.schedulerProvider = schedulerProvider;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pDeviceList.class)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        peers -> {
                            Log.d("lsc", "LobbyViewModel peers " + ((WifiP2pDeviceList) peers).getDeviceList());
                            getNavigator().onRepositoriesChanged(Stream.of(((WifiP2pDeviceList) peers).getDeviceList())
                                    .filter(wifiP2pDevice -> wifiP2pDevice.deviceName.contains(resourceProvider.getString(R.string.key_room_prefix)))
                                    .map(wifiP2pDevice -> new Room(wifiP2pDevice.deviceName.substring(resourceProvider.getString(R.string.key_room_prefix).length()), wifiP2pDevice.deviceAddress))
                                    .collect(Collectors.toList())
                            );
                        },
                        error -> {
                            getNavigator().handleError((Throwable) error);
                        },
                        () -> {

                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pDeviceList.class)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        peers -> {
                            Log.d("lsc", "LobbyViewModel peers " + ((WifiP2pDeviceList) peers).getDeviceList());
                            getNavigator().onRepositoriesChanged(Stream.of(((WifiP2pDeviceList) peers).getDeviceList())
                                    .filter(wifiP2pDevice -> wifiP2pDevice.deviceName.contains(resourceProvider.getString(R.string.key_room_prefix)))
                                    .map(wifiP2pDevice -> new Room(wifiP2pDevice.deviceName.substring(resourceProvider.getString(R.string.key_room_prefix).length()), wifiP2pDevice.deviceAddress))
                                    .collect(Collectors.toList())
                            );
                        },
                        error -> {
                            getNavigator().handleError((Throwable) error);
                        },
                        () -> {

                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(WifiP2pInfo.class)
                .filter(info -> !(((WifiP2pInfo) info).isGroupOwner))
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        info -> {
                            Log.d("lsc", "LobbyViewModel info " + info);
                            enterRoom(((WifiP2pInfo) info).groupOwnerAddress, 8080);
                        }
                )
        );

        getCompositeDisposable().add(RxEventBus.getInstance().getEvents(String.class)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(message -> getNavigator().showToast((String) message))
        );
    }

    public void discoverPeers() {
        Log.d("lsc", "LobbyViewModel discover");
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

    public void stopPeerDiscovery() {
        Log.d("lsc","stopPeerDiscovery " + (wifiP2pManager == null));
        wifiP2pManager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("lsc", "LobbyViewModel stopPeerDiscovery onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("lsc", "LobbyViewModel stopPeerDiscovery onFailure " + reason);
            }
        });
    }

    public void refresh() {
        stopPeerDiscovery();
        discoverPeers();
    }

    public void goToRoomInfo() {
        getNavigator().goToRoomInfoActivity();
    }

    public void goToSetting() {
        getNavigator().goToSettingActivity();
    }

    public void enterRoom() {
        byte[] ipAddr = new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 100};
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(ipAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        enterRoom(addr, 8080);
    }

    public void enterRoom(InetAddress roomAddress, int roomPort) {
        Log.d("lsc", "LobbyViewModel enterRoom " + roomAddress);
        getCompositeDisposable().add(ConnectionManager.clientThreadObservable(roomAddress, roomPort)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(onNext -> {
                    Log.d("lsc", "LobbyViewModel enterRoom thread " + Thread.currentThread().getName());
                    Log.d("lsc", "LobbyViewModel enterRoom onNext " + onNext);
//                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                }, onError -> {
                    Log.d("lsc", "enterRoom onError " + onError.getMessage());
                }, () -> {
                    Log.d("lsc", "enterRoom terminated");
                }));

    }

//    public void sendMessage() {
//        getCompositeDisposable().add(ConnectionManager.sendMessageCompletable("testByClient")
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .doOnComplete(() -> Log.d("lsc", "LobbyViewModel sendMessage doOnComplete " + Thread.currentThread().getName()))
//                .subscribe(
//                        () -> {
//                            Log.d("lsc", "LobbyViewModel sendMessage onCompleted");
//                        },
//                        error -> {
//                            getNavigator().handleError(error);
//                        }
//                ));
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "LobbyViewModel onCleared");
    }
}
