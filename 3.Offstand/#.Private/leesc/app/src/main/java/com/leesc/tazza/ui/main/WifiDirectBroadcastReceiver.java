package com.leesc.tazza.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import com.leesc.tazza.service.WifiP2pService;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager.PeerListListener peerListListener;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;
    public WifiP2pDevice myDevice;

    public WifiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, WifiP2pManager.PeerListListener peerListListener, WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.peerListListener = peerListListener;
        this.connectionInfoListener = connectionInfoListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, WifiP2pService.class);
        serviceIntent.setAction(intent.getAction());
        serviceIntent.putExtras(intent);
        context.startService(serviceIntent);


//        String action = intent.getAction();
//        Log.d("lsc", "WifiDirectBroadcastReceiver onReceive action " + action);
//        switch (action) {
//            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
//
//                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
//                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
//                    Log.d("lsc", "wifi enable");
//                } else {
//                    Log.d("lsc", "wifi disable");
//                }
//                break;
//
//            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
//                Log.d("lsc", "WIFI_P2P_PEERS_CHANGED_ACTION");
//                if (mManager != null) {
//                    mManager.requestPeers(mChannel, peerListListener);
//                }
//                break;
//
//            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
//                if (mManager == null) {
//                    return;
//                }
//                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//                Log.d("lsc", "networkInfo " + networkInfo.isConnected());
//                if (networkInfo.isConnected()) {
//                    mManager.requestConnectionInfo(mChannel, connectionInfoListener);
//                } else {
//                    Log.d("lsc", "disconnect");
//                }
//                break;
//
//            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
//                myDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//                break;
//        }

    }


}
