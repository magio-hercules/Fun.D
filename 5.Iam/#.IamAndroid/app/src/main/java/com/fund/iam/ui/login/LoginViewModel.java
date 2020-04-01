package com.fund.iam.ui.login;


import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.fund.iam.BuildConfig;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.request.LoginBody;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import lombok.Getter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class LoginViewModel extends BaseViewModel<LoginNavigator> implements ISessionCallback {

    @Getter
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mFirebaseAuth;

    public LoginViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        Session.getCurrentSession().addCallback(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_SIGN_URL)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Todo : AWS API 연동;
                        getCompositeDisposable().add(getDataManager().postLogin(new LoginBody(acct.getEmail(), acct.getDisplayName(), acct.getIdToken(), acct.getPhotoUrl().toString()))
                                .observeOn(getSchedulerProvider().ui())
                                .subscribeOn(getSchedulerProvider().io())
                                .subscribe(testVoid -> {
                                    Logger.d("result " + testVoid.isSuccessful());
                                    getNavigator().startMainActivity();
                                }, onError -> getNavigator().handleError(onError)));
                    } else {
                        getNavigator().handleError(new Throwable("firebaseAuthWithGoogle failed."));
                    }
                });
    }

    public void firebaseAuthWithFaceBook(AccessToken acct) {
        AuthCredential credential = FacebookAuthProvider.getCredential(acct.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Todo : AWS API 연동;
                        getNavigator().startMainActivity();
                    } else {
                        getNavigator().handleError(new Throwable("firebaseAuthWithFaceBook failed."));
                    }
                });


    }

    @Override
    public void onSessionOpened() {
        getCompositeDisposable().add(getDataManager().postVerifyToken(Session.getCurrentSession().getTokenInfo().getAccessToken())
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(user -> {
                    if(user.isSuccessful()) {
                        mFirebaseAuth.signInWithCustomToken(user.body().getFirebaseToken());
                    } else {
                        Logger.e("firebase kakao Auth Error " + user.errorBody().toString());
                    }
                }, onError -> getNavigator().handleError(onError)));
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Logger.d("onSessionOpenFailed " + exception);
        getNavigator().handleError(exception);
    }


}
