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
import com.fund.iam.data.model.Letter;
import com.fund.iam.databinding.ActivityLetterBinding;
import com.fund.iam.databinding.ActivityLoginBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseActivity;
import com.fund.iam.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static com.fund.iam.core.AppConstants.RC_GOOGLE_SIGN_IN;

public class LetterActivity extends BaseActivity<ActivityLetterBinding, LetterViewModel> implements LetterNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    LetterAdapter letterAdapter;

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
    }

    private void initViews() {
        getViewDataBinding().letters.setLayoutManager(new LinearLayoutManager(this));
        getViewDataBinding().letters.setAdapter(letterAdapter);
    }

    @Override
    public void onLetterAdd(Letter letter) {
        letterAdapter.addItem(letter);
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
}
