package com.fundroid.offstand.ui.lobby.guide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.databinding.FragmentGuideBinding;
import com.fundroid.offstand.ui.base.BaseFragment;

import com.fundroid.offstand.ui.play.PlayActivity;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

public class GuideFragment extends BaseFragment<FragmentGuideBinding, GuideViewModel> implements GuideNavigator {

    ImageView exit_jokbo;
    PlayActivity playActivity;

    public static final String TAG = GuideFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private GuideViewModel guideViewModel;

    private FragmentGuideBinding fragmentGuideBinding;

    public static GuideFragment newInstance() {
        Bundle args = new Bundle();
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    public GuideViewModel getViewModel() {
        guideViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(GuideViewModel.class);
        return guideViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guideViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentGuideBinding = getViewDataBinding();

        exit_jokbo = (ImageView)  view.findViewById(R.id.play_image_exit_jokbo);
        exit_jokbo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playActivity.Game_Jokbo_Close();
            }
        });

        initViews();
    }

    private void initViews() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        playActivity = (PlayActivity) getActivity();
    }
}