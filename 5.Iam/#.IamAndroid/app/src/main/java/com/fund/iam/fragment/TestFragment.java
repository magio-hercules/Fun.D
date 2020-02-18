package com.fund.iam.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.fund.iam.HomeActivity;
import com.fund.iam.MainActivity;
import com.fund.iam.R;
import com.fund.iam.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
    private static final String TAG = "[IAM][TEST]";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R2.id.test_login)
    Button testLogin;
    @BindView(R2.id.test_home)
    Button testHome;
    @BindView(R2.id.test_letter)
    Button testLetter;
    @BindView(R2.id.test_bookmark)
    Button testBookmark;
    @BindView(R2.id.test_search)
    Button testSearch;
    @BindView(R2.id.test_setting)
    Button testSetting;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String fragmentName) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragmentName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R2.id.test_login, R2.id.test_home, R2.id.test_home_act, R2.id.test_letter, R2.id.test_bookmark, R2.id.test_search, R2.id.test_setting})
    public void onViewClicked(View view) {
        Log.d(TAG,"onViewClicked");

        switch (view.getId()) {
            case R.id.test_login:
                Log.d(TAG,"로그인 화면 연결 필요");
                onButtonPressed("login");
                break;
            case R.id.test_home:
                Log.d(TAG,"홈 프레그먼트");
                onButtonPressed("home");
                break;
            case R.id.test_home_act:
                Log.d(TAG,"홈 액티비티");
                Intent a = new Intent(getActivity(), HomeActivity.class);
                startActivity(a);
                break;
            case R.id.test_letter:
                Log.d(TAG,"쪽지함 화면 연결 필요");
                onButtonPressed("letter");
                break;
            case R.id.test_bookmark:
                Log.d(TAG,"내저장 화면 연결 필요");
                onButtonPressed("boomark");
                break;
            case R.id.test_search:
                Log.d(TAG,"검색 화면 연결 필요");
                onButtonPressed("search");
                break;
            case R.id.test_setting:
                Log.d(TAG,"세팅 화면 연결 필요");
                onButtonPressed("setting");
                break;
        }
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
        void onFragmentInteraction(String fragmentName);
    }
}
