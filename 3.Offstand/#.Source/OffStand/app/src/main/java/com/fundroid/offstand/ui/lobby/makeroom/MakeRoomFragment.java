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

package com.fundroid.offstand.ui.lobby.makeroom;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.room.RoomActivity;
import com.fundroid.offstand.databinding.FragmentMakeRoomBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class MakeRoomFragment extends BaseFragment<FragmentMakeRoomBinding, MakeRoomViewModel> implements MakeRoomNavigator {

    public static final String TAG = MakeRoomFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private MakeRoomViewModel makeRoomViewModel;

    public static MakeRoomFragment newInstance() {
        Bundle args = new Bundle();
        MakeRoomFragment fragment = new MakeRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_make_room;
    }

    @Override
    public MakeRoomViewModel getViewModel() {
        makeRoomViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MakeRoomViewModel.class);
        return makeRoomViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeRoomViewModel.setNavigator(this);
    }

    @Override
    public void goToRoomActivity() {
        RoomActivity.start(getContext());
        getBaseActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
        getBaseActivity().finish();
    }

    @Override
    public void handleError(Throwable throwable) {
        Log.e("lsc", "MakeRoomFragment handleError " + throwable.getMessage());
        Toast.makeText(getContext(), getString(R.string.msg_room_exist), Toast.LENGTH_SHORT).show();
    }
}
