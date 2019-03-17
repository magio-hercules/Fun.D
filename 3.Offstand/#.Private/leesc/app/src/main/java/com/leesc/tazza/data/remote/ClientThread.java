package com.leesc.tazza.data.remote;

import android.util.Log;

import com.leesc.tazza.utils.rx.RxEventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private InetAddress serverIp;
    private int serverPort;
    private DataInputStream streamByServer = null;
    private DataOutputStream streamToServer = null;

    public DataInputStream getStreamByServer() {
        return streamByServer;
    }

    public DataOutputStream getStreamToServer() {
        return streamToServer;
    }

    public ClientThread(Socket socket, InetAddress serverIp, int serverPort) {
        Log.i("lsc", "ClientThread constructor " + Thread.currentThread().getId());
        this.socket = socket;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Log.d("lsc", "ClientThread 1");
        try {
            socket.connect(new InetSocketAddress(serverIp, serverPort), 30000);
            while (true) {
                Log.d("lsc", "ClientThread 2");
                streamByServer = new DataInputStream(socket.getInputStream());
                Log.d("lsc", "ClientThread 3");
                streamToServer = new DataOutputStream(socket.getOutputStream());
//                Log.d("lsc", "ClientThread 서버로부터 받은 메세지 " + streamByServer.readUTF());
                RxEventBus.getInstance().sendEvent(streamByServer.readUTF());

            }
        } catch (IOException e) {
            Log.e("lsc","ClientThread error " + e.getMessage());
        }

    }

}