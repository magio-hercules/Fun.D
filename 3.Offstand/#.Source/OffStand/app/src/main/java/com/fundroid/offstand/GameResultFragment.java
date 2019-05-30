package com.fundroid.offstand;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.OnClick;


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
    ImageView play_result_content1_card1;
    ImageView play_result_content1_card2;
    ImageView play_result_content2;
    ImageView play_result_rank2_card1;
    ImageView play_result_rank2_card2;
    ImageView play_result_rank3_card1;
    ImageView play_result_rank3_card2;
    ImageView play_result_rank4_card1;
    ImageView play_result_rank4_card2;
    ImageView play_result_left_button;
    ImageView play_result_right_button;
    ImageView play_result_shadow;

    @BindView(R.id.play_result_ReButton)
    ImageView play_result_ReButton;

    PlayActivity playActivity;


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

        Log.d("MSMS", "1");
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
        Log.d("MSMS", "2");
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
        Log.d("MSMS", "3");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MSMS", "4");

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_gameresult,container,false);

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

        // 만땅 - 기본 셋팅
        play_result_title1.setVisibility(rootview.VISIBLE);
        play_result_content1.setVisibility(rootview.VISIBLE);
        play_result_content1_card1.setVisibility(rootview.VISIBLE);
        play_result_content1_card2.setVisibility(rootview.VISIBLE);
        play_result_content2.setVisibility(rootview.GONE);
        play_result_rank2_card1.setVisibility(rootview.GONE);
        play_result_rank2_card2.setVisibility(rootview.GONE);
        play_result_rank3_card1.setVisibility(rootview.GONE);
        play_result_rank3_card2.setVisibility(rootview.GONE);
        play_result_rank4_card1.setVisibility(rootview.GONE);
        play_result_rank4_card2.setVisibility(rootview.GONE);
        play_result_left_button.setVisibility(rootview.VISIBLE);
        play_result_right_button.setVisibility(rootview.VISIBLE);
        play_result_shadow.setVisibility(rootview.VISIBLE);

        // 결과보기 - 화살표(왼쪽, 오른쪽) 눌렀을 경우
        play_result_left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kyj", "left button");

                if(play_result_content1.getVisibility() == View.VISIBLE){
                    // result_1
                    play_result_content1.setVisibility(View.GONE);
                    play_result_content1_card1.setVisibility(View.GONE);
                    play_result_content1_card2.setVisibility(View.GONE);

                    // result_2
                    play_result_content2.setVisibility(View.VISIBLE);
                    play_result_rank2_card1.setVisibility(View.VISIBLE);
                    play_result_rank2_card2.setVisibility(View.VISIBLE);
                    play_result_rank3_card1.setVisibility(View.VISIBLE);
                    play_result_rank3_card2.setVisibility(View.VISIBLE);
                    play_result_rank4_card1.setVisibility(View.VISIBLE);
                    play_result_rank4_card2.setVisibility(View.VISIBLE);
                }
                else{
                    // result_1
                    play_result_content1.setVisibility(View.VISIBLE);
                    play_result_content1_card1.setVisibility(View.VISIBLE);
                    play_result_content1_card2.setVisibility(View.VISIBLE);

                    // result_2
                    play_result_content2.setVisibility(View.GONE);
                    play_result_rank2_card1.setVisibility(View.GONE);
                    play_result_rank2_card2.setVisibility(View.GONE);
                    play_result_rank3_card1.setVisibility(View.GONE);
                    play_result_rank3_card2.setVisibility(View.GONE);
                    play_result_rank4_card1.setVisibility(View.GONE);
                    play_result_rank4_card2.setVisibility(View.GONE);
                }
            }
        });

        play_result_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kyj", "right button");

                if(play_result_content1.getVisibility() == View.VISIBLE){
                    // result_1
                    play_result_content1.setVisibility(View.GONE);
                    play_result_content1_card1.setVisibility(View.GONE);
                    play_result_content1_card2.setVisibility(View.GONE);

                    // result_2
                    play_result_content2.setVisibility(View.VISIBLE);
                    play_result_rank2_card1.setVisibility(View.VISIBLE);
                    play_result_rank2_card2.setVisibility(View.VISIBLE);
                    play_result_rank3_card1.setVisibility(View.VISIBLE);
                    play_result_rank3_card2.setVisibility(View.VISIBLE);
                    play_result_rank4_card1.setVisibility(View.VISIBLE);
                    play_result_rank4_card2.setVisibility(View.VISIBLE);
                }
                else{
                    // result_1
                    play_result_content1.setVisibility(View.VISIBLE);
                    play_result_content1_card1.setVisibility(View.VISIBLE);
                    play_result_content1_card2.setVisibility(View.VISIBLE);

                    // result_2
                    play_result_content2.setVisibility(View.GONE);
                    play_result_rank2_card1.setVisibility(View.GONE);
                    play_result_rank2_card2.setVisibility(View.GONE);
                    play_result_rank3_card1.setVisibility(View.GONE);
                    play_result_rank3_card2.setVisibility(View.GONE);
                    play_result_rank4_card1.setVisibility(View.GONE);
                    play_result_rank4_card2.setVisibility(View.GONE);
                }
            }
        });

        ImageView play_result_ReButton = (ImageView) rootview.findViewById(R.id.play_result_ReButton);

        play_result_ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MSMS", "5");

                playActivity.Game_Reslut_Close();

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
        Log.d("MSMS", "5");
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
        Log.d("MSMS", "6");
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
