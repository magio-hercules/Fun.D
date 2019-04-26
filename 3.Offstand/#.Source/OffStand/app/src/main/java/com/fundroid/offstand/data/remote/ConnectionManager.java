package com.fundroid.offstand.data.remote;

import android.util.Log;

import com.fundroid.offstand.data.model.ApiBody;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.fundroid.offstand.core.AppConstant.RESULT_OK;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;

// 한번에 roomMaxAttendee만큼
//Todo : 테스트 -> 나중에 정리
public class ConnectionManager {

    private static List<ServerThread> serverThreads = new ArrayList<>();
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;

    public static Single<Integer> serverProcessor(int apiNo) {
        Log.d("lsc","ConnectionManager serverProcessor " + apiNo);
        return Single.create(subscriber -> {
            switch (apiNo) {
                case API_ENTER_ROOM:
                    // 서버 처리 로직
                    // 서버 처리 로직 end
                    subscriber.onSuccess(RESULT_OK);
                    break;
            }
        });
    }

    public static Single<Integer> createServerThread(int roomPort, int roomMaxUser) {
        return Single.create(subscriber -> {
            serverSocket = new ServerSocket(roomPort);
            while (true) {
                if(serverThreads.size() == 0) {
                    subscriber.onSuccess(RESULT_OK);   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
                }
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads.add(serverThread);

            }

        });
    }

    public static Single<Integer> createClientThread(InetAddress serverIp, int serverPort) {
        return Single.create(subscriber -> {
            Socket socket = new Socket();
            clientThread = new ClientThread(socket, serverIp, serverPort);
            new Thread(clientThread).start();
            subscriber.onSuccess(RESULT_OK);
        });
    }

    public static Completable broadcastMessage(String message) {
        return Completable.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                serverThread.getStreamToClient().writeUTF(message);
            }
        });
    }

    public static Single sendMessage(ApiBody message) {
        Log.d("lsc","ConnectionManager sendMessage " + message);
        return Single.create(subscriber -> {
            clientThread.getStreamToServer().writeUTF(message.toString());
            subscriber.onSuccess(RESULT_OK);
        });
    }

}
