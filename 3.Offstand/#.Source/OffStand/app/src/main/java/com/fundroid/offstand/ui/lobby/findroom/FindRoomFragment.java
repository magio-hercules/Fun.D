package com.fundroid.offstand.ui.lobby.findroom;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.lobby.wifialert.WifiAlertDialog;
import com.fundroid.offstand.ui.room.RoomActivity;
import com.fundroid.offstand.databinding.FragmentFindRoomBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class FindRoomFragment extends BaseFragment<FragmentFindRoomBinding, FindRoomViewModel> implements FindRoomNavigator {

    public static final String TAG = FindRoomFragment.class.getSimpleName();

    @Inject
    LinearLayoutManager linearLayoutManager;

    @Inject
    RoomAdapter roomAdapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    public static FindRoomFragment newInstance() {
        Bundle args = new Bundle();
        FindRoomFragment fragment = new FindRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find_room;
    }

    @Override
    public FindRoomViewModel getViewModel() {
        return ViewModelProviders.of(this, viewModelProviderFactory).get(FindRoomViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewModel().syncRooms();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        getViewDataBinding().rvRoom.setLayoutManager(linearLayoutManager);
        getViewDataBinding().rvRoom.setItemAnimator(new DefaultItemAnimator());
        getViewDataBinding().rvRoom.setAdapter(roomAdapter);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.raw.gif_loading);
            getViewDataBinding().loading.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void goToRoomActivity() {
        RoomActivity.start(getContext());
        getBaseActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }

    @Override
    public void showProgress() {
        getViewDataBinding().loading.bringToFront();
        getViewDataBinding().loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgress() {
        getViewDataBinding().loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showWifiAlertDialog() {
        WifiAlertDialog.newInstance().show(getFragmentManager());
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
