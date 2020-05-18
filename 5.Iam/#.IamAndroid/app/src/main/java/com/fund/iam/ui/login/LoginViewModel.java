package com.fund.iam.ui.login;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.fund.iam.BuildConfig;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.enums.SNSType;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

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
                        getCompositeDisposable().add(getDataManager().postLogin(new User(acct.getEmail(), acct.getDisplayName(), getDataManager().getPushToken(), acct.getPhotoUrl().toString(), SNSType.GOOGLE.getSnsType()))
                                .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                                .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                                .observeOn(getSchedulerProvider().ui())
                                .subscribeOn(getSchedulerProvider().io())
                                .subscribe(portFolio -> {
                                    Logger.d("result " + portFolio.isSuccessful());
                                    if (portFolio.isSuccessful()) {
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
                                Logger.d("facebook response " + response.getJSONObject());
                                Logger.d("facebook profile name " + Profile.getCurrentProfile().getName());
                                Logger.d("facebook profile url " + Profile.getCurrentProfile().getProfilePictureUri(100, 100).toString());
                                Logger.d("facebook profile token " + acct.getToken());


                                getCompositeDisposable().add(getDataManager().postLogin(new User(response.getJSONObject().getString("email"), Profile.getCurrentProfile().getName(), getDataManager().getPushToken(), Profile.getCurrentProfile().getProfilePictureUri(100, 100).toString(), SNSType.FACEBOOK.getSnsType()))
                                        .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                                        .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                                        .observeOn(getSchedulerProvider().ui())
                                        .subscribeOn(getSchedulerProvider().io())
                                        .subscribe(portFolio -> {
                                            Logger.d("result " + portFolio.isSuccessful());
                                            if (portFolio.isSuccessful()) {
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
                if (result.getKakaoAccount().emailNeedsAgreement().getBoolean()) {
                    getNavigator().showKakaoAuthPopup();
                } else {
                    getCompositeDisposable().add(getDataManager().postLogin(new User(result.getKakaoAccount().getEmail(), result.getKakaoAccount().getProfile().getNickname(), getDataManager().getPushToken(), result.getKakaoAccount().getProfile().getThumbnailImageUrl(), SNSType.KAKAO.getSnsType()))
                            .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                            .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
                            .observeOn(getSchedulerProvider().ui())
                            .subscribeOn(getSchedulerProvider().io())
                            .subscribe(portFolio -> {
                                if (portFolio.isSuccessful()) {
                                    getDataManager().setMyPortfolios(portFolio.body());
                                    getNavigator().startMainActivity();
                                } else {
                                    Logger.e("Login Error");
                                }

                            }, onError -> getNavigator().handleError(onError)));
                }
// 필요한 동의항목의 scope ID (개발자사이트 해당 동의항목 설정에서 확인 가능)


// 사용자 동의 요청


//                UserAccount kakaoAccount = result.getKakaoAccount();
//                if (kakaoAccount != null) {
//
//                    // 이메일
//                    String email = kakaoAccount.getEmail();
//
//                    if (email != null) {
//                        Log.i("KAKAO_API", "email: " + email);
//
//                    } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
//                        // 동의 요청 후 이메일 획득 가능
//                        // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
//
//                    } else {
//                        // 이메일 획득 불가
//                    }
//
//                    // 프로필
//                    Profile profile = kakaoAccount.getProfile();
//
//                    if (profile != null) {
//                        Log.d("KAKAO_API", "nickname: " + profile.getNickname());
//                        Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
//                        Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
//
//                    } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
//                        // 동의 요청 후 프로필 정보 획득 가능
//
//                    } else {
//                        // 프로필 획득 불가
//                    }
//                }


//                getCompositeDisposable().add(getDataManager().postLogin(new User("test@daum.net"/*result.getKakaoAccount().getEmail()*/, result.getKakaoAccount().getProfile().getNickname(), getDataManager().getPushToken(), result.getKakaoAccount().getProfile().getThumbnailImageUrl(), SNSType.KAKAO.getSnsType()))
//                        .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
//                        .flatMap(userInfo -> getDataManager().postPortfolios(userInfo.body().get(0).getId()))
//                        .observeOn(getSchedulerProvider().ui())
//                        .subscribeOn(getSchedulerProvider().io())
//                        .subscribe(portFolio -> {
//                            Logger.d("portFolio " + portFolio.isSuccessful());
//                            if (portFolio.isSuccessful()) {
//                                getDataManager().setMyPortfolios(portFolio.body());
//                                getNavigator().startMainActivity();
//                            } else {
//                                Logger.e("Login Error");
//                            }
//
//                        }, onError -> getNavigator().handleError(onError)));

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