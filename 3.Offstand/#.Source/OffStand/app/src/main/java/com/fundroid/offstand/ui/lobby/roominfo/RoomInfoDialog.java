/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.fundroid.offstand.ui.lobby.roominfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.DialogRoomInfoBinding;
import com.fundroid.offstand.ui.base.BaseDialog;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;

public class RoomInfoDialog extends BaseDialog implements RoomInfoNavigator {

    private static final String TAG = RoomInfoDialog.class.getSimpleName();

    @Inject
    ViewModelProviderFactory factory;
    private RoomInfoViewModel roomInfoViewModel;

    public static RoomInfoDialog newInstance() {
        RoomInfoDialog fragment = new RoomInfoDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void dismissDialog() {
        dismissDialog(TAG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogRoomInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_room_info, container, false);
        View view = binding.getRoot();

        AndroidSupportInjection.inject(this);
        roomInfoViewModel = ViewModelProviders.of(this,factory).get(RoomInfoViewModel.class);
        binding.setViewModel(roomInfoViewModel);

        roomInfoViewModel.setNavigator(this);

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void handleError(Throwable throwable) {

    }
}
