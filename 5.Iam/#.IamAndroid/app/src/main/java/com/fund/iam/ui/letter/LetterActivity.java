package com.fund.iam.ui.letter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.Letter;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.databinding.ActivityLetterBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseActivity;
import com.fund.iam.ui.main.MainActivity;
import com.fund.iam.utils.CommentKeyBoardFix;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

public class LetterActivity extends BaseActivity<ActivityLetterBinding, LetterViewModel> implements LetterNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LetterAdapter letterAdapter;

    @Inject
    DataManager dataManager;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_letter;
    }

    @Override
    public LetterViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(LetterViewModel.class);
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, LetterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        initViews();
        if (getIntent().getExtras() != null) {
            User user = new Gson().fromJson(getIntent().getExtras().get("friend").toString(), User.class);
            User friend = new Gson().fromJson(getIntent().getExtras().get("user").toString(), User.class);
            if (dataManager.getMyInfo() == null) {
                getViewModel().getInitialData();
            }
            LetterBoxBus.getInstance().sendLetterBox(new LetterBox(friend));
            getViewModel().postMessage(user.getId());
        } else {
            getViewModel().postMessage(dataManager.getMyInfo().getId());
        }

    }

    private void initViews() {
        new CommentKeyBoardFix(this);
        getViewDataBinding().letters.setLayoutManager(new LinearLayoutManager(this));
        getViewDataBinding().letters.setAdapter(letterAdapter);
    }

    @Override
    public void onLetterSet(List<Letter> letters) {
        letterAdapter.addItems(letters);
        getViewDataBinding().letters.scrollToPosition(letterAdapter.getItemCount() - 1);

    }

    @Override
    public void onLetterAdd(Letter letter) {
        letterAdapter.addItem(letter);
        getViewDataBinding().letters.scrollToPosition(letterAdapter.getItemCount() - 1);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Logger.e("handleError " + throwable.getMessage());
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            MainActivity.start(this);
        }
        super.onBackPressed();
    }
}
