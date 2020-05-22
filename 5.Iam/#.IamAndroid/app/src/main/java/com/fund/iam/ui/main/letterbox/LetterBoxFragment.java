package com.fund.iam.ui.main.letterbox;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.databinding.FragmentLetterboxBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;


public class LetterBoxFragment extends BaseFragment<FragmentLetterboxBinding, LetterBoxViewModel> implements LetterBoxNavigator {

    public static final String TAG = LetterBoxFragment.class.getSimpleName();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LetterBoxAdapter letterBoxAdapter;

    @Inject
    DataManager dataManager;

    public static LetterBoxFragment newInstance() {
        Bundle args = new Bundle();
        LetterBoxFragment fragment = new LetterBoxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_letterbox;
    }

    @Override
    public LetterBoxViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(LetterBoxViewModel.class);
    }

    @Override
    public void onRepositoriesChanged(List<LetterBox> letterBoxes) {
        Logger.d("onRepositoriesChanged " + letterBoxes.get(0));
        letterBoxAdapter.clearItems();
        letterBoxAdapter.addItems(letterBoxes);
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
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
        initViews();
//        getViewModel().dummyLetterBoxes();
    }

    private void initViews() {

        getViewDataBinding().letterBox.setLayoutManager(new LinearLayoutManager(getActivity()));
        getViewDataBinding().letterBox.setAdapter(letterBoxAdapter);
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
