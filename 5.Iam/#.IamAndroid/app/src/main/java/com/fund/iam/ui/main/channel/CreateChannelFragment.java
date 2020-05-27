package com.fund.iam.ui.main.channel;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.fund.iam.utils.RealPathUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class CreateChannelFragment extends BaseFragment<FragmentCreateChannelBinding, CreateChannelViewModel> implements CreateChannelNavigator, View.OnClickListener {

    public static final String TAG = CreateChannelFragment.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    private static final int REQUEST_IMAGE_CROP_AFTER = 5555;
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI;

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
    public void createChannel(View view) {

        // TODO 서버에서 채널 id 받아서 채널 페이지로 전달하기 현재는 임시 코드
        Logger.i("CreateChannelFragment:channel_id"+getViewModel().channels.get(0).id);
        CreateChannelFragmentDirections.ActionMainChannel action = CreateChannelFragmentDirections.actionMainChannel();
        action.setChannelIdArg(getViewModel().channels.get(0).id);
        Navigation.findNavController(view).navigate(action);
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


        // 채널 이미지 생성 로직
        getViewDataBinding().ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getViewDataBinding().btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(getViewDataBinding().etName.getWindowToken(),0);
                imm.hideSoftInputFromWindow(getViewDataBinding().etPurpose.getWindowToken(),0);
                imm.hideSoftInputFromWindow(getViewDataBinding().etLocation.getWindowToken(),0);
                imm.hideSoftInputFromWindow(getViewDataBinding().etDescription.getWindowToken(),0);
                imm.hideSoftInputFromWindow(getViewDataBinding().etPassword.getWindowToken(),0);
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
                channel.description = getViewDataBinding().etDescriptionInput.getText().toString();
                channel.password = getViewDataBinding().etPasswordInput.getText().toString();
                getViewModel().getNewChannelInfo(view,dataManager.getMyInfo().getId(),channel.name,channel.purpose,channel.location,channel.description,channel.password);
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
