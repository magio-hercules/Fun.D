package group.study.playsample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class ShuffleActivity extends Activity {
    Button button_main;
    Button button_reset;
    Button button_shuffle;
    Button button_test;

    Button imageButton;
    ImageButton imageButton_test;

    ImageView shuffleCard1, shuffleCard2, shuffleCard3;
    ImageView card1, card2, card3, card4;

    ImageView test;

    private SpringAnimation shuffleAniX1, shuffleAniY1;
    private SpringAnimation shuffleAniX2, shuffleAniY2;
    private SpringAnimation shuffleAniX3, shuffleAniY3;

    int shuffleCount = 0;
    boolean bShuffle = false;

    public ShuffleActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle);


        button_main = (Button) findViewById(R.id.button_main);
        button_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShuffleActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_reset = (Button) findViewById(R.id.button_reset);
        button_reset.setEnabled(false);
        button_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });


        button_shuffle = (Button) findViewById(R.id.button_shuffle);
        button_shuffle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!bShuffle) {
                    bShuffle = true;
                    shuffle(1);
                } else {
                    bShuffle = false;
                }
            }
        });

        button_test = (Button) findViewById(R.id.button_test);
        button_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                test();
            }
        });

//        imageButton_test = (ImageButton) findViewById(R.id.imgbtn_test);
//        imageButton_test.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Log.d("[Shuffle]", "imageButton_test.setOnClickListener");
//            }
//        });

        shuffleCard1 = (ImageView) findViewById(R.id.cardShuffle1);
        shuffleCard2 = (ImageView) findViewById(R.id.cardShuffle2);
        shuffleCard3 = (ImageView) findViewById(R.id.cardShuffle3);
//        shuffleCard1.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerShuffleCard1);
//        shuffleCard2.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerShuffleCard2);
//        shuffleCard3.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListenerShuffleCard3);

        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        card3 = (ImageView) findViewById(R.id.card3);
        card4 = (ImageView) findViewById(R.id.card4);

        test = (ImageView) findViewById(R.id.image_test);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("[Shuffle]", "test.setOnClickListener");
            }
        });

        addListenerOnButton();
    }


    public void addListenerOnButton() {

        imageButton = (Button) findViewById(R.id.imageButtonSelector);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(ShuffleActivity.this,
                        "ImageButton (selector) is clicked!",
                        Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void shuffle(int index) {
        Log.d("[Shuffle]", "shuffle");

        switch (index) {
            case 1:
                translateAni(shuffleCard1, card2, index);
                break;
            case 2:
                translateAni(shuffleCard2, card3, index);
                break;
            case 3:
                translateAni(shuffleCard3, card4, index);
                break;
        }
    }

    private void shuffleR(int index) {
        Log.d("[Shuffle]", "shuffle");

        switch (index) {
            case 1:
                translateAni(card2, shuffleCard1, index);
                break;
            case 2:
                translateAni(card3, shuffleCard2, index);
                break;
            case 3:
                translateAni(card4, shuffleCard3, index);
                break;
        }
    }

    private void reset() {
        Log.d("[Shuffle]", "reset");

        shuffleCount = 0;
        bShuffle = false;

        translateAni(shuffleCard1, card1, 0);
        translateAni(shuffleCard2, card1, 0);
        translateAni(shuffleCard3, card1, 0);

        card2.setVisibility(View.INVISIBLE);
        card3.setVisibility(View.INVISIBLE);
        card4.setVisibility(View.INVISIBLE);

        button_reset.setEnabled(false);
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

    private void shuffleAnimation(ImageView srcImage, ImageView dstImage) {
        Log.d("[Shuffle]", "shuffleAnimation");

        shuffleAniX1 = createSpringAnimation(srcImage, SpringAnimation.X, dstImage.getX(),
                SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        shuffleAniY1 = createSpringAnimation(srcImage, SpringAnimation.Y, dstImage.getY(),
                SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
    }

    private void translateAni(ImageView src, ImageView dst, final int index) {
        int gapX = 30, gapY = 30;

        TranslateAnimation animation = new TranslateAnimation(
                0,
                dst.getX()-src.getX()+(gapX*shuffleCount),
                0 ,
                dst.getY()-src.getY()+(gapY*shuffleCount));
        animation.setRepeatMode(0);
        animation.setDuration(250);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (!bShuffle) {
                    return;
                }

                switch (index) {
                    case 1:
                        card2.setVisibility(View.VISIBLE);
                        shuffle(2);
                        break;
                    case 2:
                        card3.setVisibility(View.VISIBLE);
                        shuffle(3);
                        break;
                    case 3:
                        if (shuffleCount > 0) {
                            shuffleCount = 0;
                            button_reset.setEnabled(true);
                            return;
                        }
                        card4.setVisibility(View.VISIBLE);
                        shuffleCount++;
                        shuffle(1);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        src.startAnimation(animation);
    }

//    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerShuffleCard1 = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            shuffleAniX1.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
//                @Override
//                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
////                    shuffleAniX1.cancel();
////                    shuffleAniY1.cancel();
//
////                    shuffleAnimation(shuffleCard, card3);
//                    shuffle(2);
//                }
//            });
//        }
//    };
//
//    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerShuffleCard2 = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            shuffleAniX1.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
//                @Override
//                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
////                    shuffleAnimation(shuffleCard, card3);
//                    shuffle(3);
//                }
//            });
//        }
//    };
//
//    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerShuffleCard3 = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            shuffleAniX1.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
//                @Override
//                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
////                    shuffleAnimation(shuffleCard, card3);
//                    shuffle(1);
//                }
//            });
//        }
//    };



        private void test() {
    }

    private void resetShuffleCard() {
//        Log.d("[Shuffle]", "resetShuffleCard");
//        LayoutParams layoutParams = (LayoutParams) shuffleCard.getLayoutParams();
//        Log.d("[Shuffle]", "resetShuffleCard layoutParams : (" + layoutParams.leftMargin + ", " + layoutParams.topMargin + ", " + layoutParams.rightMargin + ", " + layoutParams.bottomMargin + ")");
//
//        LayoutParams orgPos = (LayoutParams) card1.getLayoutParams();
////
//        LayoutParams params = new LayoutParams(card1.getWidth(), card1.getHeight());
//        params.setMargins(orgPos.leftMargin, orgPos.topMargin, orgPos.rightMargin, orgPos.bottomMargin);
//
//        shuffleCard.setLayoutParams(params);
//
////        ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(card1.getLayoutParams());
////        mp.setMargins(orgPos.leftMargin, orgPos.topMargin, orgPos.rightMargin, orgPos.bottomMargin);
////        FrameLayout.LayoutParams fp = new LayoutParams(mp);
////        shuffleCard.setLayoutParams(fp);
//
//        layoutParams = (LayoutParams) shuffleCard.getLayoutParams();
//        Log.d("[Shuffle]", "resetShuffleCard layoutParams : (" + layoutParams.leftMargin + ", " + layoutParams.topMargin + ", " + layoutParams.rightMargin + ", " + layoutParams.bottomMargin + ")");
    }

}
