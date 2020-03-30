package com.fund.iam.ui.main.home;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.data.model.request.LoginBody;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    private static final String TAG = "[IAM][HOME][VM]";

    public User myInfo = null;
    public List<Portfolio> myPortfolio = null;

    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);

        subscribeEvent();
    }

    private void subscribeEvent() {

    }


//    public void testUsersApi() {
//        getCompositeDisposable().add(getDataManager().postUsers(1).subscribe(result -> {
//            Logger.d(result.body());
//        }));
//    }
//
//    public void testPortfolioApi() {
//        getCompositeDisposable().add(getDataManager().postPortfolios(1).subscribe(result -> {
//            Logger.d(result.body());
//        }));
//    }


    public void getUserInfo() {
        Log.d(TAG, "getUserInfo");

        // TODO : userId를 로그인 정보에서 가져오도록 수정
        int userId = 1;
        getCompositeDisposable().add(
                getDataManager().postUsers(userId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    Logger.d("postUsers success");
                    List<User> arrResult = result.body();
                    myInfo = (User)arrResult.get(0);
                    Logger.d("result.body " + myInfo);

                    getNavigator().updateUser();
                }, onError -> getNavigator().handleError(onError)));

//        retrofitService.getUserInfo(userId).enqueue(new Callback<List<UserInfo>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<UserInfo>> call,
//                                   @NonNull Response<List<UserInfo>> response) {
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "======================================");
//                    Log.e(TAG, "response.isSuccessful getUserInfo 출력 시작");
//
//                    List<UserInfo> userList = response.body();
//                    mUserList = userList;
//                    for (UserInfo data: userList) {
//                        Log.d("data.getId()", data.getId() + "");
//                        Log.d("data.getEmail()", data.getEmail());
//                        Log.d("data.getUser_name()", data.getUser_name());
//                        Log.d("data.getNick_name()", data.getNick_name());
//
//                        if (data.getId() == userId) {
//                            changeUIUserInfo(data);
//                        }
//                    }
//
//                    getUserPortfolio();
//
//                    Log.e(TAG, "response.isSuccessful getUserInfo 출력 끝");
//                    Log.e(TAG, "======================================");
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<UserInfo>> call, @NonNull Throwable t) {
//                Log.d(TAG, "retrofitService.getUserInfo onFailure");
//
//                Toast.makeText(getActivity(), "서버를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    public void getUserPortfolio() {
        Log.d(TAG, "getUserPortfolio");

        // TODO : userId를 로그인 정보에서 가져오도록 수정
        int userId = 1;
        getCompositeDisposable().add(
                getDataManager().postPortfolios(1)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Logger.d("postUsers success");
                            myPortfolio = result.body();
                            Logger.d("result.body " + myPortfolio);

                            getNavigator().updatePortfolio();
                        }, onError -> getNavigator().handleError(onError)));

        getCompositeDisposable().add(getDataManager().postUsers(1).subscribe(result -> {
            Logger.d(result.body());
        }));


        Log.d(TAG, "end getUserInfo");
    }


//    public void handleLetterBox() {
//        Log.d(TAG, "handleLetterBox");
//
//        getNavigator().startLetterActivity();
//    }

//    public void handleModify() {
//        Log.d(TAG, "handleModify");
//
//    }

    public void handleInsertImage() {
        Log.d(TAG, "handleInsertImage");

    }

    public void handleInsertText() {
        Log.d(TAG, "handleInsertText");

    }
}
