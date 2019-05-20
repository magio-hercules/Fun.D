package com.fundroid.offstand.ui.lobby.main;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.PlayActivity;
import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.FragmentMainBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomFragment;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import pl.droidsonroids.gif.GifDrawable;

import static com.fundroid.offstand.core.AppConstant.FRAGMENT_FIND_ROOM;
import static com.fundroid.offstand.core.AppConstant.FRAGMENT_MAKE_ROOM;

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
        ((GifDrawable) fragmentMainBinding.bgMain.getDrawable()).setLoopCount(0);
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

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sp.play(soundID, 1f, 1f, 0, 0,1f);
            }
        });
        // [만땅] SoundPool Test - End

        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(FRAGMENT_MAKE_ROOM)
                .add(R.id.fragment_container, MakeRoomFragment.newInstance(), MakeRoomFragment.TAG)
                .commit();
    }

    @Override
    public void findRoom() {
        getBaseActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(FRAGMENT_FIND_ROOM)
                .add(R.id.fragment_container, FindRoomFragment.newInstance(), FindRoomFragment.TAG)
                .commit();
    }

    @Override
    public void guide() {

    }
}
