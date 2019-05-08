package com.fundroid.offstand.data.remote;

import android.util.Log;

import com.fundroid.offstand.model.User;
import com.fundroid.offstand.utils.rx.RxEventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;
    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;
    private User user;

    public DataInputStream getStreamByClient() {
        return streamByClient;
    }

    public DataOutputStream getStreamToClient() {
        return streamToClient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                streamByClient = new DataInputStream(socket.getInputStream());
                streamToClient = new DataOutputStream(socket.getOutputStream());
                String message = streamByClient.readUTF();
                RxEventBus.getInstance().sendEvent(message);
                Log.d("lsc", "클라이언트 -> 서버 " + message);
            }
        } catch (IOException e) {
            Log.e("lsc", "ServerThread e " + e.getMessage() + ", " + Thread.currentThread().getName());
        }

    }

}