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

package com.fundroid.offstand.ui.lobby.findroom;

import android.os.Bundle;
import android.view.View;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.RoomActivity;
import com.fundroid.offstand.databinding.FragmentFindRoomBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import static com.fundroid.offstand.core.AppConstant.FRAGMENT_FIND_ROOM;

public class FindRoomFragment extends BaseFragment<FragmentFindRoomBinding, FindRoomViewModel> implements FindRoomNavigator {

    public static final String TAG = FindRoomFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private FindRoomViewModel findRoomViewModel;

    private FragmentFindRoomBinding fragmentFindRoomBinding;

    public static FindRoomFragment newInstance() {
        Bundle args = new Bundle();
        FindRoomFragment fragment = new FindRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find_room;
    }

    @Override
    public FindRoomViewModel getViewModel() {
        findRoomViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(FindRoomViewModel.class);
        return findRoomViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findRoomViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentFindRoomBinding = getViewDataBinding();
        initViews();
    }

    private void initViews() {

    }

    @Override
    public void goToRoomActivity() {
        RoomActivity.start(getContext());
        getBaseActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }
}
