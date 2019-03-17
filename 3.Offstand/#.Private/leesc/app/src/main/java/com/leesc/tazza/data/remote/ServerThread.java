package com.leesc.tazza.data.remote;

import android.util.Log;

import com.leesc.tazza.utils.rx.RxEventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;

    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;

    public DataInputStream getStreamByClient() {
        return streamByClient;
    }

    public DataOutputStream getStreamToClient() {
        return streamToClient;
    }

    public ServerThread(ServerSocket serverSocket) {
        Log.i("lsc", "ServerThread constructor " + Thread.currentThread().getName());
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        Log.d("lsc", "ServerThread 1");
        try {
            socket = serverSocket.accept();
            while (true) {
                Log.d("lsc", "ServerThread 2");
                streamByClient = new DataInputStream(socket.getInputStream());
                Log.d("lsc", "ServerThread 3");
                streamToClient = new DataOutputStream(socket.getOutputStream());
//                Log.d("lsc", "ServerThread 클라이언트로부터 받은 메세지 " + streamByClient.readUTF());
                RxEventBus.getInstance().sendEvent(streamByClient.readUTF());
            }
        } catch (IOException e) {
            Log.d("lsc", "ServerThread e " + e.getMessage());
        }

    }

}