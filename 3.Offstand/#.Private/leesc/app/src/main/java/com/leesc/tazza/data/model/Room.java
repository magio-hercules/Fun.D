package com.leesc.tazza.data.model;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

import com.leesc.tazza.utils.rx.RxEventBus;

public class Room extends WifiP2pDevice {

    public String deviceName;
    public String deviceMacAddress;

    public Room(String deviceName, String deviceMacAddress) {
        this.deviceName = deviceName;
        deviceMacAddress = this.deviceAddress = deviceMacAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void enterRoom() {
        Log.d("lsc", "Room enterRoom");
        RxEventBus.getInstance().sendEvent(this);
    }

}
