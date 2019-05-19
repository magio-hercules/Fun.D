package com.fundroid.offstand.data.remote;


import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.fundroid.offstand.core.AppConstant.RESULT_API_NOT_DEFINE;
import static com.fundroid.offstand.data.model.Card.setCardValue;
import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_CARD_OPEN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM_TO_OTHER;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_CANCEL;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_CANCEL_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_AVAILABLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_NOT_AVAILABLE;
import static com.fundroid.offstand.model.User.EnumStatus.CARDOPEN;
import static com.fundroid.offstand.model.User.EnumStatus.DIE;
import static com.fundroid.offstand.model.User.EnumStatus.READY;
import static com.fundroid.offstand.model.User.EnumStatus.STANDBY;

public class ConnectionManager {

    public enum EnumStatus {

        SHUFFLE_NOT_AVAILABLE(0), SHUFFLE_AVAILABLE(1), INGAME(2), GAME_RESULT_AVAILABLE(3);

        private int enumStatus;

        EnumStatus(int enumStatus) {
            this.enumStatus = enumStatus;
        }

        public int getEnumStatus() {
            return enumStatus;
        }
    }

    private static ServerThread[] serverThreads;
    private static int serverCount;
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;
    private static int roomPort;
    private static int roomMaxUser;
    private static EnumStatus roomStatus;

    private static ArrayList<Integer> cards = new ArrayList<>();

    public static Completable createServerThread(int roomPort, int roomMaxUser) {
        ConnectionManager.roomPort = roomPort;
        ConnectionManager.roomMaxUser = roomMaxUser;
        return Completable.create(subscriber -> {
            serverSocket = new ServerSocket(roomPort);
            serverThreads = new ServerThread[roomMaxUser];
            subscriber.onComplete();   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
            socketAcceptLoop();
        });
    }

    private static void socketAcceptLoop() throws IOException {
        while (serverCount != roomMaxUser) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket);
            serverThreads[serverCount] = serverThread;
            serverCount++;
            new Thread(serverThread).start();
        }
    }

    public static Observable<ApiBody> serverProcessor(String apiBodyStr) {
        ApiBody apiBody = new Gson().fromJson(apiBodyStr, ApiBody.class);
        switch (apiBody.getNo()) {
            case API_ENTER_ROOM:
                return setUserSeatNo(apiBody)
                        .flatMap(userServerIndex -> Observable.zip(sendMessage(new ApiBody(API_ROOM_INFO, (ArrayList<User>) Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser()).collect(Collectors.toList())), userServerIndex),
                                broadcastMessageExceptOne(new ApiBody(API_ENTER_ROOM_TO_OTHER, apiBody.getUser()), userServerIndex),
                                (firstOne, secondOne) -> firstOne));

            case API_READY:
                // 모든 유저 레디 시 방장 게임 시작 버튼 활성화
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(getUserStatus())
                        .concatMap(ConnectionManager::setRoomStatus)
                        .concatMap(result -> broadcastMessage(new ApiBody(API_READY_BR, apiBody.getSeatNo())));

            case API_READY_CANCEL:
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(getUserStatus())
                        .concatMap(ConnectionManager::setRoomStatus)
                        .concatMap(result -> broadcastMessage(new ApiBody(API_READY_CANCEL_BR, apiBody.getSeatNo())));

            case API_BAN:
                return broadcastMessageExceptOne(new ApiBody(API_BAN_BR, apiBody.getSeatNo()), apiBody.getSeatNo())
                        .concatMap(result -> closeServerSocket(apiBody.getSeatNo()));

            case API_MOVE:
                return setUserSeatNo(apiBody.getSeatNo(), apiBody.getSeatNo2())
                        .andThen(broadcastMessage(new ApiBody(API_MOVE_BR, apiBody.getSeatNo(), apiBody.getSeatNo2())));

            case API_SHUFFLE:
                return shuffle((ArrayList<ServerThread>) Stream.of(serverThreads).withoutNulls().collect(Collectors.toList()))
                        .flatMap(pair -> sendMessage(new ApiBody(API_SHUFFLE_BR, pair.second.getCards().first, pair.second.getCards().second), pair.first));

            case API_DIE:
                // 죽지 않은 User가 1명 밖에 안남을 경우 게임 결과 이동
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo()).andThen(broadcastMessage(new ApiBody(API_DIE_BR, apiBody.getSeatNo())));

            case API_CARD_OPEN:
                // 모든 유저 카드 오픈 시 방장 게임 결과 버튼 활성화
                return Observable.just(new ApiBody(RESULT_API_NOT_DEFINE));

            case API_GAME_RESULT:
                return Observable.just(new ApiBody(API_GAME_RESULT_BR, 1));

            case API_OUT:
                //Todo : 배열에 다 찰 경우 다시 loop 돌리는 로직 추가해야됨
                return broadcastMessageExceptOne(new ApiBody(API_OUT_BR, apiBody.getSeatNo()), apiBody.getSeatNo())
                        .concatMap(result -> closeServerSocket(apiBody.getSeatNo()));

            default:
                return Observable.just(apiBody);
        }
    }

    private static Observable<Integer> setUserSeatNo(ApiBody apiBody) {
        return Observable.create(subscriber -> {
            int newUserServerIndex = -1;
            for (int index = 0; index < serverThreads.length; index++) {
                if (serverThreads[index] != null && serverThreads[index].getUser() == null) {
                    newUserServerIndex = index;
                    apiBody.getUser().setSeat(index + 1);
                    serverThreads[index].setUser(apiBody.getUser());
                    subscriber.onNext(newUserServerIndex);
                }
            }
        });
    }

    private static Observable<ApiBody> closeServerSocket(int seatNo) {
        return Observable.create(subscriber -> {
            for (int index = 0; index < serverThreads.length; index++) {
                if (serverThreads[index] != null && serverThreads[index].getUser() != null) {
                    if (serverThreads[index].getUser().getSeat().equals(seatNo)) {
                        serverThreads[index].getSocket().close();
                        serverThreads[index] = null;
                        serverCount--;
                    }
                }
            }
        });
    }

    public static Completable createClientThread(@Nullable InetAddress serverIp, int serverPort) {
        return Completable.create(subscriber -> {
            Socket socket = new Socket();
            clientThread = new ClientThread(socket, (serverIp == null) ? InetAddress.getLocalHost() : serverIp, serverPort);
            new Thread(clientThread).start();
            subscriber.onComplete();
        });
    }

    private static Completable setUserSeatNo(int selectedSeat, int targetSeat) {
        boolean isTargetSeatEmpty = Stream.of(serverThreads).withoutNulls().filter(serverThread -> serverThread.getUser().getSeat().equals(targetSeat)).count() == 0;
        if (isTargetSeatEmpty) {
            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
                if (serverThread.getUser().getSeat().equals(selectedSeat)) {
                    serverThread.getUser().setSeat(targetSeat);
                }
            }
        } else {
            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
                if (serverThread.getUser().getSeat().equals(selectedSeat)) {
                    serverThread.getUser().setSeat(-1);
                }
            }
            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
                if (serverThread.getUser().getSeat().equals(targetSeat)) {
                    serverThread.getUser().setSeat(selectedSeat);
                }
            }
            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
                if (serverThread.getUser().getSeat().equals(-1)) {
                    serverThread.getUser().setSeat(targetSeat);
                }
            }
        }
        return Completable.complete();
    }

    private static Completable setUserStatus(int apiNo, int seatNo) {
        for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
            if (serverThread.getUser().getSeat().equals(seatNo)) {
                switch (apiNo) {
                    case API_READY:
                        serverThread.getUser().setStatus(READY.getEnumStatus());
                        break;

                    case API_READY_CANCEL:
                        serverThread.getUser().setStatus(STANDBY.getEnumStatus());
                        break;

                    case API_CARD_OPEN:
                        serverThread.getUser().setStatus(CARDOPEN.getEnumStatus());
                        break;

                    case API_DIE:
                        serverThread.getUser().setStatus(DIE.getEnumStatus());
                        break;
                }
            }
        }

        return Completable.complete();
    }

    private static Observable<EnumStatus> getUserStatus() {
        return Observable.create(subscriber -> {
            // 방장 제외한 나머지 User 리스트
            // 모두 ready 면 방장에게 셔플 가능 api 전송
            int userCountExceptHost = (int) Stream.of(serverThreads)
                    .filterNot(serverThread -> serverThread == null)
                    .filterNot(serverThread -> serverThread.getUser().isHost())
                    .count();

            int readyUserCount = (int) Stream.of(serverThreads)
                    .filterNot(serverThread -> serverThread == null)
                    .filterNot(serverThread -> serverThread.getUser().isHost())
                    .filter(serverThread -> serverThread.getUser().getStatus() == READY.getEnumStatus())
                    .count();

            if (userCountExceptHost == readyUserCount) {
                roomStatus = EnumStatus.SHUFFLE_AVAILABLE;
            } else {
                roomStatus = EnumStatus.SHUFFLE_NOT_AVAILABLE;
            }

            subscriber.onNext(roomStatus);
        });
    }

    private static Observable<ApiBody> setRoomStatus(EnumStatus roomStatus) {
        //Todo : Wifi Direct 연동 후, Room DTO 랑 연결하자 RoomStatus
        switch (roomStatus) {
            case SHUFFLE_AVAILABLE:
                return sendMessage(new ApiBody(API_SHUFFLE_AVAILABLE), serverThreads[0]);

            case SHUFFLE_NOT_AVAILABLE:
                return sendMessage(new ApiBody(API_SHUFFLE_NOT_AVAILABLE), serverThreads[0]);

            default:
                return Observable.just(new ApiBody(RESULT_API_NOT_DEFINE));
        }

    }

    private static Observable<Pair<ServerThread, User>> shuffle(ArrayList<ServerThread> serverThreads) {
        cards.clear();
        for (int i = 1; i < 21; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);
        return Observable.create(subscriber -> {
            for (int i = 0; i < serverThreads.size(); i++) {
                serverThreads.get(i).getUser().setCards(new Pair<>(cards.get(i * 2), cards.get((i * 2) + 1)));
                subscriber.onNext(new Pair<>(serverThreads.get(i), serverThreads.get(i).getUser()));
            }
            roomStatus = EnumStatus.INGAME;
            subscriber.onComplete();
        });
    }

    private static Observable<ApiBody> broadcastMessage(ApiBody message) {
        return Observable.create(subscriber -> {

            for (ServerThread serverThread : serverThreads) {
                if (serverThread != null)
                    serverThread.getStreamToClient().writeUTF(message.toString());
            }
            subscriber.onNext(message);
        });
    }

    private static Observable<ApiBody> broadcastMessageExceptOne(ApiBody message, int serverIndex) {
        return Observable.create(subscriber -> {
            for (int index = 0; index < serverThreads.length; index++) {
                if (serverThreads[index] != null && index != serverIndex)
                    serverThreads[index].getStreamToClient().writeUTF(message.toString());
            }
            subscriber.onNext(message);
        });
    }

    private static Observable<ApiBody> sendMessage(ApiBody message, int serverIndex) {
        return Observable.create(subscriber -> {
            serverThreads[serverIndex].getStreamToClient().writeUTF(message.toString());
            subscriber.onNext(message);
        });
    }

    private static Observable<ApiBody> sendMessage(ApiBody message, ServerThread serverThread) {
        return Observable.create(subscriber -> {
            serverThread.getStreamToClient().writeUTF(message.toString());
            subscriber.onNext(message);
        });
    }

    public static Completable sendMessage(ApiBody message) {
        return Completable.create(subscriber -> {
            clientThread.getStreamToServer().writeUTF(message.toString());
            subscriber.onComplete();
        });
    }

    public static Completable figureOut(ArrayList<User> users) {
        return Completable.create(subscriber -> {
            for (User user : users) {
//                user.getCards()
                setCardValue(user.getCards());
            }
        });
    }
}
