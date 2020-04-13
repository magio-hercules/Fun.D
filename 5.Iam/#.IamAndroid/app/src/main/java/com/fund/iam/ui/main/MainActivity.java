package com.fund.iam.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.databinding.ActivityMainBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseActivity;
import com.fund.iam.ui.letter.LetterActivity;
import com.fund.iam.ui.main.more.notice.NoticeFragment;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator, HasAndroidInjector, NavController.OnDestinationChangedListener {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    private NavController mNavController;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

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
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(MainViewModel.class);
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mNavController = Navigation.findNavController(this, R.id.fragment_container);
//        NavigationUI.setupWithNavController(getViewDataBinding().toolbar, mNavController);
        NavigationUI.setupWithNavController(getViewDataBinding().navigationBottom, mNavController);
//        mNavController.addOnDestinationChangedListener(this);
    }

    public void actionNavigate(View view) {
        Logger.d("actionNavigate " + view.getId());
        switch (view.getId()) {
            case R.id.profile_image_letter:
                mNavController.navigate(R.id.navigation_letterbox);
                break;
            case R.id.profile_image_modify:
                mNavController.navigate(R.id.navigation_home_edit);
                break;
            case R.id.profile_edit_cancel:
                mNavController.navigate(R.id.navigation_home);
                break;

            case R.id.channel_create:
                mNavController.navigate(R.id.navigation_create_channel);
                break;
        }
    }

    @Override
    public void actionNavigate(@IdRes int resourceId) {
        mNavController.navigate(resourceId);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//        switch (destination.getId()) {
//            case R.id.navigation_home:
//            case R.id.navigation_more:
//                getViewModel().toolbarVisible.set(false);
//                break;
//
//            case R.id.navigation_bookmark:
//            case R.id.navigation_search:
//            case R.id.navigation_letterbox:
//            case R.id.navigation_notice:
//            case R.id.navigation_setting:
//                getViewModel().toolbarVisible.set(true);
//                setAppBarTitle(destination.getId());
//                break;
//
//        }
    }

//    private void setAppBarTitle(@IdRes int destinationId) {
//        switch (destinationId) {
//            case R.id.navigation_notice:
//                getViewDataBinding().toolbar.setTitle(getString(R.string.title_notice));
//                break;
//        }
//    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Logger.e("handleError " + throwable.getMessage());
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
