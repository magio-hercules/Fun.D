package com.fund.iam.ui.main.channel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;

import com.fund.iam.data.model.Channel;
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
        getViewDataBinding().tvCreateDate.setText("채널 생성일: "+getViewModel().channel.get(0).createDate.substring(0, 10));


        // 채널주인용 코드
        if(getViewModel().channel.get(0).ownerId== dataManager.getMyInfo().getId()) {

            getViewDataBinding().btMulti.setText("채널 수정하기");
            getViewDataBinding().btMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 채널 수정페이지로 넘어가기..
                }
            });
        }   else {
            getViewModel().getChannelUserInfo(getViewModel().channel.get(0).id);
        }
    }

    @Override
    public void getUsersInfo() {

        getViewModel().isJoin = 0;

        // 게스트용 코드: 가입여부 체크
        for(int i=0; i< getViewModel().channelUsers.size();i++){
            if (getViewModel().channelUsers.get(i).userId == dataManager.getMyInfo().getId()) {
                getViewModel().isJoin = 1;
            }
        }
        if (getViewModel().isJoin == 0) {
            getViewDataBinding().btMulti.setText("채널 가입하기");
            getViewDataBinding().btMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 채널가입 코드
                    getViewModel().setChannelUserInsert(getViewModel().channelId,dataManager.getMyInfo().getId());
                }
            });
        } else {
            getViewDataBinding().btMulti.setText("채널 탈퇴하기");
            getViewDataBinding().btMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 채널 탈퇴 코드
                    getViewModel().setChannelUserDelete(getViewModel().channelId,dataManager.getMyInfo().getId());
                }
            });
        }
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
        getViewModel().channelId = ChannelFragmentArgs.fromBundle(getArguments()).getChannelIdArg();
        getViewModel().getChannelInfo(getViewModel().channelId);

        getViewDataBinding().btChanelUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChannelFragmentDirections.ActionChannelUserList action = ChannelFragmentDirections.actionChannelUserList();
                action.setChannelIdArg(getViewModel().channelId);
                Navigation.findNavController(v).navigate(action);

            }
        });

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
