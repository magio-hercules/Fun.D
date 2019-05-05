package com.fundroid.offstand.data.remote;

import android.util.Log;

import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Attendee;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.fundroid.offstand.core.AppConstant.RESULT_API_NOT_DEFINE;
import static com.fundroid.offstand.core.AppConstant.RESULT_OK;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM_TO_OTHER;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;

// 한번에 roomMaxAttendee만큼

public class ConnectionManager {

    private static ServerThread[] serverThreads;
    private static int serverCount;
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;

    public static Completable createServerThread(int roomPort, int roomMaxUser) {
        return Completable.create(subscriber -> {
            serverSocket = new ServerSocket(roomPort);
            serverThreads = new ServerThread[roomMaxUser];
            while (serverCount != roomMaxUser) {
                if ((Stream.of(serverThreads).filter(thread -> thread == null).count()) == roomMaxUser) {
                    subscriber.onComplete();   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
                }
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThreads[serverCount] = serverThread;
                serverCount++;
                new Thread(serverThread).start();
            }
        });
    }

    public static Observable<Integer> serverProcessor(String apiBodyStr) {
        ApiBody apiBody = new Gson().fromJson(apiBodyStr, ApiBody.class);
        int newUserServerIndex = -1;
        switch (apiBody.getNo()) {
            case API_ENTER_ROOM:
                for (int index = 0; index < serverThreads.length; index++) {
                    if (serverThreads[index] != null && serverThreads[index].getAttendee() == null) {
                        newUserServerIndex = index;
                        apiBody.getAttendee().setSeatNo(index + 1);
                        serverThreads[index].setAttendee(apiBody.getAttendee());
                    }
                }

                // 서버 처리 로직
                return Observable.zip(
                        sendMessage(new ApiBody(API_ROOM_INFO, (ArrayList<Attendee>)Stream.of(serverThreads).filter(serverThread -> serverThread != null).map(serverThread -> serverThread.getAttendee()).collect(Collectors.toList())), newUserServerIndex),
                        broadcastMessageExceptOne(new ApiBody(API_ENTER_ROOM_TO_OTHER, apiBody.getAttendee()), newUserServerIndex),
                        (firstOne, secondOne) -> RESULT_OK
                );
            default:
                return Observable.just(RESULT_API_NOT_DEFINE);
        }
    }

    public static Completable createClientThread(@Nullable InetAddress serverIp, int serverPort) {
        return Completable.create(subscriber -> {
            Socket socket = new Socket();
            clientThread = new ClientThread(socket, (serverIp == null) ? InetAddress.getLocalHost() : serverIp, serverPort);
            new Thread(clientThread).start();
//            subscriber.onNext(RESULT_OK);
            subscriber.onComplete();
        });
    }

    public static Observable test() {
        return Observable.create(subscriber -> {
            Log.d("lsc", "test complete");
            subscriber.onNext(1);
            subscriber.onComplete();
        });
    }

    public static Observable test2() {
        return Observable.create(subscriber -> {
            Log.d("lsc", "test2 complete");
            subscriber.onComplete();
        });
    }

    public static Single broadcastMessage(ApiBody message) {
        return Single.defer(() -> Single.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                serverThread.getStreamToClient().writeUTF(message.toString());
            }
        }));
    }

    private static Observable<Integer> broadcastMessageExceptOne(ApiBody message, int serverIndex) {
        return Observable.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                if (serverThread != null)
                    serverThread.getStreamToClient().writeUTF(message.toString());
            }
            subscriber.onNext(RESULT_OK);
        });
    }

    private static Observable<Integer> sendMessage(ApiBody message, int serverIndex) {
        return Observable.create(subscriber -> {
            serverThreads[serverIndex].getStreamToClient().writeUTF(message.toString());
            subscriber.onNext(RESULT_OK);
        });
    }

    public static Completable sendMessage(ApiBody message) {
        return Completable.create(subscriber -> {
            clientThread.getStreamToServer().writeUTF(message.toString());
            subscriber.onComplete();
        });
    }
}
