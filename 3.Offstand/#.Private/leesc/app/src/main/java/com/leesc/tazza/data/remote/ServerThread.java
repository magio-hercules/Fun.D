package com.leesc.tazza.data.remote;

import android.util.Log;

import com.leesc.tazza.utils.rx.RxEventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread implements Runnable {
    private Socket socket;
    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;

    public DataInputStream getStreamByClient() {
        return streamByClient;
    }

    public DataOutputStream getStreamToClient() {
        return streamToClient;
    }

    public ServerThread(Socket socket) {
        Log.i("lsc", "ServerThread constructor " + Thread.currentThread().getName());
        this.socket = socket;
    }

    @Override
    public void run() {
        Log.d("lsc", "ServerThread 1 " + Thread.currentThread().getName());
        try {
            while (true) {
                Log.d("lsc", "ServerThread 2 " + Thread.currentThread().getName());
                streamByClient = new DataInputStream(socket.getInputStream());
                Log.d("lsc", "ServerThread 3 " + Thread.currentThread().getName());
                streamToClient = new DataOutputStream(socket.getOutputStream());
//                Log.d("lsc", "ServerThread 클라이언트로부터 받은 메세지 " + streamByClient.readUTF());
                RxEventBus.getInstance().sendEvent(streamByClient.readUTF());
//                clientThreads.add(serverThread);
            }
        } catch (IOException e) {
            Log.e("lsc", "ServerThread e " + e.getMessage() + ", " + Thread.currentThread().getName());
        }

    }

}