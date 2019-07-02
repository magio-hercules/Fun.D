package com.fundroid.offstand.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import io.reactivex.Single;

public class NetworkUtils {
    public static Single<String> getIpAddress() {
        StringBuilder ip = new StringBuilder();
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip.append(inetAddress.getHostAddress());
                        return Single.just(ip.toString());
                    }
                }
            }

        } catch (SocketException e) {
            return Single.error(e);
        }

        return Single.just(ip.toString());
    }


}
