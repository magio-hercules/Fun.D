package com.fundroid.offstand.ui.play;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.model.ApiBody;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.fundroid.offstand.ui.play.PlayActivity.drawCheck;
import static com.fundroid.offstand.ui.play.PlayActivity.resultList;
import static com.fundroid.offstand.ui.play.PlayActivity.userCheck;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameResultFragment extends Fragment {

    // 만땅 - 필요한 요소 선언
    ImageView play_result_title1;
    ImageView play_result_content1;
    TextView play_result_rank1_name;
    ImageView play_result_content1_card1;
    ImageView play_result_content1_card2;
    ImageView play_result_content2;
    TextView play_result_rank2_name;
    ImageView play_result_rank2_card1;
    ImageView play_result_rank2_card2;
    TextView play_result_rank3_name;
    ImageView play_result_rank3_card1;
    ImageView play_result_rank3_card2;
    TextView play_result_rank4_name;
    ImageView play_result_rank4_card1;
    ImageView play_result_rank4_card2;
    ImageView play_result_left_button;
    ImageView play_result_right_button;
    ImageView play_result_shadow;
    ImageView play_result_rank2_die;
    ImageView play_result_rank3_die;
    ImageView play_result_rank4_die;

    @BindView(R.id.play_result_ReButton)
    ImageView play_result_ReButton;

    PlayActivity playActivity;

    ApiBody apiBody;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GameResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameResultFragment newInstance(String param1, String param2) {
        GameResultFragment fragment = new GameResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        int[] oneFirstCard = {0,
                R.drawable.one_card_1_1, R.drawable.one_card_2_1,
                R.drawable.one_card_3_1, R.drawable.one_card_4_1,
                R.drawable.one_card_5_1, R.drawable.one_card_6_1,
                R.drawable.one_card_7_1, R.drawable.one_card_8_1,
                R.drawable.one_card_9_1, R.drawable.one_card_10_1,
                R.drawable.one_card_1, R.drawable.one_card_2,
                R.drawable.one_card_3, R.drawable.one_card_4,
                R.drawable.one_card_5, R.drawable.one_card_6,
                R.drawable.one_card_7, R.drawable.one_card_8,
                R.drawable.one_card_9, R.drawable.one_card_10,};

        int[] oneSecondCard = {0,
                R.drawable.one_card_1_1, R.drawable.one_card_2_1,
                R.drawable.one_card_3_1, R.drawable.one_card_4_1,
                R.drawable.one_card_5_1, R.drawable.one_card_6_1,
                R.drawable.one_card_7_1, R.drawable.one_card_8_1,
                R.drawable.one_card_9_1, R.drawable.one_card_10_1,
                R.drawable.one_card_1, R.drawable.one_card_2,
                R.drawable.one_card_3, R.drawable.one_card_4,
                R.drawable.one_card_5, R.drawable.one_card_6,
                R.drawable.one_card_7, R.drawable.one_card_8,
                R.drawable.one_card_9, R.drawable.one_card_10,};

        int[] twoFirstCard = {0,
                R.drawable.two_card_1_1, R.drawable.two_card_2_1,
                R.drawable.two_card_3_1, R.drawable.two_card_4_1,
                R.drawable.two_card_5_1, R.drawable.two_card_6_1,
                R.drawable.two_card_7_1, R.drawable.two_card_8_1,
                R.drawable.two_card_9_1, R.drawable.two_card_10_1,
                R.drawable.two_card_1, R.drawable.two_card_2,
                R.drawable.two_card_3, R.drawable.two_card_4,
                R.drawable.two_card_5, R.drawable.two_card_6,
                R.drawable.two_card_7, R.drawable.two_card_8,
                R.drawable.two_card_9, R.drawable.two_card_10,};

        int[] twoSecondCard = {0,
                R.drawable.two_card_1_1, R.drawable.two_card_2_1,
                R.drawable.two_card_3_1, R.drawable.two_card_4_1,
                R.drawable.two_card_5_1, R.drawable.two_card_6_1,
                R.drawable.two_card_7_1, R.drawable.two_card_8_1,
                R.drawable.two_card_9_1, R.drawable.two_card_10_1,
                R.drawable.two_card_1, R.drawable.two_card_2,
                R.drawable.two_card_3, R.drawable.two_card_4,
                R.drawable.two_card_5, R.drawable.two_card_6,
                R.drawable.two_card_7, R.drawable.two_card_8,
                R.drawable.two_card_9, R.drawable.two_card_10,};
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_gameresult, container, false);

        // 만땅 - 초기화
        play_result_title1 = (ImageView) rootview.findViewById(R.id.play_result_title1);
        play_result_content1 = (ImageView) rootview.findViewById(R.id.play_result_content1);
        play_result_content1_card1 = (ImageView) rootview.findViewById(R.id.play_result_content1_card1);
        play_result_content1_card2 = (ImageView) rootview.findViewById(R.id.play_result_content1_card2);
        play_result_content2 = (ImageView) rootview.findViewById(R.id.play_result_content2);
        play_result_rank2_card1 = (ImageView) rootview.findViewById(R.id.play_result_rank2_card1);
        play_result_rank2_card2 = (ImageView) rootview.findViewById(R.id.play_result_rank2_card2);
        play_result_rank3_card1 = (ImageView) rootview.findViewById(R.id.play_result_rank3_card1);
        play_result_rank3_card2 = (ImageView) rootview.findViewById(R.id.play_result_rank3_card2);
        play_result_rank4_card1 = (ImageView) rootview.findViewById(R.id.play_result_rank4_card1);
        play_result_rank4_card2 = (ImageView) rootview.findViewById(R.id.play_result_rank4_card2);
        play_result_left_button = (ImageView) rootview.findViewById(R.id.play_result_left_button);
        play_result_right_button = (ImageView) rootview.findViewById(R.id.play_result_right_button);
        play_result_shadow = (ImageView) rootview.findViewById(R.id.play_result_shadow);
        play_result_rank1_name = (TextView) rootview.findViewById(R.id.play_result_content1_rank1_name);
        play_result_rank2_name = (TextView) rootview.findViewById(R.id.play_result_content2_rank2_name);
        play_result_rank3_name = (TextView) rootview.findViewById(R.id.play_result_content2_rank3_name);
        play_result_rank4_name = (TextView) rootview.findViewById(R.id.play_result_content2_rank4_name);
        play_result_rank2_die = (ImageView) rootview.findViewById(R.id.play_result_rank2_die);
        play_result_rank3_die = (ImageView) rootview.findViewById(R.id.play_result_rank3_die);
        play_result_rank4_die = (ImageView) rootview.findViewById(R.id.play_result_rank4_die);

        // 만땅 - 기본 셋팅
        play_result_title1.setVisibility(rootview.VISIBLE);
        play_result_content1.setVisibility(rootview.VISIBLE);
        play_result_rank1_name.setVisibility(rootview.VISIBLE);
        play_result_content1_card1.setVisibility(rootview.VISIBLE);
        play_result_content1_card2.setVisibility(rootview.VISIBLE);
        play_result_content2.setVisibility(rootview.GONE);
        play_result_rank2_name.setVisibility(rootview.GONE);
        play_result_rank2_card1.setVisibility(rootview.GONE);
        play_result_rank2_card2.setVisibility(rootview.GONE);
        play_result_rank3_name.setVisibility(rootview.GONE);
        play_result_rank3_card1.setVisibility(rootview.GONE);
        play_result_rank3_card2.setVisibility(rootview.GONE);
        play_result_rank4_name.setVisibility(rootview.GONE);
        play_result_rank4_card1.setVisibility(rootview.GONE);
        play_result_rank4_card2.setVisibility(rootview.GONE);
        play_result_left_button.setVisibility(rootview.VISIBLE);
        play_result_right_button.setVisibility(rootview.VISIBLE);
        play_result_shadow.setVisibility(rootview.VISIBLE);
        play_result_rank2_die.setVisibility(rootview.GONE);
        play_result_rank3_die.setVisibility(rootview.GONE);
        play_result_rank4_die.setVisibility(rootview.GONE);

        //드로우 일경우
        if (drawCheck) {
            play_result_content1.setImageResource(R.drawable.drawcheck);
            play_result_rank1_name.setVisibility(rootview.GONE);
            play_result_content1_card1.setVisibility(rootview.GONE);
            play_result_content1_card2.setVisibility(rootview.GONE);
            play_result_left_button.setVisibility(rootview.GONE);
            play_result_right_button.setVisibility(rootview.GONE);
        }
        //드로우가 아니면
        else {

            int drawcnt = 0;

            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> listmap = (Map<String, Object>) resultList.get(i);
                int first = (int) listmap.get("first" + i);
                int second = (int) listmap.get("second" + i);
                String name = (String) listmap.get("name" + i);
                int status = (int) listmap.get("status" + i);


                //status == 4 다이 일경우
                if (status == 4) {
                    drawcnt++;
                    //1등은 다이가 될 경우가 없음 -> 무조건 1명은 삼
                    if (i == 0) {

                        Log.d("kkkk", "kkkk" + "BBB");

                        play_result_rank1_name.setText(name);
                        play_result_content1_card1.setImageResource(oneFirstCard[first]);
                        play_result_content1_card2.setImageResource(oneSecondCard[second]);

                        //2등
                    } else if (i == 1) {
                        play_result_rank2_name.setText(name);
                        play_result_rank2_card1.setImageResource(R.drawable.card_die);
                        play_result_rank2_card2.setImageResource(R.drawable.card_die);
                        play_result_rank2_die.setImageResource(R.drawable.die_card);

                        //3등
                    } else if (i == 2) {
                        play_result_rank3_name.setText(name);
                        play_result_rank3_card1.setImageResource(R.drawable.card_die);
                        play_result_rank3_card2.setImageResource(R.drawable.card_die);
                        play_result_rank3_die.setImageResource(R.drawable.die_card);

                        //4등
                    } else if (i == 3) {
                        play_result_rank4_name.setText(name);
                        play_result_rank4_card1.setImageResource(R.drawable.card_die);
                        play_result_rank4_card2.setImageResource(R.drawable.card_die);
                        play_result_rank4_die.setImageResource(R.drawable.die_card);
//                        play_result_content1_card1.setImageResource(R.drawable.card_back);
//                        play_result_content1_card2.setImageResource(R.drawable.card_back);
                    }
                }
                //다이가 아닐경우
                else {

                    if (i == 0) {

                        Log.d("kkkk", "kkkk" + "AAA");

                        play_result_rank1_name.setText(name);
                        play_result_content1_card1.setImageResource(oneFirstCard[first]);
                        play_result_content1_card2.setImageResource(oneSecondCard[second]);
                        //textView.setText(name)

                    } else if (i == 1) {
                        play_result_rank2_name.setText(name);
                        play_result_rank2_card1.setImageResource(twoFirstCard[first]);
                        play_result_rank2_card2.setImageResource(twoSecondCard[second]);
                    } else if (i == 2) {
                        play_result_rank3_name.setText(name);
                        play_result_rank3_card1.setImageResource(twoFirstCard[first]);
                        play_result_rank3_card2.setImageResource(twoSecondCard[second]);
                    } else if (i == 3) {
                        play_result_rank4_name.setText(name);
                        play_result_rank4_card1.setImageResource(twoFirstCard[first]);
                        play_result_rank4_card2.setImageResource(twoSecondCard[second]);
                    }

                }
            }


            // 한명일때 다이 했을 때도 패 뒤집어야 되는지 검토 그럴필요 없을꺼 같긴함
            // 다이체크 // 1등 다 다이해서
            if (userCheck > 1) {
                if (userCheck - 1 == drawcnt) {
                    play_result_content1_card1.setImageResource(R.drawable.card_back);
                    play_result_content1_card2.setImageResource(R.drawable.card_back);

                }
            }
        }

//        Log.d("MSMS","MSMS" + "GameResultFragment" + resultList.get(0));
//        Map<String, Object> listmap = (Map<String, Object>) resultList.get(0);
//        String userName = (String)listmap.get("name");
//        Log.d("MSMS","MSMS"+userName);
//        Log.d("MSMS","MSMS"+  apiBody.getUsers().get(0).getCards().first);

        //play_result_content1_card1.setImageResource(firstCard[apiBody.getUsers().get(0).getCards().first]);
        //play_result_content1_card2.setImageResource(secondCard[apiBody.getUsers().get(0).getCards().second]);


        // 결과보기 - 화살표(왼쪽, 오른쪽) 눌렀을 경우
        play_result_left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kyj", "left button");

                if (play_result_content1.getVisibility() == View.VISIBLE) {
                    // result_1
                    play_result_content1.setVisibility(View.GONE);
                    play_result_rank1_name.setVisibility(View.GONE);
                    play_result_content1_card1.setVisibility(View.GONE);
                    play_result_content1_card2.setVisibility(View.GONE);

                    // result_2
                    play_result_content2.setVisibility(View.VISIBLE);
                    play_result_rank2_name.setVisibility(View.VISIBLE);
                    play_result_rank2_card1.setVisibility(View.VISIBLE);
                    play_result_rank2_card2.setVisibility(View.VISIBLE);
                    play_result_rank3_name.setVisibility(View.VISIBLE);
                    play_result_rank3_card1.setVisibility(View.VISIBLE);
                    play_result_rank3_card2.setVisibility(View.VISIBLE);
                    play_result_rank4_name.setVisibility(View.VISIBLE);
                    play_result_rank4_card1.setVisibility(View.VISIBLE);
                    play_result_rank4_card2.setVisibility(View.VISIBLE);
                    play_result_rank2_die.setVisibility(View.VISIBLE);
                    play_result_rank3_die.setVisibility(View.VISIBLE);
                    play_result_rank4_die.setVisibility(View.VISIBLE);
                } else {
                    // result_1
                    play_result_content1.setVisibility(View.VISIBLE);
                    play_result_rank1_name.setVisibility(View.VISIBLE);
                    play_result_content1_card1.setVisibility(View.VISIBLE);
                    play_result_content1_card2.setVisibility(View.VISIBLE);

                    // result_2
                    play_result_content2.setVisibility(View.GONE);
                    play_result_rank2_name.setVisibility(View.GONE);
                    play_result_rank2_card1.setVisibility(View.GONE);
                    play_result_rank2_card2.setVisibility(View.GONE);
                    play_result_rank3_name.setVisibility(View.GONE);
                    play_result_rank3_card1.setVisibility(View.GONE);
                    play_result_rank3_card2.setVisibility(View.GONE);
                    play_result_rank4_name.setVisibility(View.GONE);
                    play_result_rank4_card1.setVisibility(View.GONE);
                    play_result_rank4_card2.setVisibility(View.GONE);
                    play_result_rank2_die.setVisibility(View.GONE);
                    play_result_rank3_die.setVisibility(View.GONE);
                    play_result_rank4_die.setVisibility(View.GONE);
                }
            }
        });

        play_result_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kyj", "right button");

                if (play_result_content1.getVisibility() == View.VISIBLE) {
                    // result_1
                    play_result_content1.setVisibility(View.GONE);
                    play_result_rank1_name.setVisibility(View.GONE);
                    play_result_content1_card1.setVisibility(View.GONE);
                    play_result_content1_card2.setVisibility(View.GONE);

                    // result_2
                    play_result_content2.setVisibility(View.VISIBLE);
                    play_result_rank2_name.setVisibility(View.VISIBLE);
                    play_result_rank2_card1.setVisibility(View.VISIBLE);
                    play_result_rank2_card2.setVisibility(View.VISIBLE);
                    play_result_rank3_name.setVisibility(View.VISIBLE);
                    play_result_rank3_card1.setVisibility(View.VISIBLE);
                    play_result_rank3_card2.setVisibility(View.VISIBLE);
                    play_result_rank4_name.setVisibility(View.VISIBLE);
                    play_result_rank4_card1.setVisibility(View.VISIBLE);
                    play_result_rank4_card2.setVisibility(View.VISIBLE);
                    play_result_rank2_die.setVisibility(View.VISIBLE);
                    play_result_rank3_die.setVisibility(View.VISIBLE);
                    play_result_rank4_die.setVisibility(View.VISIBLE);
                } else {
                    // result_1
                    play_result_content1.setVisibility(View.VISIBLE);
                    play_result_rank1_name.setVisibility(View.VISIBLE);
                    play_result_content1_card1.setVisibility(View.VISIBLE);
                    play_result_content1_card2.setVisibility(View.VISIBLE);

                    // result_2
                    play_result_content2.setVisibility(View.GONE);
                    play_result_rank2_name.setVisibility(View.GONE);
                    play_result_rank2_card1.setVisibility(View.GONE);
                    play_result_rank2_card2.setVisibility(View.GONE);
                    play_result_rank3_name.setVisibility(View.GONE);
                    play_result_rank3_card1.setVisibility(View.GONE);
                    play_result_rank3_card2.setVisibility(View.GONE);
                    play_result_rank4_name.setVisibility(View.GONE);
                    play_result_rank4_card1.setVisibility(View.GONE);
                    play_result_rank4_card2.setVisibility(View.GONE);
                    play_result_rank2_die.setVisibility(View.GONE);
                    play_result_rank3_die.setVisibility(View.GONE);
                    play_result_rank4_die.setVisibility(View.GONE);
                }
            }
        });

        ImageView play_result_ReButton = (ImageView) rootview.findViewById(R.id.play_result_ReButton);

        play_result_ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playActivity.Game_Result_Close();

//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(getTargetFragment()).commit();

            }
        });

        return rootview;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        playActivity = (PlayActivity) getActivity();

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.play_result_ReButton)
    public void Game_Result_ReButton() {
        Log.d("kyj", "exit fragment");


//        fragmentTransaction.remove(this).commit();

//        GameResultFragment fragment = (GameResultFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_play_result);
//        fragment = new GameResultFragment();
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.hide(fragment);
//        ft.commit();
    }
}
