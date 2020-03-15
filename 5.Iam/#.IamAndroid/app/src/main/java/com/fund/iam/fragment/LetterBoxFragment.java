package com.fund.iam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.LetterBoxAdapter;
import com.fund.iam.LetterBoxNavigator;
import com.fund.iam.LetterBoxViewModel;
import com.fund.iam.R;
import com.fund.iam.databinding.FragmentListChatBinding;
import com.fund.iam.model.LetterBox;

import java.util.List;

public class LetterBoxFragment extends Fragment implements LetterBoxNavigator {

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();
    private FragmentListChatBinding binding;
    private LetterBoxViewModel letterBoxViewModel;
    private LetterBoxAdapter letterBoxAdapter = new LetterBoxAdapter();


    public static LetterBoxFragment newInstance() {
        Bundle args = new Bundle();
        LetterBoxFragment fragment = new LetterBoxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_chat, container, false);
        if (viewModelFactory == null) {
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        }
        letterBoxViewModel = new ViewModelProvider(this, viewModelFactory).get(LetterBoxViewModel.class);
        letterBoxViewModel.setNavigator(this);
        binding.setViewModel(letterBoxViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        binding.chats.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.chats.setAdapter(letterBoxAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        letterBoxViewModel.dummyLetterBoxes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
    }

    @Override
    public void onRepositoriesChanged(List<LetterBox> letterBoxes) {
        letterBoxAdapter.addItems(letterBoxes);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void handleError(Throwable throwable) {

    }
}
