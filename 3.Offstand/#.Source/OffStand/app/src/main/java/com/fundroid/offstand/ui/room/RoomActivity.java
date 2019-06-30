package com.fundroid.offstand.ui.room;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.data.model.User;
import com.fundroid.offstand.data.model.UserWrapper;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.play.PlayActivity;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.fundroid.offstand.utils.rx.BehaviorSubjectBus;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_BAN_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_ENTER_ROOM_TO_OTHER;
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

@SuppressLint("CheckResult")
public class RoomActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
//public class RoomActivity extends AppCompatActivity {

    static final String TAG = "[ROOM]";

    static final int MAX_USER_COUNT = 4;

    @Inject
    DataManager dataManager;

    SharedPreferences sharedPreferences;

    // org
    @BindView(R.id.btn_table)
    Button btn_table;
    @BindView(R.id.btn_ban)
    Button btn_ban;
    @BindView(R.id.btn_shuffle)
    Button btn_shuffle;
    @BindView(R.id.btn_ready)
    Button btn_ready;
    @BindView(R.id.btn_1)
    Button btn_1;
    @BindView(R.id.btn_2)
    Button btn_2;
    @BindView(R.id.btn_3)
    Button btn_3;
    @BindView(R.id.btn_4)
    Button btn_4;

    // real
    @BindView(R.id.room_image_start)
    ImageView image_start;
    @BindView(R.id.room_image_ready)
    ImageView image_ready;
    @BindView(R.id.room_image_exit)
    ImageView image_exit;
    @BindView(R.id.room_image_ban)
    ImageView image_ban;

    @BindView(R.id.room_avatar_1)
    ImageView image_avatar_1;
    @BindView(R.id.room_avatar_2)
    ImageView image_avatar_2;
    @BindView(R.id.room_avatar_3)
    ImageView image_avatar_3;
    @BindView(R.id.room_avatar_4)
    ImageView image_avatar_4;

    @BindView(R.id.room_name_1)
    TextView text_user_1;
    @BindView(R.id.room_name_2)
    TextView text_user_2;
    @BindView(R.id.room_name_3)
    TextView text_user_3;
    @BindView(R.id.room_name_4)
    TextView text_user_4;

    int nUserCount = 0;
    int nReadyCount = 0;
    boolean bReadyShuffle = false;

    ImageView selectedAvatar = null;

    // 방장 여부
    boolean bHost;

    User currentUser;
    User[] allUser;
//    ArrayList<User> users;
    // end of real


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room4);
        AndroidInjection.inject(this);
        // 버터나이프 사용
        ButterKnife.bind(this);

        btn_shuffle.setOnTouchListener(this);
        btn_ban.setOnTouchListener(this);
        btn_ready.setOnTouchListener(this);

        btn_shuffle.setOnDragListener(this);
        btn_ban.setOnDragListener(this);
        btn_ready.setOnDragListener(this);

        btn_1.setOnTouchListener(this);
        btn_2.setOnTouchListener(this);
        btn_3.setOnTouchListener(this);
        btn_4.setOnTouchListener(this);

        btn_1.setOnDragListener(this);
        btn_2.setOnDragListener(this);
        btn_3.setOnDragListener(this);
        btn_4.setOnDragListener(this);

        initRX();
        initListener();

        // TODO: 방장인지 체크
        // 방만들기로 들어오는 경우 true, 아니면 false
        // 방만들기, 방찾기에서 extra로 보내줘야 함
        Intent intent = getIntent();

        // TODO: test
        //        boolean isHost = intent.getBooleanExtra("HOST", true);
//        isHost = true;
//        if (isHost) {
//            bHost = true;
//        } else {
//            bHost = false;
//        }

        initRoom();
        initUser();

        for (int i = 1; i < nUserCount + 1; i++) {
            drawUser(allUser[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clientPublishSubjectBusDisposable.dispose();
        clientBehaviorSubjectBusDisposable.dispose();
    }

    @OnClick({R.id.room_image_start,
            R.id.room_image_ready,
            R.id.room_image_ban,
            R.id.room_image_exit,
//              R.id.room_image_ready
    })
    public void clicked(ImageView view) {
        Log.d(TAG, "clicked");
        if (!bHost && view.getId() == R.id.room_image_ban) {
            return;
        }
        view.setSelected(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch");
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        int eventPadTouch = event.getAction();

        switch (eventPadTouch) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                switch (v.getId()) {
                    case R.id.btn_shuffle:
//                        Toast.makeText(getApplicationContext(), "셔플", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_ready:
//                        Toast.makeText(getApplicationContext(), "준비", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_ban:
//                        Toast.makeText(getApplicationContext(), "내보내기", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.room_image_start:
                        Log.d(TAG, "room_image_start");
//                        image_start.setPressed(true);
                        break;
                    case R.id.room_image_ready:
                        Log.d(TAG, "room_image_ready");
//                        image_ready.setPressed(true);
                        break;
                    case R.id.room_image_ban:
                        Log.d(TAG, "room_image_ban");

                        if (!bHost) {
                            return false;
                        }
//                        image_ban.setPressed(true);
                        break;
                    case R.id.room_image_exit:
                        Log.d(TAG, "room_image_exit");
//                        image_exit.setPressed(true);
                        break;

                    case R.id.btn_1:
                    case R.id.btn_2:
                    case R.id.btn_3:
                    case R.id.btn_4:

                    case R.id.room_avatar_1:
                    case R.id.room_avatar_2:
                    case R.id.room_avatar_3:
                    case R.id.room_avatar_4:

                        if (!bHost) {
//                        if (!isHost((ImageView)v)) {
                            Log.d(TAG, "방장 아님");
                            Toast.makeText(getApplicationContext(), "방장만 강퇴시킬수 있습니다.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (!isExist((ImageView) v)) {
                            Log.d(TAG, "빈 자리");
                            return false;
                        }
                        Log.d(TAG, "Drag 시작");
                        selectedAvatar = (ImageView) v;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            v.startDragAndDrop(data, mShadow, v, 0);
                        } else {
                            v.startDrag(data, mShadow, null, 0);
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                switch (v.getId()) {
                    case R.id.room_image_start:
                        Log.d(TAG, "room_image_start (nReadyCount: " + nReadyCount + ", nUserCount: " + nUserCount + ")");
                        MediaPlayer.create(RoomActivity.this, R.raw.mouth_interface_button).start();

                        image_start.setPressed(false);
//                        if (nReadyCount != nUserCount) {
                        if (!bReadyShuffle) {
                            Toast.makeText(getApplicationContext(), "모든 유저가 완료 되어야 합니다.", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(getApplicationContext(), "시작하기 (셔플)", Toast.LENGTH_SHORT).show();

                            doSendMessage(API_SHUFFLE, currentUser.getSeat());
                            ConnectionManager.deleteRoom()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        Log.d("lsc", "success");
                                    }, e -> {
                                        Log.d("lsc", "error " + e.getMessage());
                                    });
//                            Intent intent = new Intent(RoomActivity.this, PlayActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                        break;
                    case R.id.room_image_ready:
                        Log.d(TAG, "room_image_ready");

                        int apiNo;
                        if (currentUser.getStatus() == 1) {
                            apiNo = API_READY_CANCEL;
                        } else {
                            apiNo = API_READY;
                        }

                        doSendMessage(apiNo, currentUser.getSeat());
                        break;
                    case R.id.room_image_ban:
                        MediaPlayer.create(RoomActivity.this, R.raw.mouth_interface_button).start();

                        Log.d(TAG, "room_image_ban");

                        if (!bHost) {
                            return false;
                        }

                        image_ban.setPressed(false);
                        Toast.makeText(getApplicationContext(), "강퇴시킬 유저를 끌어다놓으세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.room_image_exit:
                        MediaPlayer.create(RoomActivity.this, R.raw.mouth_interface_button).start();

                        Log.d(TAG, "room_image_exit");

                        image_exit.setPressed(false);
//                        Toast.makeText(getApplicationContext(), "방 나가기", Toast.LENGTH_SHORT).show();
                        doExit();

                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
//        String clipData = event.getClipDescription().getLabel().toString();
        ImageView target = (ImageView) v;
        String targetTag = target.getTag().toString();

//        ImageView selected = (ImageView) event.getLocalState();
//        String selectedTag = selected.getTag().toString();
        ImageView selected;
        String selectedTag;

        String targetStr = "";

//        if (!target.getTag().equals("AVATAR")) {
//            Log.d(TAG, "onDrag return false (not avatar) : " + target.getTag());
//            return false;
//        }
//        target.setBackgroundColor(getResources().getColor(R.color.green));


//        if (!selected.getTag().equals("BUTTON")) {
//            Log.d(TAG, "onDrag return false (not button) : " + selected.getTag());
//            return false;
//        }

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
//                Log.d(TAG, "ACTION_DRAG_ENTERED : " + targetTag);

                if (target == selectedAvatar) {
                    return false;
                }
                if (!bHost) {
                    Log.d(TAG, "방장만 할 수 있음");
                    return false;
                }
                if (targetTag.equals("AVATAR")) {
                    String clipData = event.getClipDescription().getLabel().toString();
                    Log.d(TAG, "clipData : " + clipData);
                    switch (clipData) {
                        case "AVATAR":
//                            target.setBackgroundColor(getResources().getColor(R.color.green));
                            target.setBackgroundColor(Color.parseColor("#8000de6a"));
                            break;
                    }
                    v.invalidate();
                } else if (targetTag.equals("BUTTON")) {
                    switch (target.getId()) {
                        case R.id.room_image_ban:
                            target.setPressed(true);
                            break;
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
//                Log.d(TAG, "ACTION_DRAG_EXITED : " + targetTag);
                if (!bHost) {
                    Log.d(TAG, "방장만 할 수 있음");
                    return false;
                }
                if (targetTag.equals("AVATAR")) {
                    ClipDescription clipDesc = (ClipDescription) event.getClipDescription();
                    if (clipDesc == null) {
                        return false;
                    }

                    String clipData = clipDesc.getLabel().toString();
                    switch (clipData) {
                        case "AVATAR":
                            target.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            break;
                    }
                } else if (targetTag.equals("BUTTON")) {
                    switch (target.getId()) {
                        case R.id.room_image_ban:
                            target.setPressed(false);
                            break;
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
//                Log.d(TAG, "ACTION_DRAG_ENDED : " + targetTag);
                if (!bHost) {
                    Log.d(TAG, "방장만 할 수 있음");
                    return false;
                }
                if (targetTag.equals("AVATAR")) {
                    ClipDescription clipDesc = (ClipDescription) event.getClipDescription();
                    if (clipDesc == null) {
//                        Log.d(TAG, "clipDesc is null");
                        return false;
                    }

                    String clipData = clipDesc.getLabel().toString();
                    switch (clipData) {
                        case "AVATAR":
                            target.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            break;
                    }
                } else if (targetTag.equals("BUTTON")) {
                    switch (target.getId()) {
                        case R.id.room_image_ban:
                            target.setPressed(false);
                            break;
                    }
                }
                return true;

            case DragEvent.ACTION_DROP:
                if (!bHost) {
                    Log.d(TAG, "방장만 할 수 있음");
                    return false;
                }
                // selected가 선택된 놈, target이 드랍된 놈
                selected = (ImageView) event.getLocalState();
                selectedTag = selected.getTag().toString();

                if (target == selected) {
                    selected.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    return false;
                }

                Log.d(TAG, "ACTION_DROP (targetTag : " + targetTag + ", selectedTag : " + selectedTag + ")");

                target.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                selected.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                if (targetTag.equals("AVATAR")) {
//                    doChange(selected, target);

                    int seatNo1 = getSeatNo(selected);
                    int seatNo2 = getSeatNo(target);
                    int[] arrSeat = {seatNo1, seatNo2};
                    doSendMessage(API_MOVE, arrSeat);
                } else if (targetTag.equals("BUTTON")) {
                    switch (target.getId()) {
                        case R.id.room_image_ban:
//                            doBan(selected);
                            int seatNo = getSeatNo(selected);
                            doSendMessage(API_BAN, seatNo);
                            break;
                    }
                } else if (targetTag.equals("TAG")) {
                    switch (target.getId()) {
                        default:
                            break;
                    }
                } else {
                    Log.d(TAG, "ACTION_DROP else");
                }
                return true;

            default:
                return false;
        }
    }


    private void initListener() {
        image_start.setOnTouchListener(this);
        image_ready.setOnTouchListener(this);
        image_ban.setOnTouchListener(this);
        image_exit.setOnTouchListener(this);

        image_start.setOnDragListener(this);
        image_ready.setOnDragListener(this);
        image_ban.setOnDragListener(this);
        image_exit.setOnDragListener(this);

        image_avatar_1.setOnTouchListener(this);
        image_avatar_2.setOnTouchListener(this);
        image_avatar_3.setOnTouchListener(this);
        image_avatar_4.setOnTouchListener(this);

        image_avatar_1.setOnDragListener(this);
        image_avatar_2.setOnDragListener(this);
        image_avatar_3.setOnDragListener(this);
        image_avatar_4.setOnDragListener(this);
    }


    private void initRoom() {
        // TODO : 유저 수는 방 설정 값에 따라 결정
//        nUserCount = 4;
        allUser = new User[MAX_USER_COUNT + 1];
    }


    // host은 본인 정보만 초기화
    // client 는 host로부터 전달받은 user 정보를 이용하여 초기화 한다
    private void initUser(ArrayList<User> users) {
        // 유저정보 획득
        String userName = dataManager.getUserName();
        int avatarId = dataManager.getUserAvatar();
        int total = dataManager.getUserTotal();
        int win = dataManager.getUserWin();
        Log.d(TAG, "sharedPreferences 조회결과 (userName: " + userName + ", avatarId: " + avatarId + ", total: " + total + ", win: " + win + ")");

        User curUser;
        int curUserIndex = 1;
        int tSeat = -1;

        // User
        Log.d(TAG, "서버에서 받은 users 정보");
        for (int i = 1; i < users.size() + 1; i++) {
            curUser = users.get(i - 1);
            tSeat = curUser.getSeat();
            if (curUser.getAvatar() != 0) {
                allUser[tSeat] = new User(tSeat, curUser.isHost(), curUser.getName(),
                        curUser.getAvatar(), curUser.getTotal(), curUser.getWin());
                allUser[tSeat].setStatus(curUser.getStatus());
                if (curUser.getName().equals(userName) && curUser.getAvatar() == avatarId) {
                    curUserIndex = tSeat;
                }
                Log.d(TAG, i + "번째 유저 : " + allUser[tSeat].toString());
            }
        }

        // dummy
        Log.d(TAG, "----------------------------");
        for (int i = 1; i < MAX_USER_COUNT + 1; i++) {
            curUser = allUser[i];
            if (curUser.getAvatar() == 0) {
                allUser[i] = new User(i, false, "", 0, 0, 0);
            }
            Log.d(TAG, i + "번째 유저 : " + curUser.toString());
        }

        bHost = false;
        nUserCount = users.size();
        Log.d(TAG, "nUserCount is " + nUserCount);

        currentUser = allUser[curUserIndex];
        Log.d(TAG, "curUser : " + currentUser.toString());

        // for client
        image_start.setVisibility(View.INVISIBLE);
        image_ready.setVisibility(View.VISIBLE);
        image_ban.setImageResource(R.drawable.room_ban_dis);
//        loadImage(image_ban, R.drawable.room_ban_dis);
    }

    // host은 본인 정보만 초기화
    // client 는 host로부터 전달받은 user 정보를 이용하여 초기화 한다
    private void initUser() {
        allUser = new User[MAX_USER_COUNT + 1];

        // 유저정보 획득
        String userName = dataManager.getUserName();
        int avatarId = dataManager.getUserAvatar();
        int total = dataManager.getUserTotal();
        int win = dataManager.getUserWin();
        Log.d(TAG, "sharedPreferences 조회결과 (userName: " + userName + ", avatarId: " + avatarId + ", total: " + total + ", win: " + win + ")");

        bHost = true; // 방장
        allUser[1] = new User(1, bHost, userName, avatarId, total, win);

        nReadyCount++;

        for (int i = 2; i < MAX_USER_COUNT + 1; i++) {
//            allUser[i] = new User(i, bHost, i, i, name + i);
            allUser[i] = new User(i, false, "", 0, 0, 0);
        }

        nUserCount = 1;
        Log.d(TAG, "nUserCount is " + nUserCount);

        currentUser = allUser[1];
        Log.d(TAG, currentUser.toString());

        // for host
        image_start.setVisibility(View.VISIBLE);
        image_ready.setVisibility(View.INVISIBLE);
    }

    private void addUser(User user) {
        Log.d(TAG, "addUser : " + user.toString());

        if (user.getAvatar() == 0) {
            Log.d(TAG, "addUser avatar is 0");
            return;
        }
        int seatNo = user.getSeat();
//        allUser[seatNo] = new User(user.getSeat(), user.isHost(), user.getSeat(), user.getAvatar(), user.getName());
        allUser[seatNo] = new User(seatNo, user.isHost(), user.getName(), user.getAvatar(), user.getTotal(), user.getWin());
        nUserCount++;
        Log.d(TAG, "addUser (nUserCount: " + nUserCount + ")");
    }

    private void deleteUser(int seat) {
        Log.d(TAG, "deleteUser seat : " + seat);

        User user = allUser[seat];
        if (user.getAvatar() == 0) {
            Log.d(TAG, "addUser avatar is 0");
        }
        user.doBan();
        nUserCount--;
        Log.d(TAG, "deleteUser (nUserCount: " + nUserCount + ")");
    }

    private void drawUser() {
        Log.d(TAG, "drawUser (nUserCount: " + nUserCount + ")");

        for (int i = 1; i < MAX_USER_COUNT + 1; i++) {
            drawUser(allUser[i]);
        }
    }

    private void drawUser(User user) {
        Log.d(TAG, "drawUser (user: " + user.toString() + ")");

        // seatNo
        int seat = user.getSeat();

        int imageId = getResourceId("id", "room_avatar_" + seat);
        int nameId = getResourceId("id", "room_name_" + seat);
        int tagId = getResourceId("id", "room_avatar_tag_" + seat);

        ImageView imageView = (ImageView) findViewById(imageId);
        ImageView tagView = (ImageView) findViewById(tagId);
        TextView textView = (TextView) findViewById(nameId);

        // 아바타 표시
        int avatarId = user.getAvatar();
        if (avatarId != 0) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(getResourceId("drawable", "avatar_" + avatarId));
//            loadImage(imageView, getResourceId("drawable", "avatar_" + avatarId));

            // 방장인 경우 방장표시, client이면 ready인지 판단하여 표시
            if (user.isHost()) {
                tagView.setVisibility(View.VISIBLE);
                tagView.setImageResource(getResourceId("drawable", "tag_host"));
//                loadImage(tagView, getResourceId("drawable", "tag_host"));
            } else if (user.getStatus() == 1) {
                tagView.setVisibility(View.VISIBLE);
                tagView.setImageResource(getResourceId("drawable", "tag_ready"));
//                loadImage(tagView, getResourceId("drawable", "tag_ready"));
            } else if (user.getStatus() == 0) {
                tagView.setVisibility(View.INVISIBLE);
                tagView.setImageResource(getResourceId("drawable", "tag_ready"));
//                loadImage(tagView, getResourceId("drawable", "tag_ready"));
            }
        } else {
            imageView.setImageResource(0);
//            loadImage(imageView, 0);

            tagView.setVisibility(View.INVISIBLE);
        }

        // 이름 표시
        String name = user.getName();
        textView.setText(name);
    }


    private int getResourceId(String type, String name) {
        // use case
//        // image from res/drawable
//        int resID = getResources().getIdentifier("my_image", "drawable", getPackageName());
//        // view
//        int resID = getResources().getIdentifier("my_resource", "id", getPackageName());
//        // string
//        int resID = getResources().getIdentifier("my_string", "string", getPackageName());
        return getResources().getIdentifier(name, type, getPackageName());
    }


    private void doExit() {
        Log.d(TAG, "doExit");
        doSendMessage(API_OUT, currentUser.getSeat());
    }

    private boolean isHost(ImageView selected) {
        boolean ret = false;
        int selectedSeat = getSeatNo(selected);
        ret = allUser[selectedSeat].isHost() == true ? true : false;
        Log.d(TAG, "isHost : " + ret);
        return ret;
    }

    private boolean isExist(ImageView selected) {
        boolean ret = false;
        int selectedSeat = getSeatNo(selected);
        ret = allUser[selectedSeat].getAvatar() != 0 ? true : false;
        Log.d(TAG, "isExist : " + ret);
        return ret;
    }

    private void doChange(int seatNo1, int seatNo2) {
        Log.d(TAG, "doChange");

        int selectedSeat = seatNo1;
        int targetSeat = seatNo2;
        Log.d(TAG, "selectedSeat : " + selectedSeat);
        Log.d(TAG, "targetSeat : " + targetSeat);

        UserWrapper wrapper1 = new UserWrapper(allUser[selectedSeat]);
        UserWrapper wrapper2 = new UserWrapper(allUser[targetSeat]);

        Log.d(TAG, "변경 전");
        Log.d(TAG, allUser[selectedSeat].toString());
        Log.d(TAG, allUser[targetSeat].toString());

        swap(wrapper1, wrapper2);
        allUser[selectedSeat] = wrapper1.user;
        allUser[targetSeat] = wrapper2.user;

        Log.d(TAG, "변경 후");
        // seat만 변경되는 것이므로 seat값은 재지정
        allUser[selectedSeat].setSeat(selectedSeat);
        allUser[targetSeat].setSeat(targetSeat);
        Log.d(TAG, allUser[selectedSeat].toString());
        Log.d(TAG, allUser[targetSeat].toString());

        int curUserSeat = currentUser.getSeat();
        Log.d(TAG, "curUserSeat : " + curUserSeat);
        if (curUserSeat == seatNo1) {
            currentUser = allUser[selectedSeat];
            Log.d(TAG, "currentUser : " + currentUser.toString());
        } else if (curUserSeat == seatNo2) {
            currentUser = allUser[targetSeat];
            Log.d(TAG, "currentUser : " + currentUser.toString());
        }

        drawUser(allUser[selectedSeat]);
        drawUser(allUser[targetSeat]);

        Log.d(TAG, "end doChange");
//        Toast.makeText(getApplicationContext(), "자리 변경 완료", Toast.LENGTH_SHORT).show();
    }

    private void doChange(ImageView selected, ImageView target) {
        Log.d(TAG, "doChange");

        int selectedSeat = getSeatNo(selected);
        int targetSeat = getSeatNo(target);
        Log.d(TAG, "selectedSeat : " + selectedSeat);
        Log.d(TAG, "targetSeat : " + targetSeat);

        UserWrapper wrapper1 = new UserWrapper(allUser[selectedSeat]);
        UserWrapper wrapper2 = new UserWrapper(allUser[targetSeat]);

        Log.d(TAG, "변경 전");
        Log.d(TAG, allUser[selectedSeat].toString());
        Log.d(TAG, allUser[targetSeat].toString());

        swap(wrapper1, wrapper2);
        allUser[selectedSeat] = wrapper1.user;
        allUser[targetSeat] = wrapper2.user;

        Log.d(TAG, "변경 후");
        Log.d(TAG, allUser[selectedSeat].toString());
        Log.d(TAG, allUser[targetSeat].toString());

        // seat만 변경되는 것이므로 seat값은 재지정
        allUser[selectedSeat].setSeat(selectedSeat);
        allUser[targetSeat].setSeat(targetSeat);

        drawUser(allUser[selectedSeat]);
        drawUser(allUser[targetSeat]);

        Log.d(TAG, "end doChange");
//        Toast.makeText(getApplicationContext(), "자리 변경 완료", Toast.LENGTH_SHORT).show();
    }

    private int getSeatNo(View v) {
        int retSeat = 0;
        switch (v.getId()) {
            case R.id.room_avatar_1:
                retSeat = 1;
                break;
            case R.id.room_avatar_2:
                retSeat = 2;
                break;
            case R.id.room_avatar_3:
                retSeat = 3;
                break;
            case R.id.room_avatar_4:
                retSeat = 4;
                break;
        }
        return retSeat;
    }


    private void swap(UserWrapper user1, UserWrapper user2) {
        User temp = user1.user;
        user1.user = user2.user;
        user2.user = temp;
    }

    private void doReady(int seat, boolean flag) {
        Log.d(TAG, "doReady int param (seat: " + seat + ")");
        User user = allUser[seat];
        if (!user.getName().equals("")) {
            user.doReady(flag);
            drawUser(user);
            if (flag) {
                nReadyCount++;
            } else {
                nReadyCount--;
            }
            Log.d(TAG, "nReadyCount : " + nReadyCount);
//            Toast.makeText(getApplicationContext(), "준비", Toast.LENGTH_SHORT).show();
        }
    }

    private void doReady(ImageView selected) {
        int selectedSeat = getSeatNo(selected);
        Log.d(TAG, "doReady ImageView param (seat: " + selectedSeat + ")");
        User user = allUser[selectedSeat];
        if (!user.getName().equals("")) {
            if (user.isHost()) {
                Toast.makeText(getApplicationContext(), "방장은 언제나 준비중", Toast.LENGTH_SHORT).show();
                return;
            }

            user.doReady(true);
            drawUser(user);
            nReadyCount++;
            Log.d(TAG, "nReadyCount : " + nReadyCount);
//            Toast.makeText(getApplicationContext(), "준비", Toast.LENGTH_SHORT).show();
        }
    }

    private void doBan(int seat) {
        Log.d(TAG, "doBan int param (seat: " + seat + ")");
        User user = allUser[seat];
        if (!user.getName().equals("")) {
            if (user.isHost()) {
                Toast.makeText(getApplicationContext(), "방장은 나갈수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            user.doBan();
            drawUser(user);
//            Toast.makeText(getApplicationContext(), "내보내기", Toast.LENGTH_SHORT).show();
        }
    }

    private void doBan(ImageView selected) {
        MediaPlayer.create(RoomActivity.this, R.raw.mouth_interface_button).start();

        int selectedSeat = getSeatNo(selected);
        Log.d(TAG, "doBan ImageView param (seat: " + selectedSeat + ")");
        User user = allUser[selectedSeat];
        if (!user.getName().equals("")) {
            if (user.isHost()) {
                Toast.makeText(getApplicationContext(), "방장은 나갈수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            user.doBan();
            drawUser(user);
//            Toast.makeText(getApplicationContext(), "내보내기", Toast.LENGTH_SHORT).show();
        }
    }

    public void doEnterUser(String name, int seatNo, int avatar) {
        Log.d(TAG, "User 입장");

//        "name" : "쫑미니",
//                "seatNo" : 3,
//                "avatar" : 4

        // seatNo 위치에 user가 비어있는지 확인 후
        // allUser에 user 정보를 추가 후 drawUser
    }

    private void doSendMessage(int apiNo, int seatNo1) {
//        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ", seatNo1: " + seatNo1 + ", seatNo2: " + -1 + ")");
        int[] arr = {seatNo1, -1};
        doSendMessage(apiNo, arr);
    }

    //    private void doSendMessage(int apiNo, int seatNo1,  int seatNo2) {
    private void doSendMessage(int apiNo, int[] arrSeat) {
        int seatNo1 = arrSeat[0];
        int seatNo2 = arrSeat[1];
        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ", seatNo1: " + seatNo1 + ", seatNo2: " + seatNo2 + ")");
        ApiBody apiBody;
        if (seatNo2 == -1) {
            apiBody = new ApiBody(apiNo, seatNo1);
        } else {
            apiBody = new ApiBody(apiNo, seatNo1, seatNo2);
        }
        ConnectionManager.sendMessage(apiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "doSendMessage success " + apiBody);
                }, onError -> {
                    onError.printStackTrace();
                });
    }

    private void loadImage(ImageView view, int resId) {
        Log.d(TAG, "loadImage (resId: " + resId + ")");
        Glide.with(this).load(resId).into(view);
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, RoomActivity.class);
        context.startActivity(intent);
    }

    private Disposable clientPublishSubjectBusDisposable;
    private Disposable clientBehaviorSubjectBusDisposable;

    private void initRX() {
        Log.d(TAG, "initRX");

        clientBehaviorSubjectBusDisposable = BehaviorSubjectBus.getInstance().getEvents(ApiBody.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(result -> {
                    Log.d(TAG, "RoomActivity behavior apiBody " + result);
                    ApiBody apiBody = ((ApiBody) result);
                    switch (apiBody.getNo()) {
                        case API_ROOM_INFO:
                            ArrayList<User> userList = apiBody.getUsers();
                            Log.d(TAG, "RoomActivity behavior apiBody " + userList);
                            initUser((ArrayList<User>) userList);
                            drawUser();
                            break;

                        case API_SHUFFLE_AVAILABLE:
                            bReadyShuffle = true;
                            break;

                        case API_SHUFFLE_NOT_AVAILABLE:
                            bReadyShuffle = false;
                            break;
                    }
                }, onError -> {
                    Log.d(TAG, "RoomActivity behavior onError " + onError);
                }, () -> Log.d(TAG, "RoomActivity behavior onCompleted"));


        clientPublishSubjectBusDisposable = ClientPublishSubjectBus.getInstance().getEvents(String.class)
                .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Log.d(TAG, "RoomActivity server result " + result);
                    Log.d("lsc", "RoomActivity server result " + result);
                    ApiBody apiBody = ((ApiBody) result);
                    switch (apiBody.getNo()) {
                        case API_ROOM_INFO:
                            break;

                        case API_ENTER_ROOM_TO_OTHER:
                            Log.d(TAG, "RoomActivity onCreate API_ENTER_ROOM_TO_OTHER");
                            User curUser = ((ApiBody) result).getUser();
                            Log.d(TAG, curUser.toString());
                            addUser(curUser);
                            drawUser(curUser);
                            break;

                        case API_READY_BR:
                            doReady(apiBody.getSeatNo(), true);
                            break;

                        case API_READY_CANCEL_BR:
                            doReady(apiBody.getSeatNo(), false);
                            break;

                        case API_SHUFFLE_BR:
                            Intent intent = new Intent(RoomActivity.this, PlayActivity.class);
                            intent.putExtra("isHost", currentUser.isHost());
                            intent.putExtra("seatNum", currentUser.getSeat());
                            intent.putExtra("card1", apiBody.getCardNo1());
                            intent.putExtra("card2", apiBody.getCardNo2());
                            startActivity(intent);
                            finishAffinity();
                            break;

                        case API_MOVE_BR:
                            doChange(apiBody.getSeatNo(), apiBody.getSeatNo2());
                            break;

                        case API_BAN_BR:
                            doBan(apiBody.getSeatNo());
                            break;

                        case API_OUT_BR:
                            //Todo : API_OUT_BR은 본인이 나갔을 때는 받지 않고 다른 User가 나갔을 때 받음.
                            doBan(apiBody.getSeatNo());
//                            intent = new Intent(RoomActivity.this, LobbyActivity.class);
//                            startActivity(intent);
//                            finish();
                            break;

                        case API_SHUFFLE_AVAILABLE:
                            bReadyShuffle = true;
                            break;

                        case API_SHUFFLE_NOT_AVAILABLE:
                            bReadyShuffle = false;
                            break;

                        case API_OUT_SELF:
                            //본인이 나갔을 때
                            LobbyActivity.start(this);
                            finish();
                            break;
                    }

                }, onError -> {
                    Log.d(TAG, "test onError " + onError);
                }, () -> Log.d(TAG, "test onCompleted"));
    }

}
