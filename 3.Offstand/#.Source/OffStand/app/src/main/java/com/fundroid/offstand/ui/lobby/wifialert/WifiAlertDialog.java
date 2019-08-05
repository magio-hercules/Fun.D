package com.fundroid.offstand.ui.lobby.wifialert;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.DialogWifiAlertBinding;
import com.fundroid.offstand.ui.base.BaseDialog;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class WifiAlertDialog extends BaseDialog implements WifiAlertNavigator {

    private static final String TAG = WifiAlertDialog.class.getSimpleName();
    @Inject
    ViewModelProviderFactory factory;
    private WifiAlertViewModel mWifiAlertViewModel;

    public static WifiAlertDialog newInstance() {
        WifiAlertDialog fragment = new WifiAlertDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void goToWifiSetting() {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    @Override
    public void dismissDialog() {
        dismissDialog(TAG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogWifiAlertBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_wifi_alert, container, false);
        View view = binding.getRoot();

        AndroidSupportInjection.inject(this);
        mWifiAlertViewModel = ViewModelProviders.of(this,factory).get(WifiAlertViewModel.class);
        binding.setViewModel(mWifiAlertViewModel);

        mWifiAlertViewModel.setNavigator(this);

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }


    @Override
    public void handleError(Throwable throwable) {

    }
}
