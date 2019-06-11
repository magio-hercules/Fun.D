package com.fundroid.offstand.ui.lobby.main;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

import pl.droidsonroids.gif.GifDrawable;

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> implements MainNavigator {

    public static final String TAG = MainFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private MainViewModel mainViewModel;

    private FragmentMainBinding fragmentMainBinding;

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
        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        return mainViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMainBinding = getViewDataBinding();
        initViews();
    }

    private void initViews() {
        String videoRootPath = "android.resource://" + getContext().getPackageName() + "/";
        fragmentMainBinding.bgMain.setVideoURI(Uri.parse(videoRootPath + R.raw.mp4_lobby));
        fragmentMainBinding.bgMain.start();
        fragmentMainBinding.bgMain.setOnPreparedListener(mp -> mp.setLooping(true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "MainFragment onDestroy");
    }

    @Override
    public void makeRoom() {
//        MediaPlayer.create(getActivity().getApplicationContext(), R.raw.mouth_interface_button).start();

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
        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, FindRoomFragment.newInstance(), FindRoomFragment.TAG)
                .commit();
    }

    @Override
    public void guide() {
        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .add(R.id.fragment_container, GuideFragment.newInstance(), GuideFragment.TAG)
                .commit();
    }
}
