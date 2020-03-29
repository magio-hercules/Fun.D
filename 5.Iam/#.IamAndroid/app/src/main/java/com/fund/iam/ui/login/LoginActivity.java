package com.fund.iam.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.databinding.ActivityLoginBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseActivity;
import com.fund.iam.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.Session;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static com.fund.iam.core.AppConstants.RC_GOOGLE_SIGN_IN;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(LoginViewModel.class);
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        initViews();
    }

    private void initViews() {
        getViewDataBinding().btnGoogle.setOnClickListener(view -> startActivityForResult(getViewModel().getMGoogleSignInClient().getSignInIntent(), RC_GOOGLE_SIGN_IN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("LoginActivity onActivityResult " + requestCode + ", " + resultCode);
        //kakao
        //Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Logger.d("LoginActivity account " + account);
                getViewModel().firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                handleError(e);
            }
        }

    }

    @Override
    public void startMainActivity() {
        MainActivity.start(this);
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
