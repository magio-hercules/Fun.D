package com.fundroid.offstand.data.remote;

import android.util.Log;

import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.fundroid.offstand.core.AppConstant.SOCKET_TIMEOUT;

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
        this.socket = socket;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(serverIp, serverPort), SOCKET_TIMEOUT);
            while (true) {
                streamByServer = new DataInputStream(socket.getInputStream());
                streamToServer = new DataOutputStream(socket.getOutputStream());
                String message = streamByServer.readUTF();
                ClientPublishSubjectBus.getInstance().sendEvent(message);
                Log.d("lsc", "서버 -> 클라이언트 " + message);

            }
        } catch (IOException e) {
            Log.e("lsc","ClientThread error " + e.getMessage());
        }

    }

}