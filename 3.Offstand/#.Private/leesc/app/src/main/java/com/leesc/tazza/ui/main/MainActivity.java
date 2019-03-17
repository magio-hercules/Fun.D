package com.leesc.tazza.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.data.model.Room;
import com.leesc.tazza.databinding.ActivityMainBinding;
import com.leesc.tazza.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    MainViewModel mainViewModel;

    private ActivityMainBinding activityMainBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = getViewDataBinding();
        mainViewModel.setNavigator(this);
        setupRecyclerView(activityMainBinding.rvWifi, new RoomAdapter());
        setupRecyclerView(activityMainBinding.rvAttendee, new AttendeeAdapter());
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRepositoriesChanged(List<Room> rooms) {
        Log.d("lsc","LobbyActivity onRepositoriesChanged rooms " + rooms.size());
        RoomAdapter adapter = (RoomAdapter) activityMainBinding.rvWifi.getAdapter();
        adapter.setDatas(rooms);
    }

    @Override
    public void handleError(Throwable throwable) {

    }

}