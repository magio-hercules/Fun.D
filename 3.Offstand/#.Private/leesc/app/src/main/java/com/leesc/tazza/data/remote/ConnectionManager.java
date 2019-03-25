package com.leesc.tazza.data.remote;

import android.util.Log;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.Observable;

// 한번에 roomMaxAttendee만큼
//Todo : 테스트 -> 나중에 정리
public class ConnectionManager {

    private static List<ServerThread> serverThreads = new ArrayList<>();
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;
//    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static Observable<String> serverThreadObservable(int roomPort, int roomMaxAttendee) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager serverThreadObservable create");
            Log.d("lsc", "ConnectionManager serverThreadObservable thread " + Thread.currentThread().getName());
            serverSocket = new ServerSocket(roomPort);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads.add(serverThread);
            }
        });
    }

    public static Observable<String> clientThreadObservable(InetAddress serverIp, int serverPort) {
        return Observable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager clientThreadObservable create");
            Log.d("lsc", "ConnectionManager clientThreadObservable thread " + Thread.currentThread().getName());
            Socket socket = new Socket();
            clientThread = new ClientThread(socket, serverIp, serverPort);
            new Thread(clientThread).start();
        });
    }

    public static Completable broadcastMessageCompletable(String message) {
        Log.d("lsc", "ConnectionManager broadcastMessageCompletable " + serverThreads.size());
        return Completable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager broadcastMessageCompletable thread " + Thread.currentThread().getName());
            for (ServerThread serverThread : serverThreads) {
                serverThread.getStreamToClient().writeUTF(message);
            }
        });
    }

    public static Completable sendMessageCompletable(String message) {
        return Completable.create(subscriber -> {
            Log.d("lsc", "ConnectionManager sendMessageCompletable " + Thread.currentThread().getName());
            clientThread.getStreamToServer().writeUTF(message);
        });
//            return Completable.complete();
    }

}
