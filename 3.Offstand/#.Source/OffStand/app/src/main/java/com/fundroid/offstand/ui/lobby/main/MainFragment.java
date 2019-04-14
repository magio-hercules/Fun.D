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

package com.fundroid.offstand.ui.lobby.main;

import android.os.Bundle;
import android.view.View;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.FragmentMainBinding;
import com.fundroid.offstand.ui.base.BaseFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import pl.droidsonroids.gif.GifDrawable;

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> implements MainNavigator {

    public static final String TAG = MainFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private MainViewModel mainViewModel;

    private FragmentMainBinding fragmentMainBinding;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public MainViewModel getViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        return mainViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMainBinding = getViewDataBinding();
        initViews();
    }

    private void initViews() {
        ((GifDrawable) fragmentMainBinding.bgMain.getDrawable()).setLoopCount(0);
    }
}
