package com.fundroid.offstand.data.model;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

public class Room extends WifiP2pDevice {

    public enum EnumStatus {

        SHUFFLE_NOT_AVAILABLE(0), SHUFFLE_AVAILABLE(1), INGAME(2), GAME_RESULT_AVAILABLE(3);

        private int enumStatus;

        EnumStatus(int enumStatus) {
            this.enumStatus = enumStatus;
        }

        public int getEnumStatus() {
            return enumStatus;
        }
    }

    public String deviceName;
    public String deviceMacAddress;
    private int RoomStatus;

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

    public int getRoomStatus() {
        return RoomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        RoomStatus = roomStatus;
    }

    public void enterRoom() {
        Log.d("lsc", "Room enterRoom");
        ClientPublishSubjectBus.getInstance().sendEvent(this);
    }

}
