package com.fund.iam.ui.main.home;


import android.graphics.Bitmap;
import android.util.Log;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    private static final String TAG = "[IAM][HOME][VM]";

    int userId = 1;

    public List<Job> listJob = null;

    public User myInfo = null;
    public List<Portfolio> myPortfolio = null;


    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        Logger.d("HomeVIewModel constructor");
        // TODO userId 가져오기
        userId = 1;

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


    public void getJobList() {
        Log.d(TAG, "postJobList");

        getCompositeDisposable().add(
                getDataManager().postJobs()
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postJobList success");
                            listJob = result.body();
                            Log.d(TAG, "listJob is " + listJob);
                        }, onError -> getNavigator().handleError(onError)));
    }

//    public Maybe<Response<List<User>>> getUserData(int userId) {
//            return getDataManager().postUsers(userId)
//                .observeOn(getSchedulerProvider().ui())
//                .subscribeOn(getSchedulerProvider().io())
//                .subscribe(result -> {
//                    Log.d(TAG, "postUsers success");
//                    List<User> arrResult = result.body();
//                    myInfo = (User)arrResult.get(0);
//                    Log.d(TAG, "result.body " + myInfo);
//
//                    getNavigator().updateUser();
//                }, onError -> getNavigator().handleError(onError)));
////        return Single.just(Collections.singletonList("Hello")) // (1)
////                .map(organizations -> {
////                    if (organizations.isEmpty()) {
////                        throw Exceptions.propagate(new NullPointerException("List is empty"));
////                    }
////                    return organizations;
////                }).doOnSuccess(organizations -> {
////                    // do something with organizations list
////                });
//    }

    public void getUserInfo() {
        Log.d(TAG, "getUserInfo");

//        makeRequestToServiceA()
//                .flatMap(responseFromServiceA -> makeRequestToServiceB(responseFromServiceA),
//                        (responseFromServiceA, responseFromServiceB) -> Observable.just("here is combined result!"))

//                  firstNetworkCall().subscribeOn(Schedulers.io())
//                .mergeWith(secondNetworkCall().subscribeOn(Schedulers.io())

        getCompositeDisposable().add(
            getDataManager().postJobs()
            .flatMap(result -> {
                Log.d(TAG, "postJobList success");
//                Logger.d(result.body());

                listJob = result.body();
//                Logger.d(listJob);

                return getDataManager().postUsers(userId);
            })
            .observeOn(getSchedulerProvider().ui())
            .subscribeOn(getSchedulerProvider().io())
            .subscribe(result -> {
                Log.d(TAG, "postUsers success");
//                Logger.d(result.body());

                List<User> arrResult = result.body();
                myInfo = (User)arrResult.get(0);
                Log.d(TAG, "result.body " + myInfo);

                getNavigator().updateUser();
            }, onError -> getNavigator().handleError(onError)));


//        getCompositeDisposable().add(
//                getDataManager().postUsers(userId)
//                .observeOn(getSchedulerProvider().ui())
//                .subscribeOn(getSchedulerProvider().io())
//                .subscribe(result -> {
//                    Logger.d("postUsers success");
//                    List<User> arrResult = result.body();
//                    myInfo = (User)arrResult.get(0);
//                    Log.d(TAG, "result.body " + myInfo);
//
//                    getNavigator().updateUser();
//                }, onError -> getNavigator().handleError(onError)));

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

        getCompositeDisposable().add(
                getDataManager().postPortfolios(1)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postUsers success");
                            myPortfolio = result.body();
                            Log.d(TAG, "result.body " + myPortfolio);

                            getNavigator().updatePortfolio();
                        }, onError -> getNavigator().handleError(onError)));



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

        getNavigator().insertImage();
    }

    public void handleInsertText() {
        Log.d(TAG, "handleInsertText");

        getNavigator().insertText();

    }

    public void handleSave() {
        Log.d(TAG, "handleSave");

        // 전체 리스트 중 id가 없는 항목만 API를 요청하여 추가
//        this.getUserPortfolio();

//                portfolio_layout
//        LinearLayout layout = setupLayout();
//        int count = layout.getChildCount();
//        View v = null;
//        for(int i=0; i<count; i++) {
//            v = layout.getChildAt(i);
//            //do something with your child element
//        }

        // 신규 리스트 추가


        // 타입에 따라 포트폴리오 추가
//        insertPortfolioText(text)
    }



    ////////////////////////////////////
    // Edit mode
    ////////////////////////////////////

    public void getUserInfoEdit() {
        Log.d(TAG, "getUserInfoEdit");

        getCompositeDisposable().add(
                getDataManager().postJobs()
                        .flatMap(result -> {
                            Log.d(TAG, "postJobList success");
//                            Logger.d(result.body());

                            listJob = result.body();
//                            Logger.d(listJob);

                            return getDataManager().postUsers(userId);
                        })
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postUsers success");
//                            Logger.d(result.body());

                            List<User> arrResult = result.body();
                            myInfo = (User)arrResult.get(0);
//                            Logger.d("result.body " + myInfo);

                            getNavigator().updateUser();
                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));

    }

    public void insertPortfolioText(String text) {
        Log.d(TAG, "insertPortfolioText : " + text);


        getCompositeDisposable().add(
//            getDataManager().postInsertPortfolio(newPortfolio)
                getDataManager().postInsertPortfolio(userId, 1, text)
            .observeOn(getSchedulerProvider().ui())
            .subscribeOn(getSchedulerProvider().io())
            .subscribe(result -> {
                Log.d(TAG, "postInsertPortfolio success");
//                Logger.d("insertPortfolioText success");
//                Logger.d(result.body());
                getNavigator().onSuccess();
            }, onError -> getNavigator().handleError(onError)));
    }

    public void insertPortfolioImage(Bitmap bitmap) {
        Log.d(TAG, "insertPortfolioImage");

        String s3Url = "";
        // image를 s3에 올린 이후 받은 url을 DB에 insert
        getCompositeDisposable().add(
                // s3 업로드 이후 구현
//                getDataManager().postS3Upload()
//                        .flatMap(result -> {
//                              // result로 url 을 받아온 이후
//                            Log.d(TAG, "postS3Upload success");
////                            Logger.d(result.body());
//
////                Portfolio newPortfolio = new Portfolio(userId, 1, "text", newUrl);
////                            listJob = result.body();
////                            Logger.d(listJob);
//
//                            return getDataManager().postInsertPortfolio(newPortfolio);
//                        })
                // image url 사용하도록 API 수정 필요
                getDataManager().postInsertPortfolio(userId, 2, s3Url)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "insertPortfolioImage success");
//                            Logger.d("insertPortfolioImage success");
//                            Logger.d(result.body());
                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));
    }

    public void deletePortfolioText(int id) {
        Log.d(TAG, "DeletePortfolioText  id : " + id);


//        Portfolio newPortfolio = new Portfolio(userId, 1, text, "");

        getCompositeDisposable().add(
                getDataManager().postDeletePortfolio(id)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postDeletePortfolio success");
//                            Logger.d("postDeletePortfolio success");
//                            Logger.d(result.body());
                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));
    }

    public void updatePortfolio(int id, int userId, int type, String text) {
        Log.d(TAG, "updatePortfolio  id : " + id + ", text : " + text);

//        Portfolio newPortfolio = new Portfolio(userId, 1, text, "");

        getCompositeDisposable().add(
            getDataManager().postUpdatePortfolio(id, userId, type, text)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(result -> {
                    Log.d(TAG, "postDeletePortfolio success");
//                            Logger.d("postDeletePortfolio success");
//                            Logger.d(result.body());
                    getNavigator().onSuccess();
                }, onError -> getNavigator().handleError(onError))
        );
    }
}
