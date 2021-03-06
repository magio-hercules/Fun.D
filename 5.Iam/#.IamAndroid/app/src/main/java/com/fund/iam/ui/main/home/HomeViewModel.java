package com.fund.iam.ui.main.home;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.User;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    private static final String TAG = "[IAM][HOME][VM]";

//    public List<Job> listJob = null;

    //    public User myInfo = null;
//    public List<Portfolio> myPortfolio = null;
    private Context mContext;

    private int mUserId = -1;
    private int mFriendId = -1;
    private boolean mBookmarkUser;



    public HomeViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        Logger.d("HomeVIewModel constructor");

        mContext = context;

        mBookmarkUser = false;

        subscribeEvent();
    }

    private void subscribeEvent() {

    }

    public boolean getBookmarkUser() {
        return mBookmarkUser;
    }

    public void getUserInfo(String email, String type) {
        Log.d(TAG, "getUserInfo email : " + email + ", type : " + type);

        getCompositeDisposable().add(
                getDataManager().postUserInfo(email, type)
//                        .doOnSuccess(info -> {
//                            Log.d(TAG, "postUserInfo success");
//                            getNavigator().updateUser(info.body().get(0));
//                        })
//                        .flatMap(info -> getDataManager().postPortfolios(info.body().get(0).getId()))
                        .flatMap(info -> {
                            Log.d(TAG, "postUserInfo success");
                            getNavigator().updateUser(info.body().get(0));

                            return getDataManager().postPortfolios(info.body().get(0).getId());
                        })
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(portFolio -> {
                            if (portFolio.isSuccessful()) {
                                Log.d(TAG, "postPortfolios success");
                                getNavigator().updatePortfolio(portFolio.body());
                            } else {
                                Logger.e("getUserInfo Error");
                            }
                        }, onError -> getNavigator().handleError(onError)));
    }

    public void getBookmarkUserInfo(int user_id, int friend_id) {
        Log.d(TAG, "getBookmarkUserInfo user_id : " + user_id + ", friend_id : " + friend_id);

        mUserId = user_id;
        mFriendId = friend_id;

        getCompositeDisposable().add(
                getDataManager().postBookmarkUser(user_id)
//                        .doOnSuccess(info -> {
//                            Log.d(TAG, "postUserInfo success");
//                            getNavigator().updateUser(info.body().get(0));
//                        })
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(info -> {
                            Log.d(TAG, "getBookmarkUserInfo success");

                            mBookmarkUser = false;
                            List<User> userList = info.body();
                            for (User userInfo : userList) {
                                if (userInfo.getId() == friend_id) {
                                    mBookmarkUser = true;
                                    break;
                                }
                            }

                            getNavigator().updateBookmark(mBookmarkUser);
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

    public void handleInsertProfile() {
        Log.d(TAG, "handleInsertProfile");

        getNavigator().insertProfile();
    }

    public void handleInsertImage() {
        Log.d(TAG, "handleInsertImage");

        getNavigator().insertImage();
    }

    public void handleInsertText() {
        Log.d(TAG, "handleInsertText");

        getNavigator().insertText();

    }

    public void toggleBookmark() {
        Log.d(TAG, "toggleBookmark");

        Log.d(TAG, "getBookmarkUserInfo userId : " + mUserId + ", friend_id : " + mFriendId);

        if (mBookmarkUser) {
            getCompositeDisposable().add(
                    getDataManager().postBookmarkUserDelete(mUserId, mFriendId)
//                        .doOnSuccess(info -> {
//                            Log.d(TAG, "postUserInfo success");
//                            getNavigator().updateUser(info.body().get(0));
//                        })
                            .observeOn(getSchedulerProvider().ui())
                            .subscribeOn(getSchedulerProvider().io())
                            .subscribe(info -> {
                                Log.d(TAG, "postBookmarkUserDelete success");
                                mBookmarkUser = !mBookmarkUser;
                                getNavigator().updateBookmark(mBookmarkUser);
                            }, onError -> getNavigator().handleError(onError)));
        } else {
            getCompositeDisposable().add(
                    getDataManager().postBookmarkUserInsert(mUserId, mFriendId)
//                        .doOnSuccess(info -> {
//                            Log.d(TAG, "postUserInfo success");
//                            getNavigator().updateUser(info.body().get(0));
//                        })
                            .observeOn(getSchedulerProvider().ui())
                            .subscribeOn(getSchedulerProvider().io())
                            .subscribe(info -> {
                                Log.d(TAG, "postBookmarkUserInsert success");
                                mBookmarkUser = !mBookmarkUser;
                                getNavigator().updateBookmark(mBookmarkUser);
                            }, onError -> getNavigator().handleError(onError)));
        }
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
                getDataManager().postImageUpload(body, fileName)
                        .flatMap(result -> {
                            // result로 url 을 받아온 이후
                            Log.d(TAG, "postImageUpload success");

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

    public Single<Response<String>> singleImageUpload(MultipartBody.Part body, RequestBody fileName) {
//    public String singleImageUpload(MultipartBody.Part body, RequestBody fileName) {
         return getDataManager().postImageUpload(body, fileName);
////                .flatMap(result -> {
////                    // result로 url 을 받아온 이후
////                    Log.d(TAG, "postImageUpload success");
////
////                    final String s3Url = result.body();
////                    Log.d(TAG, "s3Url : " + s3Url);
////
//////                    return Single<Response<String>>(s3Url);
////                    return s3Url;
////                });
//                .observeOn(getSchedulerProvider().ui())
//                .subscribeOn(getSchedulerProvider().io())
//                .subscribe(result -> {
//                    // result로 url 을 받아온 이후
//                    Log.d(TAG, "postImageUpload success");
//
//                    String s3Url = result.body();
//                    Log.d(TAG, "s3Url : " + s3Url);
//
//                    return s3Url;
//                }, onError -> getNavigator().handleError(onError)));
    }

    public Single<Response<Void>> singleImageDelete(RequestBody fileName) {
        return getDataManager().postImageDelete(fileName);
    }

    public Single<Response<Void>> singleInsertText(String text) {
        return getDataManager().postInsertPortfolio(getDataManager().getMyInfo().getId(), 1, text);
    }

    public Single<Response<Void>> singleInsertImage(MultipartBody.Part body, RequestBody fileName) {
        return getDataManager().postImageUpload(body, fileName)
                .flatMap(result -> {
                    // result로 url 을 받아온 이후
                    Log.d(TAG, "postImageUpload success");

                    final String s3Url = result.body();
                    Logger.d(s3Url);

                    return getDataManager().postInsertPortfolio(getDataManager().getMyInfo().getId(), 2, s3Url);
                });
    }

    public void deletePortfolioText(int type, int id, String value) {
        Log.d(TAG, "DeletePortfolioText type: " + type + ", id : " + id + ", value : " + value);


        RequestBody reqFileName = RequestBody.create(MediaType.parse("text/plain"), value);

//        Portfolio newPortfolio = new Portfolio(userId, 1, text, "");

        getCompositeDisposable().add(
                getDataManager().postImageDelete(reqFileName)
                        .flatMap(result -> {
                            Log.d(TAG, "postImageDelete success");
                            return getDataManager().postDeletePortfolio(id);
                        })
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postDeletePortfolio success");
//                            Logger.d("postDeletePortfolio success");
//                            Logger.d(result.body());
                            getNavigator().onSuccess();
                        }, onError -> Logger.e("deletePortfolioText Error")));


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
                getDataManager().postImageUpload(body, fileName)
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeOn(getSchedulerProvider().io())
                        .subscribe(result -> {
                            Log.d(TAG, "postImageUpload success");
                            Logger.d(result.body());

                            getNavigator().onSuccess();
                        }, onError -> getNavigator().handleError(onError)));
    }

    private void uploadImage(Bitmap bitmap, int viewId) {
        try {

            File filesDir = mContext.getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            User userInfo = getDataManager().getMyInfo();

            String fileName = userInfo.getEmail() + "_" + userInfo.getSnsType()
                    + "_" + viewId + ".jpg";
            Log.d(TAG, "uploadImage fileName : " + fileName);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            RequestBody reqFileName = RequestBody.create(MediaType.parse("text/plain"), fileName);

//            getViewModel().uploadImage(bitmap);
            insertPortfolioImage(body, reqFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
