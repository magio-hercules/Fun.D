package com.fund.iam.ui.login;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.fund.iam.BuildConfig;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.enums.SNSType;
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
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Single;
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
                        getCompositeDisposable().add(getDataManager().postLogin(new LoginBody(acct.getEmail(), acct.getDisplayName(), acct.getIdToken(), acct.getPhotoUrl().toString(),SNSType.GOOGLE.getSnsType()))
                                .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                                .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                                .observeOn(getSchedulerProvider().ui())
                                .subscribeOn(getSchedulerProvider().io())
                                .subscribe(portFolio -> {
                                    Logger.d("result " + portFolio.isSuccessful());
                                    if(portFolio.isSuccessful()) {
                                        getDataManager().setMyPortfolios(portFolio.body());
                                        getNavigator().startMainActivity();
                                    } else {
                                        Logger.e("Login Error");
                                    }

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
                        GraphRequest request = GraphRequest.newMeRequest(acct, (object, response) -> {
                            try {
                                getCompositeDisposable().add(getDataManager().postLogin(new LoginBody(response.getJSONObject().getString("email"), Profile.getCurrentProfile().getName(), acct.getToken(), Profile.getCurrentProfile().getProfilePictureUri(100, 100).toString(), SNSType.FACEBOOK.getSnsType()))
                                        .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                                        .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                                        .observeOn(getSchedulerProvider().ui())
                                        .subscribeOn(getSchedulerProvider().io())
                                        .subscribe(portFolio -> {
                                            Logger.d("result " + portFolio.isSuccessful());
                                            if(portFolio.isSuccessful()) {
                                                getDataManager().setMyPortfolios(portFolio.body());
                                                getNavigator().startMainActivity();
                                            } else {
                                                Logger.e("Login Error");
                                            }

                                        }, onError -> getNavigator().handleError(onError)));
                            } catch (JSONException e) {
                                getNavigator().handleError(e);
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    } else {
                        getNavigator().handleError(new Throwable("firebaseAuthWithFaceBook failed."));
                    }
                });


    }

    @Override
    public void onSessionOpened() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                getNavigator().handleError(errorResult.getException());
            }

            @Override
            public void onSuccess(MeV2Response result) {
                getCompositeDisposable().add(getDataManager().postLogin(new LoginBody(result.getKakaoAccount().getEmail(), result.getKakaoAccount().getProfile().getNickname(), getDataManager().getPushToken(), result.getKakaoAccount().getProfile().getThumbnailImageUrl(), SNSType.KAKAO.getSnsType()))
                        .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                        .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(portFolio -> {
                            Logger.d("result " + portFolio.isSuccessful());
                            if(portFolio.isSuccessful()) {
                                getDataManager().setMyPortfolios(portFolio.body());
                                getNavigator().startMainActivity();
                            } else {
                                Logger.e("Login Error");
                            }

                        }, onError -> getNavigator().handleError(onError)));

            }
        });

//        getCompositeDisposable().add(getDataManager().postVerifyToken(Session.getCurrentSession().getTokenInfo().getAccessToken())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribeOn(getSchedulerProvider().io())
//                .subscribe(user -> {
//                    if(user.isSuccessful()) {
//                        mFirebaseAuth.signInWithCustomToken(user.body().getFirebaseToken());
//                    } else {
//                        Logger.e("firebase kakao Auth Error " + user.errorBody().toString());
//                    }
//                }, onError -> getNavigator().handleError(onError)));
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Logger.d("onSessionOpenFailed " + exception);
        getNavigator().handleError(exception);
    }


}