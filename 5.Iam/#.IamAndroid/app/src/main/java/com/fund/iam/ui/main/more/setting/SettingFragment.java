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
import com.fund.iam.databinding.FragmentMoreBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.fund.iam.ui.main.more.MoreAdapter;
import com.fund.iam.ui.main.more.MoreNavigator;
import com.fund.iam.ui.main.more.MoreViewModel;
import com.fund.iam.utils.RecyclerDecoration;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;


public class SettingFragment extends BaseFragment<FragmentMoreBinding, MoreViewModel> implements MoreNavigator, View.OnClickListener {

    public static final String TAG = SettingFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    MoreAdapter moreAdapter;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public MoreViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(MoreViewModel.class);
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
        initViews();
    }

    private void initViews() {
        getViewDataBinding().mores.setLayoutManager(new LinearLayoutManager(getActivity()));
        getViewDataBinding().mores.addItemDecoration(new RecyclerDecoration(ContextCompat.getDrawable(getBaseActivity(), R.drawable.divider_gray), /*ViewUtils.DpToPixel(getBaseActivity(), 16)*/0, 0));
        getViewDataBinding().mores.setAdapter(moreAdapter);
        moreAdapter.clearItems();
        moreAdapter.addItem(getResources().getString(R.string.title_notice));
        moreAdapter.addItem(getResources().getString(R.string.title_setting));
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
