package com.fundroid.offstand.data.remote;


import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.model.Card;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.data.model.User;
import com.fundroid.offstand.utils.NetworkUtils;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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

import static com.fundroid.offstand.core.AppConstant.COLLECTION_ROOMS;
import static com.fundroid.offstand.core.AppConstant.RESULT_API_NOT_DEFINE;
import static com.fundroid.offstand.data.model.Card.setCardValue;
import static com.fundroid.offstand.data.model.Room.EnumStatus.REGAME;
import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_CARD_OPEN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM_TO_OTHER;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT_AVAILABLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT_SELF;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_CANCEL;
import static com.fundroid.offstand.data.remote.ApiDefine.API_READY_CANCEL_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ROOM_INFO;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_AVAILABLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_NOT_AVAILABLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_TEST;
import static com.fundroid.offstand.data.model.User.EnumStatus.CARDOPEN;
import static com.fundroid.offstand.data.model.User.EnumStatus.DIE;
import static com.fundroid.offstand.data.model.User.EnumStatus.INGAME;
import static com.fundroid.offstand.data.model.User.EnumStatus.READY;
import static com.fundroid.offstand.data.model.User.EnumStatus.STANDBY;

public class ConnectionManager {

    private static ServerThread[] serverThreads;
    private static int serverCount;
    private static ClientThread clientThread;
    private static ServerSocket serverSocket;
    private static int roomMaxUser;
    private static Room.EnumStatus roomStatus = Room.EnumStatus.SHUFFLE_NOT_AVAILABLE;
    private static ArrayList<Integer> cards = new ArrayList<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String roomDocumentId;

    public static Completable insertRoom(String roomName) {
        return NetworkUtils.getIpAddress()
                .flatMapCompletable(myIp -> Completable.create(subscriber -> {
                    Log.d("lsc", "ConnectionManager insertRoom " + myIp);
                    roomDocumentId = roomName + "(" + myIp + ")";
                    Room room = new Room(roomDocumentId, roomName, myIp);
                    DocumentReference documentReference = db.collection(COLLECTION_ROOMS).document(roomDocumentId);
                    db.runTransaction(transaction -> {
                        DocumentSnapshot snapshot = transaction.get(documentReference);
                        if (!snapshot.exists()) {
                            transaction.set(documentReference, room);
                        }
                        return null;
                    })
                            .addOnSuccessListener(onSuccess -> subscriber.onComplete())
                            .addOnFailureListener(subscriber::onError);
                }));
    }

    public static Observable selectRooms() {
        return Observable.create(subscriber -> {
            db.collection(COLLECTION_ROOMS)
                    .get()
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public static Observable<QuerySnapshot> syncRooms() {
        return Observable.create(subscriber ->
                db.collection(COLLECTION_ROOMS)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> subscriber.onNext(queryDocumentSnapshots)));
    }

    public static Completable deleteRoom() {
        Log.d("lsc", "ConnectionManager deleteRoom");
        return Completable.create(subscriber -> db.collection(COLLECTION_ROOMS).document(roomDocumentId).delete().addOnSuccessListener(Void -> subscriber.onComplete()).addOnFailureListener(subscriber::onError));
    }


    public static Completable createServerThread(int roomPort, int roomMaxUser) {
        ConnectionManager.roomMaxUser = roomMaxUser;
        return Completable.create(subscriber -> {
            serverSocket = new ServerSocket(roomPort);
            serverThreads = new ServerThread[roomMaxUser];
            subscriber.onComplete();   // accept에서 blocking 되니 방장 클라이언트가 붙기전에 보냄
            Log.d("lsc", "ConnectionManager createServerThread " + Thread.currentThread().getName());
            socketAcceptLoop();
        });
    }

    private static void socketAcceptLoop() {
        Log.d("lsc", "socketAcceptLoop " + serverSocket.getInetAddress().getHostAddress());
//        while (serverCount != roomMaxUser) {
//            Socket socket = null;
//            try {
//                socket = serverSocket.accept();
//            } catch (IOException e) {
//                Log.e("lsc", "socketAcceptLoop e " + e.getMessage());
//                ClientPublishSubjectBus.getInstance().sendEvent(new ApiBody(API_OUT_SELF).toString());
//                break;
//            }
//            ServerThread serverThread = new ServerThread(socket);
//            serverThreads[serverCount] = serverThread;
//            serverCount++;
//            new Thread(serverThread).start();
//        }

        while (true) {
            if (serverCount < roomMaxUser) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e("lsc", "socketAcceptLoop e " + e.getMessage());
                    ClientPublishSubjectBus.getInstance().sendEvent(new ApiBody(API_OUT_SELF).toString());
                    break;
                }
                ServerThread serverThread = new ServerThread(socket);
                int index = 0;
                for (int i = 0; i < roomMaxUser; i++) {
                    if (serverThreads[i] == null) {
                        index = i;
                        break;
                    }
                }
                serverThreads[index] = serverThread;
                serverCount++;
                new Thread(serverThread).start();
            }
        }
    }

    private static ArrayList<User> swapToFirst(ArrayList<User> users, int seatNo) {
        Log.d("lsc", "ConnectionManager swapToFirst " + users + ", " + seatNo);
        for (int i = 0; i < users.size(); i++) {
            Log.d("lsc", "ConnectionManager swapToFirst for " + users.get(i));
            if (users.get(i) != null && users.get(i).getSeat() == seatNo) {
                Collections.swap(users, 0, i);
            }
        }
        return users;
    }

    public static Observable<ApiBody> serverProcessor(String apiBodyStr) {
        Log.v("lsc", "serverProcessor " + apiBodyStr);
        ApiBody apiBody = new Gson().fromJson(apiBodyStr, ApiBody.class);
        switch (apiBody.getNo()) {
            case API_ENTER_ROOM:
                return
                        setUserSeatNo(apiBody)
                                .flatMap(seatNo -> Observable.zip(sendMessage(new ApiBody(API_ROOM_INFO, swapToFirst((ArrayList<User>) Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser()).collect(Collectors.toList()), seatNo)), seatNo),
                                        broadcastMessageExceptOne(new ApiBody(API_ENTER_ROOM_TO_OTHER, apiBody.getUser()), seatNo), (firstOne, secondOne) -> firstOne))
                                .concatMap(firstOne -> setRoomStatus())
                                .concatMap(ConnectionManager::sendToHost);

            case API_READY:
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(setRoomStatus())
                        .concatMap(ConnectionManager::sendToHost)
                        .concatMap(result -> broadcastMessage(new ApiBody(API_READY_BR, apiBody.getSeatNo())));

            case API_READY_CANCEL:
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(setRoomStatus())
                        .concatMap(ConnectionManager::sendToHost)
                        .concatMap(result -> broadcastMessage(new ApiBody(API_READY_CANCEL_BR, apiBody.getSeatNo())));

            case API_BAN:
                return broadcastMessageExceptOne(new ApiBody(API_BAN_BR, apiBody.getSeatNo()), apiBody.getSeatNo())
                        .concatMap(result -> closeServerSocket(apiBody.getSeatNo()))
                        .concatMap(result -> setRoomStatus())
                        .concatMap(ConnectionManager::sendToHost);

            case API_MOVE:
                return setUserSeatNo(apiBody.getSeatNo(), apiBody.getSeatNo2())
                        .andThen(broadcastMessage(new ApiBody(API_MOVE_BR, apiBody.getSeatNo(), apiBody.getSeatNo2())));

            case API_SHUFFLE:
                return shuffle((ArrayList<ServerThread>) Stream.of(serverThreads).withoutNulls().collect(Collectors.toList()))
                        .flatMap(pair -> {
                            if (roomStatus == REGAME) {
                                if (pair.second.getStatus() == DIE.getEnumStatus()) {
                                    Log.d("lsc", "REGAME DIE BR " + pair.second.getSeat());
                                    return sendMessage(new ApiBody(API_DIE_BR, pair.second.getSeat()), pair.second.getSeat());
                                } else {
                                    Log.d("lsc", "REGAME SHUFFLE before " + pair.first.getUser().getSeat() + ", " + pair.second.getStatus());
                                    pair.second.setStatus(INGAME.getEnumStatus());
                                    Log.d("lsc", "REGAME SHUFFLE after " + pair.first.getUser().getSeat() + ", " + pair.second.getStatus());
                                    return sendMessage(new ApiBody(API_SHUFFLE_BR, pair.second.getCards().first, pair.second.getCards().second), pair.first);
                                }
                            } else {
                                return sendMessage(new ApiBody(API_SHUFFLE_BR, pair.second.getCards().first, pair.second.getCards().second), pair.first);
                            }

                        });

            case API_DIE:
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(setRoomStatus())
                        .concatMap(ConnectionManager::sendToHost)
                        .concatMap(result -> sendMessage(new ApiBody(API_DIE_BR, apiBody.getSeatNo()), apiBody.getSeatNo()));

            case API_CARD_OPEN:
                return setUserStatus(apiBody.getNo(), apiBody.getSeatNo())
                        .andThen(setRoomStatus())
                        .concatMap(ConnectionManager::sendToHost)
                        .concatMap(result -> Observable.just(new ApiBody(RESULT_API_NOT_DEFINE)));

            case API_GAME_RESULT:
                return
                        setCardSumAndLevel((ArrayList<User>) Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser()).collect(Collectors.toList()))
                                .flatMap(ConnectionManager::setSumRebalance)
                                .flatMap(users -> sortByUserSum())
                                .flatMap(ConnectionManager::checkRematch)
                                .flatMapObservable(users -> {
                                    Log.d("lsc", "GAME_RESULT_BR roomStatus " + roomStatus);
                                    return broadcastMessage(new ApiBody(API_GAME_RESULT_BR, users, roomStatus.getEnumStatus() == REGAME.getEnumStatus()));
                                });


            case API_OUT:
                //Todo : 배열에 다 찰 경우 다시 loop 돌리는 로직 추가해야됨
                if (apiBody.getSeatNo().equals(serverThreads[0].getUser().getSeat())) {
                    return deleteRoom()
                            .andThen(closeAllServerSocket());
                } else {
                    return broadcastMessageExceptOne(new ApiBody(API_OUT_BR, apiBody.getSeatNo()), apiBody.getSeatNo())
                            .concatMap(result -> closeServerSocket(apiBody.getSeatNo()))
                            .concatMap(result -> setRoomStatus())
                            .concatMap(ConnectionManager::sendToHost);
                }


            case API_TEST:
                for (User user : Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser()).collect(Collectors.toList())) {
                    Log.d("lsc", "user " + user);
                }
                return Observable.just(new ApiBody(RESULT_API_NOT_DEFINE));

            default:
                Log.d("lsc", "ConnectionManager default api " + apiBody);
                return Observable.just(new ApiBody(RESULT_API_NOT_DEFINE));
        }
    }

    private static Observable<Integer> setUserSeatNo(ApiBody apiBody) {
        Log.d("lsc", "ConnectionManager setUserSeatNo api " + apiBody);
        return Observable.create(subscriber -> {
            int seatNo = 1;
            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().collect(Collectors.toList())) {
                if (serverThread.getUser() == null) {
                    apiBody.getUser().setSeat(seatNo);
                    serverThread.setUser(apiBody.getUser());
                    subscriber.onNext(seatNo);
                } else {
                    if (serverThread.getUser().getSeat() == seatNo) {
                        seatNo += 1;
                    }
                }
            }
        });
    }

    private static Observable<ApiBody> closeServerSocket(int seatNo) {
        return Observable.create(subscriber -> {
            for (int index = 0; index < serverThreads.length; index++) {
                if (serverThreads[index] != null && serverThreads[index].getUser() != null) {
                    if (serverThreads[index].getUser().getSeat().equals(seatNo)) {
                        Log.d("lsc", "closeServerSocket seatNo " + seatNo);
                        serverThreads[index].getSocket().close();
                        serverThreads[index] = null;
                        serverCount--;
                    }
                }
            }
            subscriber.onNext(new ApiBody(RESULT_API_NOT_DEFINE));
        });
    }

    private static Observable<ApiBody> closeAllServerSocket() {
        return Observable.create(subscriber -> {
            serverSocket.close();
            serverCount = 0;
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

    private static Observable<Room.EnumStatus> setRoomStatus() {
        Log.d("lsc", "ConnectionManager setRoomStatus " + roomStatus);
        return Observable.create(subscriber -> {
            // 방장 제외한 나머지 User 리스트
            // 모두 ready 면 방장에게 셔플 가능 api 전송
            int userCountExceptHost = (int) Stream.of(serverThreads)
                    .withoutNulls()
                    .filterNot(serverThread -> serverThread.getUser().isHost())
                    .count();

            int readyUserCount = (int) Stream.of(serverThreads)
                    .withoutNulls()
                    .filterNot(serverThread -> serverThread.getUser().isHost())
                    .filter(serverThread -> serverThread.getUser().getStatus() == READY.getEnumStatus())
                    .count();

            int inGameUserCount = (int) Stream.of(serverThreads) // 게임 시작 후 카드 오픈 or DIE or 나가기를 하지 않은 User 카운트
                    .withoutNulls()
                    .filter(serverThread -> serverThread.getUser().getStatus() == INGAME.getEnumStatus())
                    .count();

            int dieUserCount = (int) Stream.of(serverThreads)
                    .withoutNulls()
                    .filter(serverThread -> serverThread.getUser().getStatus() == DIE.getEnumStatus())
                    .count();

            int allGameUserCount = (int) Stream.of(serverThreads)
                    .withoutNulls()
                    .count();

            switch (roomStatus) {
                case SHUFFLE_NOT_AVAILABLE:
                case SHUFFLE_AVAILABLE:
                    if (userCountExceptHost == readyUserCount) {
                        roomStatus = Room.EnumStatus.SHUFFLE_AVAILABLE;
                    } else {
                        roomStatus = Room.EnumStatus.SHUFFLE_NOT_AVAILABLE;
                    }
                    break;

                case INGAME:
                case GAME_RESULT_AVAILABLE:
                    if (allGameUserCount == dieUserCount + 1) {
                        roomStatus = Room.EnumStatus.AUTO_RESULT;
                    } else if (inGameUserCount == 0) {
                        roomStatus = Room.EnumStatus.GAME_RESULT_AVAILABLE;
                    } else {
                        roomStatus = Room.EnumStatus.INGAME;
                    }
                    break;

                case REGAME:
                    roomStatus = Room.EnumStatus.INGAME;
                    break;
            }
            Log.d("lsc", "setRoomStatus end allGameUserCount " + allGameUserCount);
            Log.d("lsc", "setRoomStatus end inGameUserCount " + inGameUserCount);
            Log.d("lsc", "setRoomStatus end roomStatus " + roomStatus);
            subscriber.onNext(roomStatus);
        });
    }

    private static Observable<ApiBody> sendToHost(Room.EnumStatus roomStatus) {
        Log.d("lsc", "sendToHost " + roomStatus);
        switch (roomStatus) {
            case SHUFFLE_AVAILABLE:
                return sendMessage(new ApiBody(API_SHUFFLE_AVAILABLE), serverThreads[0]);

            case SHUFFLE_NOT_AVAILABLE:
                return sendMessage(new ApiBody(API_SHUFFLE_NOT_AVAILABLE), serverThreads[0]);

            case GAME_RESULT_AVAILABLE:
                return sendMessage(new ApiBody(API_GAME_RESULT_AVAILABLE), serverThreads[0]);

            case AUTO_RESULT:
//                return sendMessage(new ApiBody(API_GAME_RESULT_AVAILABLE), serverThreads[0])
//                        .concatMap(apiBody -> serverProcessor(new ApiBody(API_GAME_RESULT).toString()));
                return serverProcessor(new ApiBody(API_GAME_RESULT).toString());

            default:
                return Observable.just(new ApiBody(RESULT_API_NOT_DEFINE));
        }
    }

    public static Single<ArrayList<User>> setCardSumAndLevel(ArrayList<User> users) {
        Log.d("lsc", "setCardSumAndLevel");
        return Single.create(subscriber -> {
            for (User user : Stream.of(users).filter(user -> user.getStatus() == CARDOPEN.getEnumStatus() || user.getStatus() == DIE.getEnumStatus()).toList()) {
                setCardValue(user);
                Log.d("lsc", "setCardSumAndLevel " + user);
            }
            subscriber.onSuccess(users);
        });
    }

    private static Single<ArrayList<User>> sortByUserSum() {
        return Single.create(subscriber -> {
            ArrayList<User> targetUsers = (ArrayList<User>) Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser()).collect(Collectors.toList());
            if (targetUsers.size() > 1) {
                Collections.sort(targetUsers);
                Collections.reverse(targetUsers);
            }
            subscriber.onSuccess(targetUsers);
        });
    }

    private static Single<ArrayList<User>> checkRematch(ArrayList<User> users) {
        for (User user : users) {
            Log.d("lsc", "checkRematch users " + user);
        }
        return Single.create(subscriber -> {
            //승리자 LEVEL이 3 또는 7일 경우
            if (users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL3.getCardLevel() || users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL7.getCardLevel()) {
                if (users.size() > 1 && users.get(1).getStatus() == DIE.getEnumStatus()) {
                    roomStatus = Room.EnumStatus.INGAME;
                } else {
                    roomStatus = Room.EnumStatus.REGAME;
                }
            }

            Log.d("lsc", "checkRematch 1");

            //카드 급이 같을 경우 (죽지 않고 동점이 아닌 사람의 STATUS 를 INGAME으로 셋)
            Stream.of(serverThreads).withoutNulls().map(serverThread -> serverThread.getUser())
                    .filterNot(user -> user.getStatus() == DIE.getEnumStatus())
                    .filterNot(user ->
                            !(users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL3.getCardLevel()
                                    || users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL7.getCardLevel())
                                    && users.get(0).getCardSum() == user.getCardSum()
                    )  //1등 94인 경우, 모든 유저, 94가 아닌 경우, 동률이 아닌 유저들

                    .map(loseUser -> {  // 1등이 아닌 애들 (94가 아닌경우), 94이면 모든 유저
                        if (users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL3.getCardLevel() || users.get(0).getCardLevel() == Card.EnumCardLevel.LEVEL7.getCardLevel()) {
                            loseUser.setStatus(INGAME.getEnumStatus());
                        } else if (users.size() > 1 && users.get(0).getCardSum() != users.get(1).getCardSum()) {
                        } else {
                            loseUser.setStatus(DIE.getEnumStatus());
                        }
                        return loseUser;
                    })

                    .collect(Collectors.toList());


            Log.d("lsc", "checkRematch 2");
            if (users.size() > 1 && users.get(0).getCardSum() == users.get(1).getCardSum()) {
                roomStatus = REGAME;
            }
            subscriber.onSuccess(users);
        });
    }

    private static Single<ArrayList<User>> setSumRebalance(ArrayList<User> users) {
        return Single.create(subscriber -> {
            if (Stream.of(users).filter(user -> user.getCardLevel() == Card.EnumCardLevel.LEVEL4.getCardLevel()).count() == 0) {
                Stream.of(users).filter(user -> user.getCardLevel() == Card.EnumCardLevel.LEVEL5.getCardLevel()).findFirst().ifPresent(level5User -> level5User.setCardSum(0));
            }
            if (Stream.of(users).filter(user -> user.getCardLevel() == Card.EnumCardLevel.LEVEL8.getCardLevel()).count() == 0) {
                Stream.of(users).filter(user -> user.getCardLevel() == Card.EnumCardLevel.LEVEL9.getCardLevel()).findFirst().ifPresent(level9User -> level9User.setCardSum(0));
            }
            subscriber.onSuccess(users);
        });
    }

    private static Observable<Pair<ServerThread, User>> shuffle(ArrayList<ServerThread> serverThreads) {
        cards.clear();
        for (int i = 1; i < 21; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);
        return Observable.create(subscriber -> {
            for (int i = 0; i < serverThreads.size(); i++) {
//                Log.d("lsc", "shuffle " + cards.get(i * 2) + ", " + cards.get((i * 2) + 1));
                serverThreads.get(i).getUser().setCardSum(-2);
                if (cards.get(i * 2) < cards.get((i * 2) + 1)) {
                    serverThreads.get(i).getUser().setCards(new Pair<>(cards.get(i * 2), cards.get((i * 2) + 1)));
                } else {
                    serverThreads.get(i).getUser().setCards(new Pair<>(cards.get((i * 2) + 1), cards.get(i * 2)));
                }
                if (roomStatus == REGAME) {

                } else {
                    serverThreads.get(i).getUser().setStatus(INGAME.getEnumStatus());
                }
                Log.d("lsc", "shuffle userName " + serverThreads.get(i).getUser().getName() + ", userStatus " + serverThreads.get(i).getUser().getStatus() + ", roomStatus " + roomStatus);
//                serverThreads.get(0).getUser().setCards(new Pair<>(4, 9));//팔땡
//                serverThreads.get(1).getUser().setCards(new Pair<>(8, 18));//멍구사
                subscriber.onNext(new Pair<>(serverThreads.get(i), serverThreads.get(i).getUser()));
            }
            roomStatus = Room.EnumStatus.INGAME;
            subscriber.onComplete();
        });
    }

    private static Observable<ApiBody> broadcastMessage(ApiBody message) {
        Log.d("lsc", "ConnectionManager broadcastMessage " + message);
        return Observable.create(subscriber -> {

            for (ServerThread serverThread : serverThreads) {
                if (serverThread != null) {
                    serverThread.getStreamToClient().writeUTF(message.toString());
                }
            }
            subscriber.onNext(message);
        });
    }

    private static Observable<ApiBody> broadcastMessageExceptOne(ApiBody message, int seatNo) {
        return Observable.create(subscriber -> {

            for (ServerThread serverThread : Stream.of(serverThreads).withoutNulls().filterNot(serverThread -> serverThread.getUser().getSeat() == seatNo).collect(Collectors.toList())) {
                serverThread.getStreamToClient().writeUTF(message.toString());
            }
            subscriber.onNext(message);
        });
    }

    private static Observable<ApiBody> sendMessage(ApiBody message, int seatNo) {
        return Observable.create(subscriber -> {
            Stream.of(serverThreads)
                    .withoutNulls()
                    .filter(serverThread -> serverThread.getUser().getSeat() == seatNo)
                    .findFirst()
                    .get()
                    .getStreamToClient()
                    .writeUTF(message.toString());

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
}
