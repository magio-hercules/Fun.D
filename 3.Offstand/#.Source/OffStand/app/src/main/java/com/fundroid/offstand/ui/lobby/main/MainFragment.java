package com.fundroid.offstand.ui.lobby.main;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.FragmentMainBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomFragment;
import com.fundroid.offstand.ui.lobby.guide.GuideFragment;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> implements MainNavigator {

    public static final String TAG = MainFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        String videoRootPath = "android.resource://" + getContext().getPackageName() + "/";
        getViewDataBinding().bgMain.setVideoURI(Uri.parse(videoRootPath + R.raw.mp4_lobby));

        getViewDataBinding().btnMakeRoom.setImageResource(R.drawable.btn_make_room);
        getViewDataBinding().btnFindRoom.setImageResource(R.drawable.btn_find_room);
        getViewDataBinding().btnGuide.setImageResource(R.drawable.btn_guide);
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewDataBinding().bgMain.start();
        getViewDataBinding().bgMain.setOnPreparedListener(mp -> mp.setLooping(true));
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewDataBinding().bgMain.stopPlayback();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "MainFragment onDestroy");
    }

    @Override
    public void makeRoom() {
        getViewDataBinding().btnMakeRoom.setPressed(false);

        // [만땅] SoundPool Test - Start
        // 한참을 연구해봤다..그래서 디버깅을 걸어보았는데...이상하게 중단점 지정하고 디버깅하면 소리가 난다 ㅋㅋㅋㅋㅋ뭐지?
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        SoundPool sp = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        int soundID = sp.load(getActivity().getApplicationContext(), R.raw.mouth_interface_button, 1);
//        sp.play(soundID, 1f, 1f, 0, 0,1f);

        sp.setOnLoadCompleteListener((soundPool, sampleId, status) -> sp.play(soundID, 1f, 1f, 0, 0, 1f));
        // [만땅] SoundPool Test - End
        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, MakeRoomFragment.newInstance(), MakeRoomFragment.TAG)
                .commit();
    }

    @Override
    public void findRoom() {
        getViewDataBinding().btnFindRoom.setPressed(false);

        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, FindRoomFragment.newInstance(), FindRoomFragment.TAG)
                .commit();
    }

    @Override
    public void guide() {
        getViewDataBinding().btnGuide.setPressed(false);
        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, GuideFragment.newInstance(), GuideFragment.TAG)
                .commit();
    }
}
