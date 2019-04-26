package com.fundroid.offstand.ui.lobby.main;

import android.os.Bundle;
import android.view.View;

import com.fundroid.offstand.BR;
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
