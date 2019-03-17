package com.leesc.tazza.ui.main;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private ServerSocket socket;

    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;
    private DataInputStream streamByServer = null;
    private DataOutputStream streamToServer = null;


    public ServerThread(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){

    }

}