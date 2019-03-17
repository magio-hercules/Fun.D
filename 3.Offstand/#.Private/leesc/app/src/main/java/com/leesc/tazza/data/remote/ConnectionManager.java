package com.leesc.tazza.data.remote;

import android.util.Log;

import com.annimon.stream.Stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

//Todo : 테스트 -> 나중에 정리
public class ConnectionManager {

    private static List<ServerThread> serverThreads = new ArrayList<>();
    private static ClientThread clientThread;

    public static Observable<String> serverThreadObservable(int roomPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager serverThreadObservable create");
            ServerSocket serverSocket = new ServerSocket(roomPort);
            ServerThread serverThread = new ServerThread(serverSocket);
            new Thread(serverThread).start();
            serverThreads.add(serverThread);
        });
    }

    public static Observable<String> clientThreadObservable(InetAddress serverIp, int serverPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager clientThreadObservable create");
            Socket socket = new Socket();
            clientThread = new ClientThread(socket, serverIp, serverPort);
            new Thread(clientThread).start();
        });
    }

    public static Completable broadcastMessageCompletable(String message) {
        for (ServerThread serverThread : serverThreads) {
            try {
                serverThread.getStreamToClient().writeUTF(message);
                return Completable.complete();
            } catch (IOException e) {
                return Completable.error(e);
            }
        }
        return Completable.complete();
    }

    public static Completable sendMessageCompletable(String message) {
        try {
            clientThread.getStreamToServer().writeUTF(message);
            return Completable.complete();
        } catch (IOException e) {
            return Completable.error(e);
        }
    }

}
