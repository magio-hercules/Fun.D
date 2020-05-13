package com.fund.iam.ui.main.bookmark;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.databinding.FragmentBookmarkBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;


public class BookmarkFragment extends BaseFragment<FragmentBookmarkBinding, BookmarkViewModel> implements BookmarkNavigator, View.OnClickListener {

    public static final String TAG = BookmarkFragment.class.getSimpleName();

    private BookmarkListChannelAdapter adapter_channels;
    private BookmarkListUserAdapter adapter_users;
    int TabState = 1;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    public static BookmarkFragment newInstance() {
        Bundle args = new Bundle();
        BookmarkFragment fragment = new BookmarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookmark;
    }

    @Override
    public BookmarkViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(BookmarkViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void updateChannels() {
        adapter_channels.setModel_Channels(getViewModel().channels);
    }

    @Override
    public void updateUsers() {
        Logger.d("jobs Size" + getViewModel().jobs.size());
        adapter_users.setModel_Users(getViewModel().users, getViewModel().jobs);
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

        getViewDataBinding().setViewModel(getViewModel());

        initViews(view);
    }

    private void initViews(View view) {

        setChannelData();

        getViewDataBinding().btChanelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChannelData();
            }
        });

        getViewDataBinding().btUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserData();
            }
        });


        getViewDataBinding().recyclerViewChannels.setHasFixedSize(true);
        getViewDataBinding().recyclerViewChannels.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_channels = new BookmarkListChannelAdapter();
        getViewDataBinding().recyclerViewChannels.setAdapter(adapter_channels);

        getViewDataBinding().recyclerViewUsers.setHasFixedSize(true);
        getViewDataBinding().recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_users = new BookmarkListUserAdapter();
        getViewDataBinding().recyclerViewUsers.setAdapter(adapter_users);

        getViewModel().getJobsInfo();
        getViewModel().getBookmarkChannelsInfo(1);
        getViewModel().getBookmarkUsersInfo(1);
    }

    public void setChannelData() {
        getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
        getViewDataBinding().btChanelList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        getViewDataBinding().recyclerViewUsers.setVisibility(View.GONE);
        getViewDataBinding().recyclerViewChannels.setVisibility(View.VISIBLE);
        getViewDataBinding().tvUserTitle.setVisibility(View.GONE);
        getViewDataBinding().tvChannelTitle.setVisibility(View.VISIBLE);

        TabState = 1;
    }

    public void setUserData() {
        getViewDataBinding().btChanelList.setCardBackgroundColor(Color.parseColor("#7E57C2"));
        getViewDataBinding().btUserList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        getViewDataBinding().recyclerViewChannels.setVisibility(View.GONE);
        getViewDataBinding().recyclerViewUsers.setVisibility(View.VISIBLE);
        getViewDataBinding().tvUserTitle.setVisibility(View.VISIBLE);
        getViewDataBinding().tvChannelTitle.setVisibility(View.GONE);

        TabState = 2;
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
        Logger.e("BookmarkFragment:handleError " + throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
