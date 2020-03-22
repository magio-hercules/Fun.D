package com.fund.iam.ui.login;


import android.content.Context;
import android.view.View;

import com.fund.iam.BuildConfig;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthCredential;
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
        subscribeEvent();
        Session.getCurrentSession().addCallback(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_SIGN_URL)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    private void subscribeEvent() {

    }

//    private Task<String> getFirebaseJwt(final String kakaoAccessToken) {
//        final TaskCompletionSource<String> source = new TaskCompletionSource<>();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = getResources().getString(R.string.validation_server_domain) + "/verifyToken";
//        HashMap<String, String> validationObject = new HashMap<>();
//        validationObject.put("token", kakaoAccessToken);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(validationObject), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String firebaseToken = response.getString("firebase_token");
//                    source.setResult(firebaseToken);
//                } catch (Exception e) {
//                    source.setException(e);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.toString());
//                source.setException(error);
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("token", kakaoAccessToken);
//                return params;
//            }
//        };
//
//        queue.add(request);
//        return source.getTask();
//    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Logger.d("LoginActivity firebaseAuthWithGoogle " + credential);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Todo : AWS API 연동;
                        getNavigator().startMainActivity();
                    } else {
                        getNavigator().handleError(new Throwable("firebaseAuthWithGoogle error"));
                    }
                });
    }


    @Override
    public void onSessionOpened() {
        Logger.d("onSessionOpened");
        String accessToken = Session.getCurrentSession().getTokenInfo().getAccessToken();
//        getFirebaseJwt(accessToken).continueWithTask(new Continuation<String, Task<AuthResult>>() {
//            @Override
//            public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
//                String firebaseToken = task.getResult();
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                return auth.signInWithCustomToken(firebaseToken);
//            }
//        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    updateUI();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Failed to create a Firebase user.", Toast.LENGTH_LONG).show();
//                    if (task.getException() != null) {
//                        Log.e(TAG, task.getException().toString());
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Logger.d("onSessionOpenFailed " + exception);
        getNavigator().handleError(exception);
    }
}
