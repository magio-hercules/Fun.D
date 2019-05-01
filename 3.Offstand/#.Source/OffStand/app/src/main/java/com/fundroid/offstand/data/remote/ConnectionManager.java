package com.fundroid.offstand.data.remote;

import android.util.Log;

import androidx.core.util.Pair;

import com.annimon.stream.Stream;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Attendee;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.fundroid.offstand.core.AppConstant.RESULT_OK;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM_TO_OTHER;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;

// 한번에 roomMaxAttendee만큼

public class ConnectionManager {

    private static ServerThread serverThreads[];
    private static int serverCount;
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;

    public static Single<Integer> createServerThread(int roomPort, int roomMaxUser) {
        return Single.defer(() -> Single.create(subscriber -> {
            serverSocket = new ServerSocket(roomPort);
            serverThreads = new ServerThread[roomMaxUser];
            while (true) {
                if ((Stream.of(serverThreads).filter(serverThread -> serverThread == null).count()) == roomMaxUser) {
                    subscriber.onSuccess(RESULT_OK);   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
                }
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads[serverCount] = serverThread;
            }
        }));
    }

    private static ArrayList<Attendee> attendees = new ArrayList<>();

    public static Observable<Integer> serverProcessor(String apiBodyStr) {
        Log.d("lsc", "ConnectionManager serverProcessor " + apiBodyStr);
        ApiBody apiBody = new Gson().fromJson(apiBodyStr, ApiBody.class);

        return Observable.create(subscriber -> {
            switch (apiBody.getNo()) {
                case API_ENTER_ROOM:
                    apiBody.getAttendee().setSeatNo(serverCount);
                    serverThreads[serverCount].setAttendee(apiBody.getAttendee());
                    // 서버 처리 로직
                    Single.zip(
                            sendMessage(new ApiBody(API_ROOM_INFO, attendees), serverCount),
                            broadcastMessageExceptOne(new ApiBody(API_ENTER_ROOM_TO_OTHER, apiBody.getAttendee()), apiBody.getAttendee().getSeatNo()),
                            (firstOne, secondOne) -> RESULT_OK
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                serverCount++;
                            }, onError -> {
                                Log.e("lsc", "zip error " + onError);
                            });
                    // 서버 처리 로직 END
                    subscriber.onNext(RESULT_OK);
                    break;
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

    public static Single broadcastMessage(ApiBody message) {
        return Single.defer(() -> Single.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                serverThread.getStreamToClient().writeUTF(message.toString());
            }
        }));
    }

    public static Single broadcastMessageExceptOne(ApiBody message, int seatNo) {
        return Single.defer(() -> Single.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                if (serverThread != null)
                    serverThread.getStreamToClient().writeUTF(message.toString());
            }
        }));
    }

    public static Single sendMessage(ApiBody message, int seatNo) {
//        Log.d("lsc","sendMessage " + message + ", seatNo " + seatNo);
        return Single.defer(() -> Single.create(subscriber -> {
            serverThreads[seatNo].getStreamToClient().writeUTF(message.toString());
        }));
    }

    public static Single sendMessage(ApiBody message) {
//        Log.d("lsc", "ConnectionManager sendMessage " + message);
        return Single.defer(() -> Single.create(subscriber -> {
            Log.d("lsc","sendMessage " + clientThread);
            Log.d("lsc","sendMessage " + clientThread.getStreamToServer());
            clientThread.getStreamToServer().writeUTF(message.toString());
            subscriber.onSuccess(RESULT_OK);
        }));
    }
}
