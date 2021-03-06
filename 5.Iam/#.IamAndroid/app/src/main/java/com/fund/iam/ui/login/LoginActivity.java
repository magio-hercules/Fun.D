package com.fund.iam.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.databinding.ActivityLoginBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseActivity;
import com.fund.iam.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.fund.iam.core.AppConstants.RC_FACEBOOK_SIGN_IN;
import static com.fund.iam.core.AppConstants.RC_GOOGLE_SIGN_IN;
import static com.fund.iam.core.AppConstants.RC_KAKAO_SIGN_IN;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_FAILED;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator, FacebookCallback<LoginResult> {

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

    private CallbackManager mCallbackManager = CallbackManager.Factory.create();

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
        getViewDataBinding().btnGoogleCustom.setOnClickListener(view -> startActivityForResult(getViewModel().getMGoogleSignInClient().getSignInIntent(), RC_GOOGLE_SIGN_IN));
        getViewDataBinding().btnFacebook.setPermissions("email", "public_profile");
        getViewDataBinding().btnFacebook.registerCallback(mCallbackManager, this);
        getViewDataBinding().btnFacebookCustom.setOnClickListener(view -> getViewDataBinding().btnFacebook.performClick());
        getViewDataBinding().btnKakaoCustom.setOnClickListener(view -> getViewDataBinding().btnKakao.performClick());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("LoginActivity onActivityResult " + requestCode + ", " + resultCode);
        switch (requestCode) {

            case RC_KAKAO_SIGN_IN:
                Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);


                break;

            case RC_GOOGLE_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Logger.d("LoginActivity account " + account);
                    getViewModel().firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case SIGN_IN_FAILED:
                            showToast("구글 로그인 도중 실패하었습니다. 다시 시도해주세요.");
                            break;

                        case SIGN_IN_CANCELLED:
                            showToast("구글 로그인 도중 취소되었습니다. 다시 시도해주세요.");
                            break;

                        case SIGN_IN_CURRENTLY_IN_PROGRESS:
                            showToast("구글 로그인 도중 중지되었습니다. 다시 시도해주세요.");
                            break;
                        default:
                            handleError(e);
                    }
                }
                break;

            case RC_FACEBOOK_SIGN_IN:
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @Override
    public void startMainActivity() {
        MainActivity.start(this);
        finish();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        getViewModel().firebaseAuthWithFaceBook(loginResult.getAccessToken());

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void showKakaoAuthPopup() {
        Session.getCurrentSession().updateScopes(this, Arrays.asList("account_email"), new AccessTokenCallback() {

            @Override
            public void onAccessTokenReceived(AccessToken accessToken) {
                getViewModel().onSessionOpened();
            }

            @Override
            public void onAccessTokenFailure(ErrorResult errorResult) {
                handleError(errorResult.getException());
            }
        });
    }

    @Override
    public void onError(FacebookException error) {
        handleError(error);
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
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
    }
}