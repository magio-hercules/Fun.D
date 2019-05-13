package com.fundroid.offstand;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PlayActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
    static final String TAG = "[PLAY]";

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

    @BindView(R.id.text_card1)
    TextView text1;
    @BindView(R.id.text_card2)
    TextView text2;

    @BindView(R.id.button_reset)
    Button button_reset;

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

    private String card1, card2;


    // TODO
    // 참여인원
    // 잔여 시간
    // 진행 게임
    // 잔여 게임


    // 만땅 - 게임 결과 화면
    FrameLayout result_back;
    ImageView result_title1;
    ImageView result_title2;
    ImageView result_content1;
    ImageView result_content2;
    ImageView result_shadow;
    ImageView result_rebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // 버터나이프 사용
        ButterKnife.bind(this);

        initShuffle();

        initCardImage();

        button_reset.setEnabled(false);

        image1.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView1);
        image2.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView2);

        makeRandomNumber();
        image1.setImageResource(getResourceId("drawable", "card_" + card1));

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
                        if (button_reset.isEnabled()) {
                            return false;
                        }

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
                        int gap = startY > (int)event.getRawY() ? startY - (int)event.getRawY() : (int)event.getRawY() - startY;
                        Log.d(TAG, "viewHeight/2 : " + (viewHeight/2) + ", gap : " + gap);

                        // 패가 절반이상 까진경우
                        if (viewHeight/2 < gap) {
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

        initListener();

        initButton(true);

        // 만땅 - 셋팅
        result_back = (FrameLayout) findViewById(R.id.fragment_container_play_result);
        result_title1 = (ImageView) findViewById(R.id.play_result_title1);
        result_title2 = (ImageView) findViewById(R.id.play_result_title2);
        result_content1 = (ImageView) findViewById(R.id.play_result_content1);
        result_content2 = (ImageView) findViewById(R.id.play_result_content2);
        result_shadow = (ImageView) findViewById(R.id.play_result_shadow);
        result_rebutton = (ImageView) findViewById(R.id.play_result_ReButton);


        // 만땅 - 기존에 안보이게 셋팅
        result_back.setVisibility(View.GONE);
        result_title1.setVisibility(View.GONE);
        result_title2.setVisibility(View.GONE);
        result_content1.setVisibility(View.GONE);
        result_content2.setVisibility(View.GONE);
        result_shadow.setVisibility(View.GONE);
        result_rebutton.setVisibility(View.GONE);
    }


    @OnClick({
            R.id.button_reset
//            R.id.button_shuffle,
//            R.id.button_random,
             })
    public void onClickButton(View view) {
        Log.d(TAG, "clicked 22");

        switch(view.getId()) {
            case R.id.button_reset:
                resetCard();
                break;
            case R.id.button_shuffle:
//              Intent intent = new Intent(PlayActivity.this, ShuffleActivity.class);
//              startActivity(intent);
                break;
            case R.id.button_random:
//              Intent intent = new Intent(MainActivity.this, RandomActivity.class);
//              startActivity(intent);
                break;
//            case R.id.button_open:
//                openCard();
//                break;
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


//    @OnTouch(R.id.ImageView02)
//    public boolean onTouchView(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (button_reset.isEnabled()) {
//                    return false;
//                }
//
//                viewHeight = v.getHeight();
//                Log.d(TAG, "v.getHeight() : " + viewHeight);
//                Log.d(TAG, "v.getY() : " + v.getY());
//                Log.d(TAG, "event.getRawY() : " + event.getRawY());
//
//                startY = (int) event.getRawY();
//
////                        dX = v.getX() - event.getRawX();
//                dY = v.getY() - event.getRawY();
//                // cancel animations
//                xAnimation.cancel();
//                yAnimation.cancel();
//                break;
//            case MotionEvent.ACTION_UP:
//                int gap = startY > (int)event.getRawY() ? startY - (int)event.getRawY() : (int)event.getRawY() - startY;
//                Log.d(TAG, "viewHeight/2 : " + (viewHeight/2) + ", gap : " + gap);
//
//                // 패가 절반이상 까진경우
//                if (viewHeight/2 < gap) {
//                    Log.d(TAG, "패가 절반이상 까진경우");
////                            openAnimation.start();
//                    bCheck = true;
//                }
//
////                        xAnimation.start();
//                yAnimation.start();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                image2.animate()
////                                .x(event.getRawX() + dX)
//                        .y(event.getRawY() + dY)
//                        .setDuration(0)
//                        .start();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }


    public static SpringAnimation createSpringAnimation(View view,
                                                        DynamicAnimation.ViewProperty property,
                                                        float finalPosition,
                                                        float stiffness,
                                                        float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new  SpringForce(finalPosition);
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

                    makeRandomNumber();

//                    image1.setImageResource(getResourceId("drawable", "card_" + card1));
//                    image2.setImageResource(R.drawable.card_back);
                    image1.setImageResource(getResourceId("drawable", "card_" + card1));
                    Log.d("[PLAY]", "Reset 완료11");
                    image2.setImageResource(R.drawable.card_back);
                    Log.d("[PLAY]", "Reset 완료22");
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
            image_open.setVisibility(View.VISIBLE);

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
        }
    }

    private void initCardImage() {
        image0 = (ImageView) findViewById(R.id.play_image_card0);
        image1 = (ImageView) findViewById(R.id.play_image_card1);
        image2 = (ImageView) findViewById(R.id.play_image_card2);
        image3 = (ImageView) findViewById(R.id.play_image_card3);
        image4 = (ImageView) findViewById(R.id.play_image_card4);
    }

    private void initShuffle() {
        ShuffleFragment fragment = (ShuffleFragment) getSupportFragmentManager().findFragmentById(R.id.room_shuffle_frame);
        if (fragment == null) {
            // Make new fragment to show this selection.
            fragment = new ShuffleFragment();

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.room_shuffle_frame, fragment);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
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
            text1.setText("첫번째 패 : " + card1);
//            Toast.makeText(this, "3光", Toast.LENGTH_SHORT).show();
//            image1.setImageResource(R.drawable.card_8_1);
//            image2.setImageResource(R.drawable.card_3_1);
            image1.setImageResource(getResourceId("drawable", "card_" + card2));
            image2.setImageResource(getResourceId("drawable", "card_" + card1));

            tempIndex++;
            bCheck = false;
        } else  if (tempIndex == 2){
            text2.setText("두번째 패 : " + card2);
            tempIndex = 1;
            bCheck = false;

            button_reset.setEnabled(true);
            // move
            ani_view2_x.start();
            ani_view2_y.start();
            ani_view1_x.start();
            ani_view1_y.start();

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
        image1.setImageResource(R.drawable.card_back);
        image2.setImageResource(R.drawable.card_back);

        image3.setImageResource(0);
        image4.setImageResource(0);
        text1.setText("첫번째 패 : ");
        text2.setText("두번째 패 : ");

        button_reset.setEnabled(false);

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
        if (tempNum/11 > 0) {
            val = String.valueOf(tempNum%10 == 0 ? 10 : tempNum%10) + "_2";
        } else {
            val = String.valueOf(tempNum) + "_1";
        }
        card1 = val;

        tempNum = n2;
        if (tempNum/11 > 0) {
            val = String.valueOf(tempNum%10 == 0 ? 10 : tempNum%10) + "_2";
        } else {
            val = String.valueOf(tempNum) + "_1";
        }
        card2 = val;

        Log.d(TAG, "Card1 : " + card1);
        Log.d(TAG, "Card2 : " + card2);
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
        // TODO : 로비로 이동하도록 변경
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void doRestart() {
        Log.d(TAG, "doRestart");
        // TODO :
//        Toast.makeText(getApplicationContext(), "RE 게임", Toast.LENGTH_SHORT).show();

        resetCard();
    }

    private void doDie() {
        Log.d(TAG, "doDie");
        // TODO :
        Toast.makeText(getApplicationContext(), "DIE", Toast.LENGTH_SHORT).show();
    }

    private void doResult() {
        Log.d(TAG, "doResult");
        // TODO :
        Toast.makeText(getApplicationContext(), "게임 결과 연결해주세요", Toast.LENGTH_SHORT).show();
    }

    private void doJokbo() {
        Log.d(TAG, "doJokbo");
        // TODO :
        Toast.makeText(getApplicationContext(), "족보 화면 연결해주세요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

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
                        image_setting.setPressed(false);
                        doSetting();
                        break;
                    case R.id.play_image_exit:
                        image_exit.setPressed(false);
                        doExit();
                        break;
                    case R.id.play_image_open:
                        image_open.setPressed(false);
                        openCard();
                        break;
                    case R.id.play_image_re:
                        image_re.setPressed(false);
                        doRestart();
                        break;
                    case R.id.play_image_die:
                        image_die.setPressed(false);
                        doDie();
                        break;
                    case R.id.play_image_result:
                        image_result.setPressed(false);
                        doResult();
                        Game_Result();  // 만땅
                        break;
                    case R.id.play_image_jokbo:
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
        ImageView target = (ImageView)v;
        String targetTag = target.getTag().toString();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ACTION_DRAG_ENTERED : " + targetTag);
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED : " + targetTag);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED : " + targetTag);
                return true;
        }
        return true;
    }

    // 만땅 - 게임 결과 창
    // 결과보기 눌렀을 경우 Event
    @OnClick(R.id.play_image_result)
    public void Game_Result(){
        Log.d(TAG, "Click Game_Result");

        result_back.setVisibility(View.VISIBLE);
        result_title1.setVisibility(View.VISIBLE);
        result_title2.setVisibility(View.VISIBLE);
        result_content1.setVisibility(View.VISIBLE);
        result_content2.setVisibility(View.VISIBLE);
        result_shadow.setVisibility(View.VISIBLE);
        result_rebutton.setVisibility(View.VISIBLE);
    }

    // 결과보기 - Re 게임 버튼 눌렀을 경우 Event
    @OnClick(R.id.play_result_ReButton)
    public void Game_Result_ReButton(){

        result_back.setVisibility(View.GONE);
        result_title1.setVisibility(View.GONE);
        result_title2.setVisibility(View.GONE);
        result_content1.setVisibility(View.GONE);
        result_content2.setVisibility(View.GONE);
        result_shadow.setVisibility(View.GONE);
        result_rebutton.setVisibility(View.GONE);
    }

    // 해당 id 뒤에 있는 객체에 대해서 이벤트 처리를 어떻게 할 것인가를 정하는 기능([return] true : 불가능 / false : 가능)
    @OnTouch(R.id.fragment_container_play_result)
    public boolean Click_block(){
        return true;
    }
    // 만땅 end
}
