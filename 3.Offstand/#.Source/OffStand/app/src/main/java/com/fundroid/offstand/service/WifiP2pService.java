package com.fundroid.offstand.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;

import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

import androidx.annotation.Nullable;
import dagger.android.AndroidInjection;

public class WifiP2pService extends Service implements WifiP2pManager.ChannelListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lsc", "WifiP2pService onCreate");
        AndroidInjection.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("lsc", "WifiP2pService onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lsc", "WifiP2pService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onChannelDisconnected() {
        Log.d("lsc", "WifiP2pService onChannelDisconnected");
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d("lsc", "WifiP2pService onConnectionInfoAvailable " + info.groupOwnerAddress + ", isGroupOwner " + info.isGroupOwner + ", wifiP2pInfo.groupFormed " + info.groupFormed);
        ClientPublishSubjectBus.getInstance().sendEvent(info);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Log.d("lsc", "WifiP2pService onPeersAvailable");
        ClientPublishSubjectBus.getInstance().sendEvent(peers);
    }
}
