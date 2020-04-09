package com.fund.iam.ui.main.channel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;

import com.fund.iam.databinding.FragmentChannelBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class ChannelFragment extends BaseFragment<FragmentChannelBinding, ChannelViewModel> implements ChannelNavigator, View.OnClickListener {

    public static final String TAG = ChannelFragment.class.getSimpleName();


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    public static ChannelFragment newInstance() {
        Bundle args = new Bundle();
        ChannelFragment fragment = new ChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_channel;
    }

    @Override
    public ChannelViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(ChannelViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void getChannelInfo() {

        getViewDataBinding().tvName.setText(getViewModel().channel.get(0).name);
        getViewDataBinding().tvLocation.setText(getViewModel().channel.get(0).location);
        getViewDataBinding().tvDescription.setText(getViewModel().channel.get(0).description);
        getViewDataBinding().tvCreateDate.setText(getViewModel().channel.get(0).createDate);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("CreateFragment:onCreate");
         getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewDataBinding().setViewModel(getViewModel());

        initViews(view);
    }

    private void initViews(View view) {
        int id = ChannelFragmentArgs.fromBundle(getArguments()).getChannelIdArg();
        getViewModel().getChannelInfo(id);
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
