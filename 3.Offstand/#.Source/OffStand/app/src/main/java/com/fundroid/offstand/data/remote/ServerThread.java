package com.fundroid.offstand.data.remote;

import android.util.Log;

import com.fundroid.offstand.data.model.User;
import com.fundroid.offstand.utils.rx.ServerPublishSubjectBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;
    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;
    private User user;
    private final Object lock = new Object();

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
        Log.d("lsc", "ServerThread constructor " + Thread.currentThread().getName());
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (lock) {
//                    Log.d("lsc","ServerThread run socket " + (socket == null));
                    if(socket != null) {
                        streamByClient = new DataInputStream(socket.getInputStream());
                        streamToClient = new DataOutputStream(socket.getOutputStream());
                        String message = streamByClient.readUTF();
                        ServerPublishSubjectBus.getInstance().sendEvent(message);
                        Log.d("lsc", "클라이언트 -> 서버 " + message);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("lsc", "ServerThread e " + e.getMessage() + ", " + Thread.currentThread().getName());
        }

    }

    @Override
    public String toString() {
        return "ServerThread{" +
                "user=" + user +
                '}';
    }
}