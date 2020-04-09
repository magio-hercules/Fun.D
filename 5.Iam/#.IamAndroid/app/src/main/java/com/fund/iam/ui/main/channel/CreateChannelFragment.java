package com.fund.iam.ui.main.channel;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Channel;
import com.fund.iam.databinding.FragmentCreateChannelBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class CreateChannelFragment extends BaseFragment<FragmentCreateChannelBinding, CreateChannelViewModel> implements CreateChannelNavigator, View.OnClickListener {

    public static final String TAG = CreateChannelFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    public static CreateChannelFragment newInstance() {
        Bundle args = new Bundle();
        CreateChannelFragment fragment = new CreateChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_channel;
    }

    @Override
    public CreateChannelViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(CreateChannelViewModel.class);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void createChannel() {
        // TODO 응답 받아서 새 페이지로 넘어 갈 수 있게 설정
//        showToast(getViewModel().channel.id+"");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("CreateChannelFragment:onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;

        getViewDataBinding().btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        getViewDataBinding().etNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getViewDataBinding().switchPassword.isChecked()) {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")&&!getViewDataBinding().etPasswordInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }

                } else {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().etPurposeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getViewDataBinding().switchPassword.isChecked()) {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")&&!getViewDataBinding().etPasswordInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }

                } else {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().etLocationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getViewDataBinding().switchPassword.isChecked()) {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")&&!getViewDataBinding().etPasswordInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }

                } else {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().etDescriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getViewDataBinding().switchPassword.isChecked()) {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")&&!getViewDataBinding().etPasswordInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }

                } else {
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    } else {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                        getViewDataBinding().btCreateChannel.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().cvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewDataBinding().etDescriptionInput.requestFocus();
                imm.showSoftInput(getViewDataBinding().etDescriptionInput,0);
                getViewDataBinding().etDescriptionInput.setSelection(getViewDataBinding().etDescriptionInput.getText().length());
            }
        });

        getViewDataBinding().switchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getViewDataBinding().cvPassword.setVisibility(View.VISIBLE);
                    getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                    getViewDataBinding().btCreateChannel.setEnabled(false);
                } else {
                    getViewDataBinding().cvPassword.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(getViewDataBinding().etPasswordInput.getWindowToken(),0);
                    getViewDataBinding().etPasswordInput.setText("");
                    if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                        getViewDataBinding().btCreateChannel.setEnabled(true);
                    }
                }
            }
        });

        getViewDataBinding().etPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!getViewDataBinding().etNameInput.getText().toString().equals("") &&!getViewDataBinding().etPurposeInput.getText().toString().equals("")&&!getViewDataBinding().etLocationInput.getText().toString().equals("")&&!getViewDataBinding().etDescriptionInput.getText().toString().equals("")&&!getViewDataBinding().etPasswordInput.getText().toString().equals("")) {
                        getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#7E57C2"));
                    getViewDataBinding().btCreateChannel.setEnabled(true);
                } else {
                    getViewDataBinding().btCreateChannel.setBackgroundColor(Color.parseColor("#D1D1D1"));
                    getViewDataBinding().btCreateChannel.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().btCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel channel = new Channel();
                channel.name = getViewDataBinding().etNameInput.getText().toString();
                channel.purpose = getViewDataBinding().etPurposeInput.getText().toString();
                channel.location = getViewDataBinding().etLocationInput.getText().toString();
                channel.description = getViewDataBinding().etLocationInput.getText().toString();
                channel.password = getViewDataBinding().etPasswordInput.getText().toString();
                // TODO ownerID를 받아서 보내야함.
                getViewModel().getNewChannelInfo(1,channel.name,channel.purpose,channel.location,channel.description,channel.password);
            }
        });
    }

    @Override
    public void onClick(View view) {
        goBack();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); }


    @Override
    public void handleError(Throwable throwable) {
        Logger.e("CreateChannelFragment:handleError " + throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }

}
