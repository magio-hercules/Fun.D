package com.fund.iam.ui.main.channel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.databinding.FragmentChannelUserListBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class ChannelUserListFragment extends BaseFragment<FragmentChannelUserListBinding, ChannelUserListViewModel> implements ChannelUserListNavigator, View.OnClickListener {

    public static final String TAG = ChannelUserListFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    public static ChannelUserListFragment newInstance() {
        Bundle args = new Bundle();
        ChannelUserListFragment fragment = new ChannelUserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_channel_user_list;
    }

    @Override
    public ChannelUserListViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(ChannelUserListViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("CreateUserListFragment:onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initViews(view);
    }

    private void initViews(View view) {

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
