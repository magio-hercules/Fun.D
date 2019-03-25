package com.example.eom.offstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PlayActivity extends AppCompatActivity {
    static final String TAG = "[PLAY]";

    @BindView(R.id.ImageView01)
    ImageView image1;
    @BindView(R.id.ImageView02)
    ImageView image2;
    @BindView(R.id.ImageView03)
    ImageView image3;
    @BindView(R.id.ImageView04)
    ImageView image4;

    @BindView(R.id.text_card1)
    TextView text1;
    @BindView(R.id.text_card2)
    TextView text2;

    @BindView(R.id.button_reset)
    Button button_reset;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // 버터나이프 사용
        ButterKnife.bind(this);

        button_reset.setEnabled(false);

        image1.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView1);
        image2.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView2);

        makeRandomNumber();
        image1.setImageResource(getResourceId("drawable", "card_" + card1));
    }


    @OnClick({ R.id.button_reset, R.id.button_shuffle, R.id.button_random })
    public void onClickButton(View view) {
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
        }
    }


    @OnTouch(R.id.ImageView02)
    public boolean onTouchView(View v, MotionEvent event) {
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

            org_x = image1.getX();
            org_y = image1.getY();

            ret_view1_x = createSpringAnimation(image1, SpringAnimation.X, org_x,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            ret_view1_y = createSpringAnimation(image1, SpringAnimation.Y, org_y,
                    SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);

            ret_view1_x.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                    Log.d(TAG, "Reset 완료");

                    makeRandomNumber();

                    image1.setImageResource(getResourceId("drawable", "card_" + card1));
                    image2.setImageResource(R.drawable.card_back);
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

            org_x = image2.getX();
            org_y = image2.getY();

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
        }
    }


    private void resetCard() {
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
}
