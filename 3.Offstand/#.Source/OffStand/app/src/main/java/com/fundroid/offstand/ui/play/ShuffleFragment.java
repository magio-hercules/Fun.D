package com.fundroid.offstand.ui.play;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fundroid.offstand.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShuffleFragment extends Fragment {
    static final String TAG = "[SHUFFLE]";


    @BindView(R.id.cardShuffle1)
    ImageView shuffleCard1;
    @BindView(R.id.cardShuffle2)
    ImageView shuffleCard2;
    @BindView(R.id.cardShuffle3)
    ImageView shuffleCard3;
    @BindView(R.id.card1)
    ImageView card1;
    @BindView(R.id.card2)
    ImageView card2;
    @BindView(R.id.card3)
    ImageView card3;
    @BindView(R.id.card4)
    ImageView card4;

    @BindView(R.id.button_reset)
    Button button_reset;


    int shuffleCount = 0;
    boolean bShuffle = false;




    public ShuffleFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.fragment_shuffle, container, false);

        // 버터나이프 사용
        ButterKnife.bind(this, retView);

        button_reset.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onClick_shuffle();
            }
        }, 1000);

        return retView;
    }


    @OnClick(R.id.button_shuffle)
    public void onClick_shuffle() {
        Log.d(TAG, "onClick_shuffle");

        if (!bShuffle) {
            bShuffle = true;
            shuffle(1);
        } else {
            bShuffle = false;
        }
    }

    @OnClick(R.id.button_reset)
    public void onClick_reset() {
        Log.d(TAG, "onClick_reset");

        reset();
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


    private void translateAni(ImageView src, ImageView dst, final int index) {
        int gapX = 30, gapY = 30;

        // 두번째 장이 위로 올라오는것처럼 보이게 z값 수정
        if (shuffleCount > 0) {
            dst.setZ(-1.0f);
        }
//        Log.d(TAG, "src Z : " + src.getZ());
//        Log.d(TAG, "dst Z : " + dst.getZ());

        TranslateAnimation animation = new TranslateAnimation(
                0,
                dst.getX()-src.getX()+(gapX*shuffleCount),
                0 ,
                dst.getY()-src.getY()+(gapY*shuffleCount));
        animation.setRepeatMode(0);
        animation.setDuration(300);
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

//                            Toast.makeText(getActivity(), "셔플 완료!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "end Shuffle");

                            doFinish();
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


    private void reset() {
        Log.d(TAG, "reset");

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


    private void doFinish() {
        Log.d(TAG, "doFinish");
        // #type 1 : room에서 호출되는 경우, play 호출
//        Intent intent = new Intent(getActivity(), PlayActivity.class);
//        startActivity(intent);

        // #type 2 : play에서 호출되는 경우, shuffle fragment hide
        ShuffleFragment fragment = (ShuffleFragment) getFragmentManager().findFragmentById(R.id.room_shuffle_frame);
        if (fragment != null) {
            Log.d(TAG, "fragment is not null");
            // Make new fragment to show this selection.
//            fragment = new ShuffleFragment();

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Log.d(TAG, "fragment is null");
        }
    }
}
