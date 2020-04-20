package com.fund.iam.ui.main.home;


import android.util.Log;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    private static final String TAG = "[IAM][HOME][VM]";

//    public List<Job> listJob = null;

//    public User myInfo = null;
//    public List<Portfolio> myPortfolio = null;


    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        Logger.d("HomeVIewModel constructor");

        subscribeEvent();
    }

    private void subscribeEvent() {

    }


    public void getUserInfo() {
        Log.d(TAG, "getUserInfo");

        getCompositeDisposable().add(
                getDataManager().postJobs()
                        .flatMap(result -> {
                            Log.d(TAG, "postJobList success");
//                            Logger.d(result.body());

//                            return getDataManager().postUserInfo(getDataManager().getMyInfo().getId());
                            return getDataManager().postUserInfo(getDataManager().getMyInfo().getEmail(), getDataManager().getMyInfo().getSnsType());
                        })
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postUserInfo success");
//                Logger.d(result.body());

                            List<User> arrResult = result.body();
//                            myInfo = (User)arrResult.get(0);
//                            Log.d(TAG, "result.body " + myInfo);

                            getNavigator().updateUser();
                        }, onError -> getNavigator().handleError(onError)));
    }


    public void getUserPortfolio() {
        Log.d(TAG, "getUserPortfolio");

        getCompositeDisposable().add(
                getDataManager().postPortfolios(getDataManager().getMyInfo().getId())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postPortfolios success");
//                            myPortfolio = result.body();
//                            Log.d(TAG, "result.body " + myPortfolio);

                            getNavigator().updatePortfolio();
                        }, onError -> getNavigator().handleError(onError)));

        Log.d(TAG, "end getUserPortfolio");
    }

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


    public void insertPortfolioText(String text) {
        Log.d(TAG, "insertPortfolioText : " + text);


        getCompositeDisposable().add(
//            getDataManager().postInsertPortfolio(newPortfolio)
                getDataManager().postInsertPortfolio(getDataManager().getMyInfo().getId(), 1, text)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postInsertPortfolio success");
//                Logger.d("insertPortfolioText success");
//                Logger.d(result.body());
                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));
    }

    public void insertPortfolioImage(MultipartBody.Part body, RequestBody fileName) {
//    public void insertPortfolioImage(Bitmap bitmap) {
        Log.d(TAG, "insertPortfolioImage fileName: " + fileName.toString());

        // image를 s3에 올린 이후 받은 url을 DB에 insert
        getCompositeDisposable().add(
                // s3 업로드 이후 구현
                getDataManager().postUploadImage(body, fileName)
                        .flatMap(result -> {
                              // result로 url 을 받아온 이후
                            Log.d(TAG, "postUploadImage success");

                            final String s3Url = result.body();
                            Logger.d(s3Url);

//                Portfolio newPortfolio = new Portfolio(userId, 1, "text", newUrl);
//                            listJob = result.body();
//                            Logger.d(listJob);

                            return getDataManager().postInsertPortfolio(getDataManager().getMyInfo().getId(), 2, s3Url);
                        })
                // image url 사용하도록 API 수정 필요
//                getDataManager().postInsertPortfolio(getDataManager().getMyInfo().getId(), 2, s3Url)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "insertPortfolioImage success");
//                            Logger.d("insertPortfolioImage success");
                            Logger.d(result.body());

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

    public void handleUserInfo() {
        Log.d(TAG, "handleUserInfo");

        User userInfo = getDataManager().getMyInfo();
        Log.d(TAG, "myInfo id : " + userInfo.getId() + ", email: " + userInfo.getEmail()
        + ", snsType: " + userInfo.getSnsType());

        getCompositeDisposable().add(
                getDataManager().postUserInfo(userInfo.getEmail(), userInfo.getSnsType())
                        .doOnSuccess(info -> {
                            Log.d(TAG, "postUserInfo success");
                            getDataManager().setMyInfo(info.body().get(0));
                        })
                        .flatMap(info -> getDataManager().postPortfolios(info.body().get(0).getId()))
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(portFolio -> {
                            if (portFolio.isSuccessful()) {
                                Log.d(TAG, "postPortfolios success");
                                getDataManager().setMyPortfolios(portFolio.body());
                                getNavigator().goBack();
                            } else {
                                Logger.e("Login Error");
                            }
                        }, onError -> getNavigator().handleError(onError)));
    }

    public void handleUserUpdate(User updateInfo) {
        Log.d(TAG, "handleUserUpdate");

        getCompositeDisposable().add(getDataManager().postUserUpdate(updateInfo)
                .doOnSuccess(userInfo -> getDataManager().setMyInfo(userInfo.body().get(0)))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(portFolio -> {
                    Logger.d("handleUserUpdate " + portFolio.isSuccessful());
                    if (portFolio.isSuccessful()) {
//                        getDataManager().setMyPortfolios(portFolio.body());

//                        getNavigator().updateUser();
                        getNavigator().onSuccess();
                    } else {
                        Logger.e("Login Error");
                    }

                }, onError -> getNavigator().handleError(onError)));
    }

    public void uploadImage(MultipartBody.Part body, RequestBody fileName) {
        Log.d(TAG, "uploadImage  fileName : " + fileName);

        getCompositeDisposable().add(
                getDataManager().postUploadImage(body, fileName)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postUploadImage success");
                            Logger.d(result.body());

                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));
    }
}
