package com.fundroid.offstand.ui.play;

import android.annotation.SuppressLint;
import android.content.ClipDescription;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.model.ApiBody;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.lobby.guide.GuideFragment;
import com.fundroid.offstand.utils.rx.ClientPublishSubjectBus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.fundroid.offstand.data.remote.ApiDefine.API_CARD_OPEN;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_DIE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT_AVAILABLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_GAME_RESULT_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT;
import static com.fundroid.offstand.data.remote.ApiDefine.API_OUT_SELF;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_BR;
import static com.fundroid.offstand.utils.CommonUtils.getVisibleFragmentTag;

public class PlayActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener, HasSupportFragmentInjector {
    static final String TAG = "[PLAY]";

    public static int SOUND_MAX_COUNT = 10;
    public static int SOUND_BACKGROUND = 0;
    public static int SOUND_LAUGH = 2;
    public static int SOUND_THUNDER = 3;
    public static int SOUND_LUCKY1 = 4;
    public static int SOUND_LUCKY2 = 5;
    public static int SOUND_LUCKY3 = 6;
    public static boolean drawCheck = false;
    public static int userCheck = 0;


    //    @BindView(R.id.play_image_card0)
    ImageView image0;
    //    @BindView(R.id.play_image_card1)
    ImageView image1;
    //    @BindView(R.id.play_image_card2)
    ImageView image2;
    //    @BindView(R.id.play_image_card3)
    ImageView image3;
    //    @BindView(R.id.play_image_card4)
    ImageView image4;

    @BindView(R.id.play_sound_bluffing)
    ImageView play_sound_bluffing;
    @BindView(R.id.play_sound_1)
    ImageView play_sound_1;
    @BindView(R.id.play_sound_2)
    ImageView play_sound_2;
    @BindView(R.id.play_sound_3)
    ImageView play_sound_3;
    @BindView(R.id.play_sound_4)
    ImageView play_sound_4;

    @BindView(R.id.play_image_card_die)
    ImageView image_card_die;

    @BindView(R.id.play_image_setting)
    ImageView image_setting;
    @BindView(R.id.play_image_exit)
    ImageView image_exit;
    @BindView(R.id.play_image_open)
    ImageView image_open;
    @BindView(R.id.play_image_re)
    ImageView image_re;
    @BindView(R.id.play_image_die)
    ImageView image_die;
    @BindView(R.id.play_image_result)
    ImageView image_result;
    @BindView(R.id.play_image_jokbo)
    ImageView image_jokbo;

    View.OnTouchListener touchListener;

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;
    private SpringAnimation openAnimation;

    private SpringAnimation ani_view1_x, ani_view1_y;
    private SpringAnimation ret_view1_x, ret_view1_y;
    private SpringAnimation ani_view2_x, ani_view2_y;
    private SpringAnimation ret_view2_x, ret_view2_y;

    private float org_x;
    private float org_y;
    private float dX;
    private float dY;

    private int viewHeight;
    private int startY;
    private int tempIndex = 1;
    private boolean bCheck = false;

    private boolean isHost = false;
    private int seatNum = -1;

    private boolean enableRegame = false;
    private boolean enableResult = false;
    private boolean showResult = false;

    private boolean bOpenCard = false;
    private boolean bHideCard1 = false, bHideCard2 = false;
    private String card1, card2;

    SoundPool soundPool = null;
    int[] soundTrack;

    @Inject
    DataManager dataManager;

    // TODO
    // 참여인원
    // 잔여 시간
    // 진행 게임
    // 잔여 게임
    FragmentManager fragmentManager;
    GameResultFragment fragment;
    GuideFragment fragment2;

    // 만땅 - 게임 결과 화면
    FrameLayout result_back;
    ImageView result_title1;
    //    ImageView result_title2;
    ImageView result_content1;
    ImageView result_content1_card1;
    ImageView result_content1_card2;
    ImageView result_content2;
    ImageView result_content2_rank2_card1;
    ImageView result_content2_rank2_card2;
    ImageView result_content2_rank3_card1;
    ImageView result_content2_rank3_card2;
    ImageView result_content2_rank4_card1;
    ImageView result_content2_rank4_card2;
    ImageView result_left_button;
    ImageView result_right_button;
    ImageView result_shadow;
    ImageView result_rebutton;

    //게임 결과 리스트 정보
    static Map<String, Object> resultInfoMap = new HashMap<String, Object>();
    static List resultList = new ArrayList();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Log.d(TAG, "onCreate");
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        fragment = new GameResultFragment();
        fragment2 = new GuideFragment();

        // 버터나이프 사용
        ButterKnife.bind(this);

        Intent intent = new Intent(this.getIntent());
        int tNum1, tNum2;
        isHost = intent.getBooleanExtra("isHost", false);
        seatNum = intent.getIntExtra("seatNum", -1);
        tNum1 = intent.getIntExtra("card1", -1);
        tNum2 = intent.getIntExtra("card2", -1);
        card1 = calcRandomNumber(tNum1);
        card2 = calcRandomNumber(tNum2);
        Log.d(TAG, "isHost : " + isHost);
        Log.d(TAG, "seatNum : " + seatNum);
        Log.d(TAG, "Card1 : " + card1);
        Log.d(TAG, "Card2 : " + card2);

        initShuffle();

        initRX();

        initImage();

        initSound();

        initListener();

        initButton(true);


        // 만땅 - 셋팅
        result_back = (FrameLayout) findViewById(R.id.fragment_container_play_result);
//        result_title1 = (ImageView) findViewById(R.id.play_result_title1);
////        result_title2 = (ImageView) findViewById(R.id.play_result_title2);
//        result_content1 = (ImageView) findViewById(R.id.play_result_content1);
//        result_content1_card1 = (ImageView) findViewById(R.id.play_result_content1_card1);
//        result_content1_card2 = (ImageView) findViewById(R.id.play_result_content1_card2);
//        result_content2 = (ImageView) findViewById(R.id.play_result_content2);
//        result_content2_rank2_card1 = (ImageView) findViewById(R.id.play_result_rank2_card1);
//        result_content2_rank2_card2 = (ImageView) findViewById(R.id.play_result_rank2_card2);
//        result_content2_rank3_card1 = (ImageView) findViewById(R.id.play_result_rank3_card1);
//        result_content2_rank3_card2 = (ImageView) findViewById(R.id.play_result_rank3_card2);
//        result_content2_rank4_card1 = (ImageView) findViewById(R.id.play_result_rank4_card1);
//        result_content2_rank4_card2 = (ImageView) findViewById(R.id.play_result_rank4_card2);
//        result_left_button = (ImageView) findViewById(R.id.play_result_left_button);
//        result_right_button = (ImageView) findViewById(R.id.play_result_right_button);
//        result_shadow = (ImageView) findViewById(R.id.play_result_shadow);
//        result_rebutton = (ImageView) findViewById(R.id.play_result_ReButton);


        // 만땅 - 기존에 안보이게 셋팅
        result_back.setVisibility(View.GONE);
//        result_title1.setVisibility(View.GONE);
////        result_title2.setVisibility(View.GONE);
//        result_content1.setVisibility(View.GONE);
//        result_content1_card1.setVisibility(View.GONE);
//        result_content1_card2.setVisibility(View.GONE);
//        result_content2.setVisibility(View.GONE);
//        result_content2_rank2_card1.setVisibility(View.GONE);
//        result_content2_rank2_card2.setVisibility(View.GONE);
//        result_content2_rank3_card1.setVisibility(View.GONE);
//        result_content2_rank3_card2.setVisibility(View.GONE);
//        result_content2_rank4_card1.setVisibility(View.GONE);
//        result_content2_rank4_card2.setVisibility(View.GONE);
//        result_left_button.setVisibility(View.GONE);
//        result_right_button.setVisibility(View.GONE);
//        result_shadow.setVisibility(View.GONE);
//        result_rebutton.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        for (int i = 0; i < soundTrack.length; i++) {
            soundTrack[i] = 0;
        }

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @OnClick({
            R.id.play_image_setting,
            R.id.play_image_exit,
            R.id.play_image_open,
            R.id.play_image_re,
            R.id.play_image_die,
            R.id.play_image_result,
            R.id.play_image_jokbo
    })
    public void clicked(ImageView view) {
        Log.d(TAG, "clicked");
        view.setSelected(true);
    }

    @OnClick({
            R.id.play_image_card3,
            R.id.play_image_card4
    })
    public void onClick_hide_card(ImageView view) {
        Log.d(TAG, "onClick_hide_card");

        if (!bOpenCard) {
            return;
        }

        switch (view.getId()) {
            case R.id.play_image_card3:
                if (bHideCard2) {
                    loadImage(image2, getResourceId("drawable", "card_" + card1));
                    bHideCard2 = false;
                } else {
                    loadImage(image2, R.drawable.card_back);
                    bHideCard2 = true;
                }
                break;
            case R.id.play_image_card4:
                if (bHideCard1) {
                    loadImage(image1, getResourceId("drawable", "card_" + card2));
                    bHideCard1 = false;
                } else {
                    loadImage(image1, R.drawable.card_back);
                    bHideCard1 = true;
                }
                break;
        }
    }

    @OnClick({
            R.id.play_sound_bluffing,
            R.id.play_sound_1,
            R.id.play_sound_2,
            R.id.play_sound_3,
            R.id.play_sound_4
    })
    public void onClick_sound(ImageView view) {
        Log.d(TAG, "onClick_sound");

        switch (view.getId()) {
            case R.id.play_sound_bluffing:
                Group groupSound = (Group) findViewById(R.id.room_group_sound);
                if (groupSound.getVisibility() == View.GONE) {
                    groupSound.setVisibility(View.VISIBLE);
                } else {
                    groupSound.setVisibility(View.GONE);
                }
                break;
            case R.id.play_sound_1:
                playSound(SOUND_LAUGH);
                break;
            case R.id.play_sound_2:
                playSound(SOUND_LUCKY1);
                break;
            case R.id.play_sound_3:
                playSound(SOUND_LUCKY2);
                break;
            case R.id.play_sound_4:
                playSound(SOUND_LUCKY3);
                break;
//            case R.id.play_sound_5:
//                playSound(SOUND_THUNDER);
//                break;
        }
    }

    public static SpringAnimation createSpringAnimation(
            View view,
            DynamicAnimation.ViewProperty property,
            float finalPosition,
            float stiffness,
            float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }


    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerView1 = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            ani_view1_x = createSpringAnimation(image1, SpringAnimation.X, image4.getX(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            ani_view1_y = createSpringAnimation(image1, SpringAnimation.Y, image4.getY(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            org_x = image0.getX();
            org_y = image0.getY();

            ret_view1_x = createSpringAnimation(image1, SpringAnimation.X, org_x,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            ret_view1_y = createSpringAnimation(image1, SpringAnimation.Y, org_y,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            ret_view1_x.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                    Log.d(TAG, "Reset 완료");

                    loadImage(image1, getResourceId("drawable", "card_" + card1));
                    loadImage(image2, R.drawable.card_back);
                }
            });
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerView2 = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            xAnimation = createSpringAnimation(image2, SpringAnimation.X, image1.getX(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            yAnimation = createSpringAnimation(image2, SpringAnimation.Y, image1.getY(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
//            openAnimation = createSpringAnimation(image2, SpringAnimation.Y, image1.getY() + image1.getHeight(),
//                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            ani_view2_x = createSpringAnimation(image2, SpringAnimation.X, image3.getX(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            ani_view2_y = createSpringAnimation(image2, SpringAnimation.Y, image3.getY(),
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            org_x = image0.getX();
            org_y = image0.getY();

            ret_view2_x = createSpringAnimation(image2, SpringAnimation.X, org_x,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            ret_view2_y = createSpringAnimation(image2, SpringAnimation.Y, org_y,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            yAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                    if (bCheck) {
                        changeCard();
                    }
                }
            });
        }
    };


    private void initButton(boolean bFlag) {
        if (bFlag) {
            image_setting.setVisibility(View.VISIBLE);
            // shuffle 효과 중에 보이는 이슈로 인해 INVISIBLE로 변경
            image_open.setVisibility(View.INVISIBLE);

            image_exit.setVisibility(View.INVISIBLE);
            image_re.setVisibility(View.INVISIBLE);
            image_die.setVisibility(View.INVISIBLE);
            image_result.setVisibility(View.INVISIBLE);
            image_jokbo.setVisibility(View.INVISIBLE);
        } else {
            image_setting.setVisibility(View.INVISIBLE);
            image_open.setVisibility(View.INVISIBLE);

            image_exit.setVisibility(View.VISIBLE);
            image_re.setVisibility(View.VISIBLE);
            image_die.setVisibility(View.VISIBLE);
            image_result.setVisibility(View.VISIBLE);
            image_jokbo.setVisibility(View.VISIBLE);

//            loadImage(image_re, R.drawable.play_re_dis);
//            loadImage(image_result, R.drawable.play_result_dis);
            image_re.setEnabled(false);
            image_result.setEnabled(false);

            enableRegame = false;
            enableResult = false;
            showResult = false;

            bOpenCard = true;
            bHideCard1 = false;
            bHideCard2 = false;

            image3.setVisibility(View.VISIBLE);
            image4.setVisibility(View.VISIBLE);
        }
    }

    private void initImage() {
        image0 = (ImageView) findViewById(R.id.play_image_card0);
        image1 = (ImageView) findViewById(R.id.play_image_card1);
        image2 = (ImageView) findViewById(R.id.play_image_card2);
        image3 = (ImageView) findViewById(R.id.play_image_card3);
        image4 = (ImageView) findViewById(R.id.play_image_card4);

        play_sound_bluffing.setImageResource(R.drawable.button_play_sound_bluffing);
        play_sound_1.setImageResource(R.drawable.button_play_sound_1);
        play_sound_2.setImageResource(R.drawable.button_play_sound_1);
        play_sound_3.setImageResource(R.drawable.button_play_sound_1);
        play_sound_4.setImageResource(R.drawable.button_play_sound_1);
    }

    private void initSound() {
        Log.d(TAG, "initSound");

        soundTrack = new int[SOUND_MAX_COUNT];

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build();

                soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(3).build();

//        loadSound(SOUND_BACKGROUND, R.raw.pirate_sound_background);
                loadSound(SOUND_LAUGH, R.raw.play_sound_laugh);
//                loadSound(SOUND_THUNDER, R.raw.play_sound_thunder);
                loadSound(SOUND_LUCKY1, R.raw.play_sound_lucky1);
                loadSound(SOUND_LUCKY2, R.raw.play_sound_lucky2);
                loadSound(SOUND_LUCKY3, R.raw.play_sound_lucky3);
            }
        }, 100);
    }

    private void initShuffle() {
        Log.d(TAG, "initShuffle");

        play_sound_bluffing.setVisibility(View.GONE);
        Group groupSound = (Group) findViewById(R.id.room_group_sound);
        groupSound.setVisibility(View.GONE);
        image_open.setVisibility(View.GONE);

        // 생성한 비디오뷰를 bind
        VideoView videoView = (VideoView) findViewById(R.id.mp4_shuffle);
        // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
        MediaController mediaController = new MediaController(this);
        // 비디오뷰에 연결
        mediaController.setAnchorView(videoView);
        // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mp4_shuffle);
        /*
        외부파일의 경우
        Uri video = Uri.parse("http://해당 url/mp4_file_name.mp4") 와 같이 사용한다.
        */

        //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
//        videoView.setMediaController(mediaController);
        //비디오뷰에 재생할 동영상주소를 연결
        videoView.setVideoURI(video);
        //비디오뷰를 포커스하도록 지정
        videoView.requestFocus();
        //비디오를 처음부터 재생할 때 0으로 시작(파라메터 sec)
//        videoView.seekTo(200);
        // 검은색 화면이 깜빡이는 이슈 해결
        videoView.setZOrderOnTop(true);
        videoView.setVisibility(View.VISIBLE);
        //동영상 재생
        videoView.start();

        //동영상이 재생준비가 완료되었을 때를 알 수 있는 리스너 (실제 웹에서 영상을 다운받아 출력할 때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "initShuffle setOnPreparedListener");
//                mp.setLooping(true);
            }
        });

        //동영상 재생이 완료된 걸 알 수 있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
//                Toast.makeText(PlayActivity.this,
//                        "동영상 재생이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "initShuffle setOnCompletionListener");

                image_open.setVisibility(View.VISIBLE);
//                    Group groupSound = (Group) findViewById(R.id.room_group_sound);
                play_sound_bluffing.setVisibility(View.VISIBLE);
//                    groupSound.setVisibility(View.VISIBLE);

                videoView.stopPlayback();
                videoView.setVisibility(View.GONE);
            }
        });
    }

    public void finishShuffle() {
        ShuffleFragment fragment = (ShuffleFragment) getSupportFragmentManager().findFragmentById(R.id.room_shuffle_frame);
        if (fragment == null) {
            // Make new fragment to show this selection.
            fragment = new ShuffleFragment();

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    private void initListener() {
        image1.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView1);
        image2.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView2);

        loadImage(image1, getResourceId("drawable", "card_" + card1));

        image2.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(PlayActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "onDoubleTap");

                    bCheck = true;
                    changeCard();

                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewHeight = v.getHeight();
                        Log.d(TAG, "v.getHeight() : " + viewHeight);
                        Log.d(TAG, "v.getY() : " + v.getY());
                        Log.d(TAG, "event.getRawY() : " + event.getRawY());

                        startY = (int) event.getRawY();

//                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        // cancel animations
                        xAnimation.cancel();
                        yAnimation.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        int gap = startY > (int) event.getRawY() ? startY - (int) event.getRawY() : (int) event.getRawY() - startY;
                        Log.d(TAG, "viewHeight/2 : " + (viewHeight / 2) + ", gap : " + gap);

                        // 패가 절반이상 까진경우
                        if (viewHeight / 2 < gap) {
                            Log.d(TAG, "패가 절반이상 까진경우");
//                            openAnimation.start();
                            bCheck = true;
                        }

//                        xAnimation.start();
                        yAnimation.start();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        image2.animate()
//                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        image_setting.setOnTouchListener(this);
        image_exit.setOnTouchListener(this);
        image_open.setOnTouchListener(this);
        image_re.setOnTouchListener(this);
        image_die.setOnTouchListener(this);
        image_result.setOnTouchListener(this);
        image_jokbo.setOnTouchListener(this);

        image_setting.setOnDragListener(this);
        image_exit.setOnDragListener(this);
        image_open.setOnDragListener(this);
        image_re.setOnDragListener(this);
        image_die.setOnDragListener(this);
        image_result.setOnDragListener(this);
        image_jokbo.setOnDragListener(this);
    }


    private void loadSound(int index, int soundId) {
        Log.d(TAG, "loadSound (index: " + index + ", soundId: " + soundId + ")");

        soundTrack[index] = soundPool.load(PlayActivity.this, soundId, 1);
    }

    private void playSound(int index) {
        playSound(index, 1f);
    }

    private void playSound(int index, float volume) {
        Log.d(TAG, "playSound (index: " + index + ", volumn: " + volume + ")");

        soundPool.play(soundTrack[index], volume, volume, 0, 0, 1f);
    }

    private void stopSound(int index) {
        soundPool.stop(index);
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


    private void changeCard() {
        if (tempIndex == 1) {
            loadImage(image1, getResourceId("drawable", "card_" + card2));
            loadImage(image2, getResourceId("drawable", "card_" + card1));

            tempIndex++;
            bCheck = false;
        } else if (tempIndex == 2) {
            tempIndex = 1;
            bCheck = false;

            // move
            ani_view2_x.start();
            ani_view2_y.start();
            ani_view1_x.start();
            ani_view1_y.start();

            doSendMessage(API_CARD_OPEN, seatNum);

            initButton(false);
        }
    }

    private void resetCard() {
        Log.d(TAG, "resetCard");

        xAnimation.cancel();
        yAnimation.cancel();

        ret_view2_x.cancel();
        ret_view2_y.cancel();
        ret_view1_x.cancel();
        ret_view1_y.cancel();

        tempIndex = 1;
        bCheck = false;
        loadImage(image1, R.drawable.card_back);
        loadImage(image2, R.drawable.card_back);
        loadImage(image3, 0);
        loadImage(image4, 0);

        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.INVISIBLE);
        image_card_die.setVisibility(View.GONE);

        ret_view2_x.start();
        ret_view2_y.start();
        ret_view1_x.start();
        ret_view1_y.start();

        initButton(true);
    }

    private void openCard() {
        Log.d(TAG, "openCard");

        bCheck = true;
        changeCard();
        bCheck = true;
        changeCard();
    }


    private void makeRandomNumber() {
        Log.d(TAG, "makeRandomNumber");
        double d = Math.random(); // 0 <= d < 1

        Random rnd = new Random();
        int n1 = rnd.nextInt(21) + 1;
        int n2 = rnd.nextInt(21) + 1;

        if (n1 == n2) {
            n2 = rnd.nextInt(20);
        }
        Log.d(TAG, "makeRandomNumber n1 : " + n1);
        Log.d(TAG, "makeRandomNumber n2 : " + n2);

        String val = "";
        int tempNum = n1;
        if (tempNum / 11 > 0) {
            val = String.valueOf(tempNum % 10 == 0 ? 10 : tempNum % 10) + "_2";
        } else {
            val = String.valueOf(tempNum) + "_1";
        }
        card1 = val;

        tempNum = n2;
        if (tempNum / 11 > 0) {
            val = String.valueOf(tempNum % 10 == 0 ? 10 : tempNum % 10) + "_2";
        } else {
            val = String.valueOf(tempNum) + "_1";
        }
        card2 = val;

        Log.d(TAG, "Card1 : " + card1);
        Log.d(TAG, "Card2 : " + card2);
    }

    private String calcRandomNumber(int number) {
        int tempNum = number;
        String retVal = "";
        if (tempNum / 11 > 0) {
            retVal = String.valueOf(tempNum % 10 == 0 ? 10 : tempNum % 10) + "_2";
        } else {
            retVal = String.valueOf(tempNum) + "_1";
        }
        return retVal;
    }

    private void doSetting() {
        Log.d(TAG, "doSetting");
        // TODO : 세팅으로 이동
//        Intent intent = new Intent(PlayActivity.this, SettingActivity.class);
//        startActivity(intent);
        Toast.makeText(getApplicationContext(), "세팅 화면 연결해주세요", Toast.LENGTH_SHORT).show();
    }

    private void doExit() {
        Log.d(TAG, "doExit");
        doSendMessage(API_OUT, seatNum);
    }

    private void doRestart() {
        Log.d(TAG, "doRestart");

        if (isHost && enableRegame) {
            doSendMessage(API_SHUFFLE);

            // 방장이 DIE 상태에서 RE게임을 수행할 때
            image_re.setEnabled(false);
            image_result.setEnabled(false);

            showResult = false;
        } else {
            Toast.makeText(getApplicationContext(), "게임결과 확인 후 RE게임 가능", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "this is client || enableRegame is false");
        }
    }

    private void doDie() {
        Log.d(TAG, "doDie");
        doSendMessage(API_DIE, seatNum);
    }

    private void doResult() {
        Log.d(TAG, "doResult");

        if (showResult) {
            Log.d(TAG, "showResult is true");
            Game_Result();
        } else if (isHost && enableResult) {
            Log.d(TAG, "this is host & enableResult is true");
            doSendMessage(API_GAME_RESULT);
        } else {
            Log.d(TAG, "this is client & showResult is false");
        }
    }

    private void doJokbo() {
        Log.d(TAG, "doJokbo");
        // TODO :
//        Toast.makeText(getApplicationContext(), "족보 화면 연결해주세요", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, fragment2, GuideFragment.TAG)
                .commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
//        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        int eventPadTouch = event.getAction();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
//
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                switch (v.getId()) {
                    case R.id.play_image_setting:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_setting.setPressed(false);
                        doSetting();
                        break;
                    case R.id.play_image_exit:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_exit.setPressed(false);
                        doExit();
                        break;
                    case R.id.play_image_open:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_open.setPressed(false);
                        openCard();
                        break;
                    case R.id.play_image_re:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_re.setPressed(false);
                        doRestart();
                        break;
                    case R.id.play_image_die:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_die).start();

                        image_die.setPressed(false);
                        doDie();
                        break;
                    case R.id.play_image_result:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_result.setPressed(false);
                        doResult();
                        break;
                    case R.id.play_image_jokbo:
                        MediaPlayer.create(PlayActivity.this, R.raw.mouth_interface_button).start();

                        image_jokbo.setPressed(false);
                        doJokbo();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        ImageView target = (ImageView) v;
//        String targetTag = target.getTag().toString();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
//                Log.d(TAG, "ACTION_DRAG_ENTERED : " + targetTag);
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
//                Log.d(TAG, "ACTION_DRAG_EXITED : " + targetTag);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
//                Log.d(TAG, "ACTION_DRAG_ENDED : " + targetTag);
                return true;
        }
        return true;
    }

    private void loadImage(ImageView view, int resId) {
        Log.d(TAG, "loadImage (resId: " + resId + ")");
        Glide.with(this).load(resId).into(view);
    }


    public void Game_Result_Close() {

        result_back.setVisibility(View.GONE);

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .remove(fragment)
                    .commitNow();
        }
    }

    // 만땅 - 게임 결과 창
    // 결과보기 눌렀을 경우 Event
//    @OnClick(R.id.play_image_result)
    public void Game_Result() {
        Log.d(TAG, "Click Game_Result");


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_play_result, fragment).commit();

        result_back.setVisibility(View.VISIBLE);
//        result_title1.setVisibility(View.VISIBLE);
////        result_title2.setVisibility(View.VISIBLE);
//        result_content1.setVisibility(View.VISIBLE);
//        result_content1_card1.setVisibility(View.VISIBLE);
//        result_content1_card2.setVisibility(View.VISIBLE);
////        result_content2.setVisibility(View.VISIBLE);
////        result_content2_rank2_card1.setVisibility(View.VISIBLE);
////        result_content2_rank2_card2.setVisibility(View.VISIBLE);
////        result_content2_rank3_card1.setVisibility(View.VISIBLE);
////        result_content2_rank3_card2.setVisibility(View.VISIBLE);
////        result_content2_rank4_card1.setVisibility(View.VISIBLE);
////        result_content2_rank4_card2.setVisibility(View.VISIBLE);
//        result_left_button.setVisibility(View.VISIBLE);
//        result_right_button.setVisibility(View.VISIBLE);
//        result_shadow.setVisibility(View.VISIBLE);
//        result_rebutton.setVisibility(View.VISIBLE);
    }

    // 결과보기 - 화살표(왼쪽, 오른쪽) 눌렀을 경우
//    @OnClick(R.id.play_result_left_button)
//    public void Game_Result_Left(){
//        if(result_content1.getVisibility() == View.VISIBLE){
//            // result_1
////            result_content1.setVisibility(View.GONE);
////            result_content1_card1.setVisibility(View.GONE);
////            result_content1_card2.setVisibility(View.GONE);
////
////            // result_2
////            result_content2.setVisibility(View.VISIBLE);
////            result_content2_rank2_card1.setVisibility(View.VISIBLE);
////            result_content2_rank2_card2.setVisibility(View.VISIBLE);
////            result_content2_rank3_card1.setVisibility(View.VISIBLE);
////            result_content2_rank3_card2.setVisibility(View.VISIBLE);
////            result_content2_rank4_card1.setVisibility(View.VISIBLE);
////            result_content2_rank4_card2.setVisibility(View.VISIBLE);
//        }
//        else{
//            // result_1
////            result_content1.setVisibility(View.VISIBLE);
////            result_content1_card1.setVisibility(View.VISIBLE);
////            result_content1_card2.setVisibility(View.VISIBLE);
////
////            // result_2
////            result_content2.setVisibility(View.GONE);
////            result_content2_rank2_card1.setVisibility(View.GONE);
////            result_content2_rank2_card2.setVisibility(View.GONE);
////            result_content2_rank3_card1.setVisibility(View.GONE);
////            result_content2_rank3_card2.setVisibility(View.GONE);
////            result_content2_rank4_card1.setVisibility(View.GONE);
////            result_content2_rank4_card2.setVisibility(View.GONE);
//        }
//    }

//    @OnClick(R.id.play_result_right_button)
//    public void Game_Result_Right(){
//        if(result_content1.getVisibility() == View.VISIBLE){
//            // result_1
//            result_content1.setVisibility(View.GONE);
//            result_content1_card1.setVisibility(View.GONE);
//            result_content1_card2.setVisibility(View.GONE);
//
//            // result_2
//            result_content2.setVisibility(View.VISIBLE);
//            result_content2_rank2_card1.setVisibility(View.VISIBLE);
//            result_content2_rank2_card2.setVisibility(View.VISIBLE);
//            result_content2_rank3_card1.setVisibility(View.VISIBLE);
//            result_content2_rank3_card2.setVisibility(View.VISIBLE);
//            result_content2_rank4_card1.setVisibility(View.VISIBLE);
//            result_content2_rank4_card2.setVisibility(View.VISIBLE);
//        }
//        else{
//            // result_1
//            result_content1.setVisibility(View.VISIBLE);
//            result_content1_card1.setVisibility(View.VISIBLE);
//            result_content1_card2.setVisibility(View.VISIBLE);
//
//            // result_2
//            result_content2.setVisibility(View.GONE);
//            result_content2_rank2_card1.setVisibility(View.GONE);
//            result_content2_rank2_card2.setVisibility(View.GONE);
//            result_content2_rank3_card1.setVisibility(View.GONE);
//            result_content2_rank3_card2.setVisibility(View.GONE);
//            result_content2_rank4_card1.setVisibility(View.GONE);
//            result_content2_rank4_card2.setVisibility(View.GONE);
//        }
//    }

    // 결과보기 - Back 버튼 눌렀을 경우 Event
//    @OnClick(R.id.play_result_ReButton)
//    public void Game_Result_ReButton(){
//
//        result_back.setVisibility(View.GONE);
//        result_title1.setVisibility(View.GONE);
////        result_title2.setVisibility(View.GONE);
//        result_content1.setVisibility(View.GONE);
//        result_content1_card1.setVisibility(View.GONE);
//        result_content1_card2.setVisibility(View.GONE);
//
//        result_content2.setVisibility(View.GONE);
//        result_content2_rank2_card1.setVisibility(View.GONE);
//        result_content2_rank2_card2.setVisibility(View.GONE);
//        result_content2_rank3_card1.setVisibility(View.GONE);
//        result_content2_rank3_card2.setVisibility(View.GONE);
//        result_content2_rank4_card1.setVisibility(View.GONE);
//        result_content2_rank4_card2.setVisibility(View.GONE);
//
//        result_left_button.setVisibility(View.GONE);
//        result_right_button.setVisibility(View.GONE);
//        result_shadow.setVisibility(View.GONE);
//        result_rebutton.setVisibility(View.GONE);
//    }

    // 해당 id 뒤에 있는 객체에 대해서 이벤트 처리를 어떻게 할 것인가를 정하는 기능([return] true : 불가능 / false : 가능)
    @OnTouch(R.id.fragment_container_play_result)
    public boolean Click_block() {
        return true;
    }
    // 만땅 end


    private void doSendMessage(int apiNo) {
//        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ", seatNo1: " + seatNo1 + ", seatNo2: " + seatNo2 + ")");
        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ")");
        ApiBody apiBody;
        apiBody = new ApiBody(apiNo);

        ConnectionManager.sendMessage(apiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "doSendMessage success " + apiBody);
                }, onError -> {
                    onError.printStackTrace();
                });
    }

    private void doSendMessage(int apiNo, int seatNo1) {
//        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ", seatNo1: " + seatNo1 + ", seatNo2: " + seatNo2 + ")");
        Log.d(TAG, "doSendMessage (apiNo: " + apiNo + ", seatNo1: " + seatNo1 + ")");
        ApiBody apiBody;
        apiBody = new ApiBody(apiNo, seatNo1);

        ConnectionManager.sendMessage(apiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "doSendMessage success " + apiBody);
                }, onError -> {
                    onError.printStackTrace();
                });
    }


    @SuppressLint("CheckResult")
    private void initRX() {
        Log.d(TAG, "initRX");

        ClientPublishSubjectBus.getInstance().getEvents(String.class)
                .map(json -> new Gson().fromJson((String) json, ApiBody.class))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Log.d(TAG, "PlayActivity server result " + result);
                    ApiBody apiBody = ((ApiBody) result);
                    switch (apiBody.getNo()) {
                        case API_SHUFFLE_BR:
                            initShuffle();
                            resetCard();

                            card1 = calcRandomNumber(apiBody.getCardNo1());
                            card2 = calcRandomNumber(apiBody.getCardNo2());
                            Log.d(TAG, "Card1 : " + card1);
                            Log.d(TAG, "Card2 : " + card2);
                            break;

                        case API_DIE_BR:
                            loadImage(image1, R.drawable.card_die);
                            loadImage(image2, R.drawable.card_die);

                            image_card_die.setVisibility(View.VISIBLE);

                            bOpenCard = false;
                            break;

                        case API_GAME_RESULT_BR:
                            if (isHost) {
//                                image_re.setImageResource(R.drawable.button_play_re);
//                                loadImage(image_re, R.drawable.button_play_re);
                                image_re.setEnabled(true);
                                enableRegame = true;

                                if (!showResult) {
                                    image_result.setEnabled(true);
                                }
                            } else {
                                image_result.setEnabled(true);
                            }

                            Log.d("lsc", "MSMS" + apiBody.getUsers());
                            //int a = apiBody.getUsers().get(0).getCards().first;

                            Log.d("MSMS", "MSMS" + apiBody.getUsers().get(0).getCards().first);
//                            resultInfo.resultInfoMap.put("name",apiBody.getUsers().get(0).getCards().first);


                            drawCheck = false;
                            int usersSize = apiBody.getUsers().size();
                            userCheck = usersSize;
                            resultInfoMap.clear();
                            resultList.clear();

                            for (int i = usersSize - 1; i >= 0; i--) {
                                resultInfoMap.put("name" + i, apiBody.getUsers().get(i).getName());
                                resultInfoMap.put("first" + i, apiBody.getUsers().get(i).getCards().first);
                                resultInfoMap.put("second" + i, apiBody.getUsers().get(i).getCards().second);
                                resultInfoMap.put("status" + i, apiBody.getUsers().get(i).getStatus());
                                Log.d("MSMS", "MSMS" + apiBody.getUsers().get(i).getName());
                                resultList.add(resultInfoMap);

                                if (!apiBody.getDraw() && seatNum == apiBody.getUsers().get(i).getSeat()) {
                                    Log.d(TAG, "게임결과 (seatNum: " + seatNum + ")");
                                    int nTotal = dataManager.getUserTotal();
                                    int nWin = dataManager.getUserWin();

                                    dataManager.setUserTotal(nTotal + 1);
                                    if (i == 0) { // 승
                                        dataManager.setUserWin(nWin + 1);
                                        Log.d(TAG, "게임결과 (승) : " + (nTotal+1) + "전 " + (nWin + 1) + "승");
                                    } else {
                                        Log.d(TAG, "게임결과 (패) : " + (nTotal+1) + "전 " + nWin + "승");
                                    }
                                }
                            }

                            if (apiBody.getDraw() == true) {
                                drawCheck = true;
                            }

                            showResult = true;

                            Game_Result();
                            break;

                        case API_GAME_RESULT_AVAILABLE:
                            if (isHost) {
//                                image_result.setImageResource(R.drawable.button_play_result);
//                                loadImage(image_result, R.drawable.button_play_result);
                                image_result.setEnabled(true);
                                enableResult = true;
                            }
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

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void onBackPressed() {
        if (getVisibleFragmentTag(this) == GuideFragment.TAG) {
            Log.d("lsc", "PlayActivity onBackPressed 1");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(GuideFragment.TAG);
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .remove(fragment)
                        .commitNow();
            }
        } else {
            Log.d("lsc", "PlayActivity onBackPressed 2");
            super.onBackPressed();
        }
    }

    public void Game_Jokbo_Close() {
        if (fragment2 != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .remove(fragment2)
                    .commitNow();
        }
    }
}
