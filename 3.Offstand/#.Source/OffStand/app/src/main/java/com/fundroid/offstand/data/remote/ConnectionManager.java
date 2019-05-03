package com.fundroid.offstand.data.remote;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.annimon.stream.Stream;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Attendee;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public static boolean isOpen(ServerSocket ss) {

        return ss.isBound() && !ss.isClosed();

    }

    public static Completable createServerThread(int roomPort, int roomMaxUser) {
        Log.d("lsc", "createServerThread " + Thread.currentThread().getName());
        return Completable.create(subscriber -> {
            Log.d("lsc", "createServerThread in " + Thread.currentThread().getName());
            serverSocket = new ServerSocket(roomPort);
            serverThreads = new ServerThread[roomMaxUser];
            Log.d("lsc", "createServerThread in 2");
            while (true) {
                if ((Stream.of(serverThreads).filter(thread -> thread == null).count()) == roomMaxUser) {
                    Log.d("lsc", "createServerThread onNext");
//                    subscriber.onNext(RESULT_OK);   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
                    subscriber.onComplete();   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
                }
                Log.d("lsc", "createServerThread in 3");
                Socket socket = serverSocket.accept();
                Log.d("lsc", "createServerThread in 4");
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads[serverCount] = serverThread;
                Log.d("lsc", "createServerThread end");
            }
        });
    }

    private static ArrayList<Attendee> attendees = new ArrayList<>();

    public static Observable<Integer> serverProcessor(String apiBodyStr) {
        Log.d("lsc", "ConnectionManager serverProcessor " + apiBodyStr);
        ApiBody apiBody = new Gson().fromJson(apiBodyStr, ApiBody.class);

        return Observable.defer(() -> Observable.create(subscriber -> {
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
                            .observeOn(Schedulers.io())
                            .subscribe(result -> {
                                serverCount++;
                            }, onError -> {
                                Log.e("lsc", "zip error " + onError);
                            });
                    // 서버 처리 로직 END
                    subscriber.onNext(RESULT_OK);
                    break;
            }
        }));
    }

    public static Completable createClientThread(@Nullable InetAddress serverIp, int serverPort) {
        Log.d("lsc", "createClientThread " + Thread.currentThread().getName());
        return Completable.create(subscriber -> {
            Log.d("lsc", "createClientThread in " + Thread.currentThread().getName());
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

    public static Single broadcastMessageExceptOne(ApiBody message, int seatNo) {
        return Single.defer(() -> Single.create(subscriber -> {
            for (ServerThread serverThread : serverThreads) {
                if (serverThread != null)
                    serverThread.getStreamToClient().writeUTF(message.toString());
            }
        }));
    }

    public static Single sendMessage(ApiBody message, int seatNo) {
        return Single.defer(() -> Single.create(subscriber -> {
            serverThreads[seatNo].getStreamToClient().writeUTF(message.toString());
        }));
    }

    public static Completable sendMessage(ApiBody message) {
        Log.d("lsc", "sendMessage " + Thread.currentThread().getName());
        return Completable.create(subscriber -> {
            Log.d("lsc", "sendMessage " + message);
            Log.d("lsc", "sendMessage " + clientThread.getStreamToServer());
            clientThread.getStreamToServer().writeUTF(message.toString());
            subscriber.onComplete();
        });
    }
}
