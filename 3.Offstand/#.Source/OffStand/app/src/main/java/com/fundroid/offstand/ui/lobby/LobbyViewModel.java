package com.fundroid.offstand.ui.lobby;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.fundroid.offstand.utils.rx.ServerPublishSubjectBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import io.reactivex.Completable;

public class LobbyViewModel extends BaseViewModel<LobbyNavigator> {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private ResourceProvider resourceProvider;

    public LobbyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.resourceProvider = resourceProvider;

        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(WifiP2pDeviceList.class)
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(
                                peers -> {
//                            Log.d("lsc", "LobbyViewModel peers " + ((WifiP2pDeviceList) peers).getDeviceList());
                                    Log.d("lsc", "LobbyViewModel peers " + ((WifiP2pDeviceList) peers).getDeviceList().size());
//                            getNavigator().onRepositoriesChanged(Stream.of(((WifiP2pDeviceList) peers).getDeviceList())
//                                    .filter(wifiP2pDevice -> wifiP2pDevice.deviceName.contains(resourceProvider.getString(R.string.key_room_prefix)))
//                                    .map(wifiP2pDevice -> new Room(wifiP2pDevice.deviceName.substring(resourceProvider.getString(R.string.key_room_prefix).length()), wifiP2pDevice.deviceAddress))
//                                    .collect(Collectors.toList())
//                            );
                                },
                                error -> {
                                    getNavigator().handleError((Throwable) error);
                                },
                                () -> {

                                }
                        )
        );

        getCompositeDisposable().add(ClientPublishSubjectBus.getInstance().getEvents(WifiP2pInfo.class)
                .filter(info -> !(((WifiP2pInfo) info).isGroupOwner))
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                        info -> {
                            Log.d("lsc", "LobbyViewModel info " + info);
                            enterRoom(((WifiP2pInfo) info).groupOwnerAddress, 8080);
                        }
                )
        );

    }

//    public void discoverPeers() {
//        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.d("lsc", "LobbyViewModel discoverPeers onSuccess");
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Log.d("lsc", "LobbyViewModel discoverPeers onFailure " + reason);
//            }
//
//        });
//    }

     private Completable discoverPeers() {
        return Completable.create(subscriber -> {
            wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d("lsc", "LobbyViewModel discoverPeers onSuccess");
                    subscriber.onComplete();
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("lsc", "LobbyViewModel discoverPeers onFailure " + reason);
                    subscriber.onError(new Throwable("reasonCode : " + reason));
                }

            });
        });
     }

//    public void stopPeerDiscovery() {
//        wifiP2pManager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.d("lsc", "LobbyViewModel stopPeerDiscovery onSuccess");
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Log.d("lsc", "LobbyViewModel stopPeerDiscovery onFailure " + reason);
//            }
//        });
//    }

    private Completable stopPeerDiscovery() {
        return Completable.create(subscriber -> {
            wifiP2pManager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d("lsc", "LobbyViewModel stopPeerDiscovery onSuccess");
                    subscriber.onComplete();
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("lsc", "LobbyViewModel stopPeerDiscovery onFailure " + reason);
                    subscriber.onError(new Throwable("reasonCode : " + reason));
                }
            });
        });
    }

    public void refresh() {
        Log.d("lsc","LobbyViewModel refresh");
//        stopPeerDiscovery();
//        discoverPeers();
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
        getCompositeDisposable().add(ConnectionManager.createClientThread(roomAddress, roomPort)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> {
                    Log.d("lsc", "LobbyViewModel enterRoom thread " + Thread.currentThread().getName());
                    Log.d("lsc", "LobbyViewModel enterRoom onNext ");
//                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                }, onError -> {
                    Log.d("lsc", "enterRoom onError " + onError.getMessage());
                }));

    }

    public void createGroup() {
//        testBoolean = true;
        try {
            Method setDeviceName = wifiP2pManager.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            setDeviceName.setAccessible(true);
            setDeviceName.invoke(wifiP2pManager, channel, resourceProvider.getString(R.string.key_room_prefix) + "테스트임시", new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("lsc", "LobbyViewModel setDeviceName onSuccess");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("lsc", "LobbyViewModel setDeviceName onFailure " + reason);
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            getNavigator().handleError(e);
        }

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
//        testBoolean = false;
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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "LobbyViewModel onCleared");
    }
}
