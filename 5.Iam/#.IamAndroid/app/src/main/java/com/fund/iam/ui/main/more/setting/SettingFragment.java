package com.fund.iam.ui.main.more.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.databinding.FragmentNoticeBinding;
import com.fund.iam.databinding.FragmentSettingBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.fund.iam.utils.RecyclerDecoration;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM;


public class SettingFragment extends BaseFragment<FragmentSettingBinding, SettingViewModel> implements SettingNavigator, View.OnClickListener {

    public static final String TAG = SettingFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public SettingViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(SettingViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionBar();
        initViews();
    }

    private void setupActionBar() {
        getBaseActivity().setSupportActionBar(getViewDataBinding().toolbar);
        getBaseActivity().getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {

    }

    @Override
    public void onClick(View view) {
        goBack();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Logger.e("handleError " + throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
