package group.study.playsample;

import android.content.Intent;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;

    TextView text1;
    TextView text2;

    Button button_reset;
    Button button_shuffle;
    Button button_random;

    int windowwidth;
    int windowheight;

    int viewHeight;

    private LayoutParams layoutParams;
    int startY;
    int startMarginTop;

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

    boolean bCheck = false;
    int tempIndex = 1;


    String card1, card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();
        image1 = (ImageView) findViewById(R.id.ImageView01);
        image2 = (ImageView) findViewById(R.id.ImageView02);
        image3 = (ImageView) findViewById(R.id.ImageView03);
        image4 = (ImageView) findViewById(R.id.ImageView04);


        text1 = (TextView) findViewById(R.id.text_card1);
        text2 = (TextView) findViewById(R.id.text_card2);

        button_reset = (Button) findViewById(R.id.button_reset);
        button_reset.setEnabled(false);

        button_shuffle = (Button) findViewById(R.id.button_shuffle);

        button_random = (Button) findViewById(R.id.button_random);

        image1.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView1);
        image2.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerView2);

        image2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LayoutParams layoutParams = (LayoutParams) image2.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        startY = (int) event.getRawY();
//                        startMarginTop = layoutParams.topMargin;
//                        Log.d("[PLAY]", "startY : " + startY);

                        if (button_reset.isEnabled()) {
                            return false;
                        }

                        viewHeight = v.getHeight();
                        Log.d("[PLAY]", "v.getHeight() : " + viewHeight);
                        Log.d("[PLAY]", "v.getY() : " + v.getY());
                        Log.d("[PLAY]", "event.getRawY() : " + event.getRawY());

                        startY = (int) event.getRawY();

                        // capture the difference between view's top left corner and touch point
//                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        // cancel animations
                        xAnimation.cancel();
                        yAnimation.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
//                        startY = (int) event.getRawY();
//                        startMarginTop = layoutParams.topMargin;
//                        Log.d("[PLAY]", "startY : " + startY);


                        int gap = startY > (int)event.getRawY() ? startY - (int)event.getRawY() : (int)event.getRawY() - startY;
                        Log.d("[PLAY]", "viewHeight/2 : " + (viewHeight/2) + ", gap : " + gap);

                        // 패가 절반이상 까진경우
                        if (viewHeight/2 < gap) {
                            Log.d("[PLAY]", "패가 절반이상 까진경우");
//                            openAnimation.start();
                            bCheck = true;
                        }

//                        xAnimation.start();
                        yAnimation.start();
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        int x_cord = (int) event.getRawX();
//                        int y_cord = (int) event.getRawY();
//
//                        Log.d("[PLAY]", "y_cord : " + y_cord);
//
//                        if (x_cord > windowwidth) {
//                            x_cord = windowwidth;
//                        }
//                        if (y_cord > windowheight) {
//                            y_cord = windowheight;
//                        }
//
////                        layoutParams.leftMargin = x_cord - 25;
////                        layoutParams.topMargin = y_cord - 75;
////                        layoutParams.leftMargin = x_cord;
////                        layoutParams.topMargin = y_cord;
////                        image2.setLayoutParams(layoutParams);
//
//                        int gap = y_cord - startY;
//                        Log.d("[PLAY]", "gap : " + gap);
////                        if (startY < y_cord) {
////                            gap = y_cord - startY;
////                            Log.d("[PLAY]", "gap : " + gap);
////                        } else {
////                            gap = startY - y_cord;
////                            Log.d("[PLAY]", "gap : " + gap);
////                        }
////                        layoutParams.topMargin = y_cord;
//                        layoutParams.topMargin = startMarginTop + gap;
//                        image2.setLayoutParams(layoutParams);


//                        Log.d("[PLAY]", "v.getY() : " + v.getY() + ", event.getRawY() : " + event.getRawY());

                        //  a different approach would be to change the view's LayoutParams.
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

        button_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetCard();
            }
        });

        button_shuffle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShuffleActivity.class);
                startActivity(intent);
            }
        });

        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomActivity.class);
                startActivity(intent);
            }
        });

        makeRandomNumber();
        image1.setImageResource(getResourceId("drawable", "card_" + card1));
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

    // create X and Y animations for view's initial position once it's known
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
                    Log.d("[PLAY]", "Reset 완료");

                    makeRandomNumber();

                    image1.setImageResource(getResourceId("drawable", "card_" + card1));
                    image2.setImageResource(R.drawable.card_back);
                }
            });
        }
    };

    // create X and Y animations for view's initial position once it's known
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
//            text1.setText("첫번째 패 : 3光");
            text1.setText("첫번째 패 : " + card1);
//            Toast.makeText(this, "3光", Toast.LENGTH_SHORT).show();
//            image1.setImageResource(R.drawable.card_8_1);
//            image2.setImageResource(R.drawable.card_3_1);
            image1.setImageResource(getResourceId("drawable", "card_" + card2));
            image2.setImageResource(getResourceId("drawable", "card_" + card1));

            tempIndex++;
            bCheck = false;
        } else  if (tempIndex == 2){
//            text2.setText("두번째 패 : 8光");
//            Toast.makeText(this, "8光", Toast.LENGTH_SHORT).show();
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
        Log.d("[PLAY]", "makeRandomNumber");
        double d = Math.random(); // 0 <= d < 1

        Random rnd = new Random();
        int n1 = rnd.nextInt(21) + 1;
        int n2 = rnd.nextInt(21) + 1;

        if (n1 == n2) {
            n2 = rnd.nextInt(20);
        }
        Log.d("[PLAY]", "makeRandomNumber n1 : " + n1);
        Log.d("[PLAY]", "makeRandomNumber n2 : " + n2);

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

        Log.d("[PLAY]", "Card1 : " + card1);
        Log.d("[PLAY]", "Card2 : " + card2);
    }
}
