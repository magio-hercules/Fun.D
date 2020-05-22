package com.fund.iam.ui.main.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.databinding.FragmentHomeEditBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.fund.iam.ui.letter.LetterActivity;
import com.fund.iam.utils.RealPathUtil;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HomeEditFragment extends BaseFragment<FragmentHomeEditBinding, HomeViewModel> implements HomeNavigator, View.OnClickListener {

    public static final String TAG = HomeEditFragment.class.getSimpleName();


    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    private static final int REQUEST_IMAGE_CROP_AFTER = 5555;

    private final int GET_GALLERY_IMAGE = 200;

    private final int PORTFOLIPO_TITLE_ID  = 0x3000;
    private final int PORTFOLIPO_EDIT_ID  = 0x4000;
    private final int PORTFOLIPO_DELETE_ID  = 0x5000;
    private final int PORTFOLIPO_IMAGE_ID  = 0x6000;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;


    //    Spinner spinner;
    String[] spinnerValueLocation = {
            "서울",
            "수원",
            "인천",
            "하남"
    };

    String[] spinnerValueJob = {
            "개발자",
            "디자이너",
            "기획자",
    };

    String[] spinnerValueJobDetail = {
            "FrontEnd 개발자",
            "BackEnd 개발자",
            "Server 개발자",
            "UI/UX 디자이너",
            "웹 디자이너",
            "기획자",
    };

    String[] spinnerValueGender = {
            "남자",
            "여자",
    };

    // TODO : DB에 테이블로 추가될 정보
    String[] spinnerValueAge = {
            "10대",
            "20대",
            "30대",
            "40대",
            "50대",
            "60대",
    };


    ///////////////////
    // Home variable //

    //권한 설정 변수
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수
    private static final int MULTIPLE_PERMISSIONS = 101;

    private int portfolidIndex = 1;

    // 캡쳐 후 저장될 사진의 path
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI;

    // 프로필 이미지인지 체크
    boolean bProfile = false;

    // Portfolio 관리 정보 //
    // 추가될 신규 포트폴리오 (id, text or imageUrl)
    List<Map<Integer, String>> arrAddPortfolioText = new ArrayList<Map<Integer, String>>();
    //    List<Map<Integer, String>> arrAddPortfolioImage = new ArrayList<Map<Integer, String>>();
    List<Integer> arrAddPortfolioImage = new ArrayList<Integer>();
    // 삭제될 기존 포트폴리오 (id만 관리)
    List<Integer> arrDeletePortfolio = new ArrayList<Integer>();
    // 수정될 기존 TEXT 포트폴리오 (id, text)
    List<Map<Integer, String>> arrModifyPortfolio = new ArrayList<Map<Integer, String>>();

    ProgressDialog progressDialog;
    // API 호출 횟수를 count 하여 ProgressDialog 관리
    boolean bWaiting = false;
    int watingCount = 0;

    // 이미지 정보
    Bitmap mBitmapAvatar;

    // Home variable //
    ///////////////////


    public static HomeEditFragment newInstance() {
        Bundle args = new Bundle();
        HomeEditFragment fragment = new HomeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_edit;
    }

    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(HomeViewModel.class);
    }

    @Override
    public void goBack() {
//        getBaseActivity().onFragmentDetached(TAG);
        getViewDataBinding().profileEditCancel.performClick();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.i("onCreate");

        progressDialog = new ProgressDialog(getContext());
//        loadingStart();

        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) { e = e.getCause();
            }

            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }

            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                // return;
            }

            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }

            Log.e("RxJava_HOOK", "Undeliverable exception received, not sure what to do " + e.getMessage());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View is created so postpone the transition
//        postponeEnterTransition();

        checkPermissions();

        initSpinnerList();
        initViews();
        initHandlers();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addDataScheme("file");

        Log.e(TAG, "onViewCreated registerReceiver OK");
//        getContext().registerReceiver(mYourBroadcastReceiver, filter);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mYourBroadcastReceiver, filter);
    }

    @Override
    public void onClick(View view) {
        goBack();
    }

    @Override
    public void startLetterActivity() {
        LetterActivity.start(getActivity());
//        getActivity().finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Logger.e("handleError " + throwable.getMessage());
//        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addDataScheme("file");

//        Log.e(TAG, "registerReceiver OK");
//        getContext().registerReceiver(mYourBroadcastReceiver, filter);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mYourBroadcastReceiver, filter);
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        try {
//            Log.d(TAG, "onPause registerReceiver unregister OK");
//            getContext().unregisterReceiver(mYourBroadcastReceiver);
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mYourBroadcastReceiver);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "unregisterReceiver IllegalArgumentException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "unregisterReceiver Exception : " + e);
        }/*finally {
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mYourBroadcastReceiver);

        try {
            Log.d(TAG, "onDestroyView registerReceiver unregister OK");
//            getContext().unregisterReceiver(mYourBroadcastReceiver);
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mYourBroadcastReceiver);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "unregisterReceiver IllegalArgumentException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "unregisterReceiver Exception : " + e);
        }/*finally {
        }*/

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (resultCode != RESULT_OK) {
            Toast.makeText(getActivity(), "이미지가 선택되지 않습니다.", Toast.LENGTH_SHORT).show();
        }

        switch(requestCode)
        {
            case REQUEST_TAKE_ALBUM:
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
//                    Bitmap image = BitmapFactory.decodeStream(in);
                    Bitmap image = readImageWithSampling(in, 800, 600);
                    in.close();

                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                                getActivity().getContentResolver(),
                                                imageUri);

                    String imagePath = RealPathUtil.getRealPathFromURI_API19(getContext(), imageUri);
                    Log.i(TAG, "imagePath : " + imagePath);

                    // 이미지를 상황에 맞게 회전시킨다
                    ExifInterface exif = new ExifInterface(imagePath);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    Bitmap rotatedBitmap = rotateBitmap(bitmap, exifOrientation);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.i(TAG, "exifDegree : " + exifDegree);

//                    makeImageContents(1, image, null);
//                    addPortfolioImage(bitmap);
                    addPortfolioImage(rotatedBitmap);
                } catch (Exception e) {
                    bProfile = false;
                    e.printStackTrace();
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "REQUEST_IMAGE_CROP OK");

//                    galleryAddPic();

                    File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "IAM");
                    Log.i(TAG, "storageDir: " + storageDir.toString());
                    Log.i(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
                    Log.i(TAG, "imageUri.getPath(): " + imageUri.getPath());

                    MediaScannerConnection.scanFile(getContext(),
//                            new String[]{file.getAbsolutePath()},
                        new String[]{imageUri.getPath()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.v(TAG, "file:" + path + "was scanned success");

//                                    makeImageContents(3, null, contentUri);

//                                final Handler handler = new Handler();
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(TAG, "runOnUiThread");

                                                Toast.makeText(getContext(), "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();

                                                try {
                                                    File f = new File(mCurrentPhotoPath);
                                                    Uri imageUri = Uri.fromFile(f);
//                                                    Log.v(TAG, "call makeImageContents type 3");
//                                                    makeImageContents(3, null, selectedImg);

                                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                                            imageUri);

                                                    String imagePath = RealPathUtil.getRealPathFromURI_API19(getContext(), imageUri);
                                                    Log.i(TAG, "imagePath : " + imagePath);

                                                    // 이미지를 상황에 맞게 회전시킨다
                                                    ExifInterface exif = new ExifInterface(imagePath);
                                                    int exifOrientation = exif.getAttributeInt(
                                                            ExifInterface.TAG_ORIENTATION,
                                                            ExifInterface.ORIENTATION_NORMAL);
                                                    Bitmap rotatedBitmap = rotateBitmap(bitmap, exifOrientation);
                                                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                                                    Log.i(TAG, "exifDegree : " + exifDegree);

                                                    addPortfolioImage(rotatedBitmap);
                                                } catch (Exception e) {
                                                    bProfile = false;
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }, 0);
                            }
                        });
                }
                break;

            case REQUEST_IMAGE_CROP_AFTER:
                Log.i(TAG, "!!! REQUEST_IMAGE_CROP_AFTER");

                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "REQUEST_IMAGE_CROP_AFTER OK");
                    makeImageContents(3, null, imageUri);
                }
                break;

            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Log.i(TAG, "REQUEST_TAKE_PHOTO OK");
                        Log.i(TAG, "OK before CropImage");

                        //사진 자르기
                        cropImage();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Toast.makeText(getContext(), "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //아래는 권한 요청 Callback 함수입니다. PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다.
    // 아래에서는 !=를 사용했기에 권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult " + requestCode);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄운다
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(getContext(), "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
    }

    /////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViews() {
        Log.d(TAG, "initViews");

        updateUser();
        updatePortfolio();
    }

    private void initHandlers() {
        Log.d(TAG, "initHandlers");

        getViewDataBinding().profileEditSave.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "profile_edit_save onClick");

                handleSave(view);
//                handleUpload();
            }
        });

        getViewDataBinding().profileEditSave2.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "profile_edit_save2 onClick");
                getViewDataBinding().profileEditSave.performClick();
            }
        });

    }

    public void onSuccess() {
        watingCount--;

        Log.d(TAG, "onSuccess");
        Log.d(TAG, "watingCount is " + watingCount);

        if (bWaiting && watingCount == 0) {
//            getViewModel().handleUserInfo();

            // goBack으로 대체
//            getViewDataBinding().profileEditCancel.performClick();

            // type 1: 수정사항 반영 완료
//            loadingEnd(1);
        }
    }

    public void loadingStart() {
        bWaiting = false;
        watingCount = 0;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (progressDialog == null) {
                            Log.e(TAG, "progressDialog is null");
                        }
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("잠시만 기다려 주세요");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd(int type) {
        Log.d(TAG, "loadingEnd type : " + type);
        int delay;
        if (type == 0)
            delay = 300;
        else if (type == 1)
            delay = 300;
        else
            delay = 500;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        switch (type) {
                            case 0:
                                ;
                                break;
                            case 1:
                                Toast.makeText(getContext(), "수정사항이 반영되었습니다.", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "수정사항이 반영되었습니다.");
                                Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!");

                                break;
                            case 2:
                                Toast.makeText(getContext(), "변경사항이 없습니다.", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "변경사항이 없습니다.");
                                Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!");
                                break;
                            default:
                                break;
                        }
                    }
                }, delay);
    }

    // for test
//    private void handleUpload() {
//        Log.d(TAG, "handleUpload");
//
//        mBitmapAvatar = ((BitmapDrawable)getViewDataBinding().profileEditProfile.getDrawable()).getBitmap();
//        if (mBitmapAvatar == null) {
//            Log.d(TAG, "mBitmapAvatar is null");
//
//        } else {
//            Log.d(TAG, "mBitmapAvatar is not null");
//
//            uploadImage(mBitmapAvatar, -1);
//        }
//    }

    private ArrayList<Map<Integer, String>> dummy = new ArrayList<>();
    int newImageViewId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleSave(View view) {
        Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!");
        Log.d(TAG, "handleSave");

        loadingStart();
        int changeCount = 0;


        String[] locationList;
        locationList = dataManager.getLocations().stream()
                .map(item -> item.getName()).toArray(String[]::new);

        String[] jobList;
        jobList = (String[])dataManager.getJobs().stream()
                .map(item -> item.getName()).toArray(String[]::new);

        // STEP 0, 프로필 정보 update
        User userInfo = dataManager.getMyInfo();
        String _imageUrl = userInfo.getImageUrl();
        String _userName = getViewDataBinding().profileEditName.getText().toString();
        String _nickName = getViewDataBinding().profileEditNickname.getText().toString();
        String _email = getViewDataBinding().profileEditEmail.getText().toString();
        String _phone = getViewDataBinding().profileEditPhone.getText().toString();

        String _locationStr = getViewDataBinding().profileEditLocation.getText().toString();
        String _jobStr = getViewDataBinding().profileEditJob.getText().toString();
        String _location = "" + (Arrays.asList(locationList).indexOf(_locationStr) + 1);
        String _job = "" + (Arrays.asList(jobList).indexOf(_jobStr) + 1);

        String _genderStr = getViewDataBinding().profileEditGender.getText().toString();
        String _ageStr = getViewDataBinding().profileEditAge.getText().toString();
        int _gender = Arrays.asList(spinnerValueGender).indexOf(_genderStr);
        int _age = Arrays.asList(spinnerValueAge).indexOf(_ageStr);

        // 변경사항 확인
        if (userInfo.getUserName() != _userName || userInfo.getNickName() != _nickName
                || userInfo.getEmail() != _email || userInfo.getPhone()  != _phone
                || userInfo.getLocationList() != _location
                || userInfo.getJobList() !=  _job
                || userInfo.getGender() != _gender || userInfo.getAge() != _age) {

            if (bProfile) {
                ImageView profileImage = getViewDataBinding().profileEditProfile;

                Bitmap bitmap = ((BitmapDrawable)profileImage.getDrawable()).getBitmap();
                if (bitmap != null) {
                    Log.d(TAG, "Profile image bitmap is not null");

                    singlProfileUpload(bitmap)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(result -> {
                                String newUrl = result.toString();
                                Log.d(TAG, "newUrl : " + newUrl);

                                newUrl = result.body().toString();
                                Log.d(TAG, "singlProfileUpload success");
                                Log.d(TAG, "newUrl : " + newUrl);

                                User updateUserInfo = new User(userInfo.getId(), userInfo.getSnsType(), newUrl,
                                        _userName, _nickName, _email, _phone,
                                        _location, _job, _gender, _age);
                                Logger.d("업데이트 될 유저정보 : " + updateUserInfo);

                                getViewModel().handleUserUpdate(updateUserInfo);
                            }, onError -> Log.d(TAG, "test error " + onError));

                    watingCount++;
                    changeCount++;
                }
            } else {
                User updateUserInfo = new User(userInfo.getId(), userInfo.getSnsType(), _imageUrl,
                        _userName, _nickName, _email, _phone,
                        _location, _job, _gender, _age);
                Logger.d("업데이트 될 유저정보 : " + updateUserInfo);

                watingCount++;
                changeCount++;

                getViewModel().handleUserUpdate(updateUserInfo);
            }


        } else {
            Log.d(TAG, "유저 프로필은 변경사항 없음");
        }

        List<Portfolio> listPortfolio = dataManager.getMyPortfolios();

        // 전체 리스트 중 id가 없는 항목만 API를 요청하여 추가
        LinearLayout layout = getViewDataBinding().portfolioLayout;
        int count = getViewDataBinding().portfolioLayout.getChildCount();

        LinearLayout innerLayout = null;
        View v = null;
        ImageView imageView = null;
        String imageUrl = "";
        String prevUrl = "";

        EditText editView = null;
        String editText = "";
        String prevText = "";

        // 포트폴리오 layout에 남아있는 view 정보
        for(int i=0; i<count; i++) {
            innerLayout = (LinearLayout)layout.getChildAt(i);
            v = innerLayout.getChildAt(1);

            if (v instanceof ImageView) {
                imageView = (ImageView) v;
                Log.d(TAG, i + "번째 imageView url : " + imageView);

                if (i < listPortfolio.size()) {
                    prevUrl = listPortfolio.get(i).getImageUrl();
                    Log.d(TAG, i + "번째 prevUrl : " + prevUrl);
                }
            } else if (v instanceof EditText) {
                editView = (EditText) v;
                editText = editView.getText().toString();
                Log.d(TAG, i + "번째 editView editText : " + editText);

                if (i < listPortfolio.size()) {
                    prevText = listPortfolio.get(i).getText();
                    Log.d(TAG, i + "번째 prevText : " + prevText);

                    // 기존 포트폴리오 text 수정사항 체크
                    if (!editText.equals(prevText)) {
                        int _id = listPortfolio.get(i).getId();
                        Log.d(TAG, "기존 아이템 수정 id : " + _id
                                + ", 변경 전 : " + prevText + ", 변경 후 : " + editText);
                        Map<Integer, String> map = new HashMap<Integer, String>();
                        map.put(_id, editText);
                        arrModifyPortfolio.add(map);
                    }
                }
            }
        }

        Log.d(TAG, "arrAddPortfolioText : " + arrAddPortfolioText.toString());
        Log.d(TAG, "arrAddPortfolioImage : " + arrAddPortfolioImage.toString());
        Log.d(TAG, "arrDeletePortfolio : " + arrDeletePortfolio.toString());
        Log.d(TAG, "arrModifyPortfolio : " + arrModifyPortfolio.toString());

        int _id;
        String _text;

        // STEP 1, 기존 리스트 변경 사항 발생시 update (현재는 text type만 update 가능)
        for (Map<Integer, String> port : arrModifyPortfolio) {
            for (Map.Entry<Integer, String> entry : port.entrySet()) {
                _id = entry.getKey();
                _text = entry.getValue();
                Log.d(TAG, "_id : " + _id + ", _text : " + _text);

                watingCount++;
                changeCount++;
                Log.d(TAG, "updatePortfolio watingCount is " + watingCount);
                getViewModel().updatePortfolio(_id,
                        dataManager.getMyInfo().getId(), 1, _text);
            }
        }

        // STEP 2, 제거 리스트 삭제 요청 (type 무관)
        for (Portfolio port : listPortfolio) {
            _id = port.getId();
            if (arrDeletePortfolio.contains(_id)) {
                String value = port.getType() == 1 ? port.getText() : port.getImageUrl();
                Log.d(TAG, "삭제 요청 id : " + _id);
                Log.d(TAG, "type : " + port.getType() +
                        ", text or url : " + value);

                watingCount++;
                changeCount++;
                Log.d(TAG, "deletePortfolioText watingCount is " + watingCount);
                getViewModel().deletePortfolioText(port.getType(), port.getId(), value);
            }
        }

        // STEP 2, 신규 리스트 추가 요청 (type 구분 필요)
        changeCount += arrAddPortfolioText.size();

        final int checkAddCount = arrAddPortfolioText.size();
        final int checkChangeCount = changeCount;
        int index = 0;

        Observable.fromIterable(arrAddPortfolioText)
                .concatMapSingle(item -> {
                    Log.d(TAG, "item : " + item);

                    int addId = item.keySet().hashCode();
                    String addText = item.get(addId);
                    Log.d(TAG, "id : " + addId + ", text : " + addText);

                    watingCount++;
                    Log.d(TAG, "insertPortfolioText watingCount is " + watingCount);

                    // 비어있는 text 포트폴리오는 제외되었기 때문에 "" 으로 들어온 정보는 이미지로 판단할수 있음
                    if (addText == "") {
                        Log.d(TAG, "이미지 추가");

                        Bitmap bitmap = null;
                        ImageView newImageView = null;
                        newImageView = (ImageView) getViewDataBinding().portfolioLayout.findViewById(addId);
                        if (newImageView == null) {
                            Log.e(TAG, "newImageView is null");
                            return Single.error(new Throwable("test error"));
                        }

                        bitmap = ((BitmapDrawable)newImageView.getDrawable()).getBitmap();
                        if (bitmap != null) {
                            Log.d(TAG, "bitmap is not null");

                            int newImageViewId = newImageView.getId();
                            Log.d(TAG, "newImageViewId : " + newImageViewId);

                            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                            Date date = new Date();
                            String fileIndex = formatter.format(date) + newImageViewId;

                            Log.d(TAG, "fileIndex : " + fileIndex);
                            return singleImageUpload(bitmap, fileIndex);
                        } else {
                            Log.d(TAG, "bitmap is null");
                            return Single.error(new Throwable("test error"));
                        }
                    } else {
                        Log.d(TAG, "텍스트 추가");
//                        return getViewModel().singleInsertText(addText);
                        return getViewModel().singleInsertText(addText);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Log.d(TAG, "끝");
                    Log.d(TAG, result.toString());
                }, onError -> {
                    Log.d(TAG, "test error " + onError);

                }, () -> {
                    Log.d(TAG, "addCount is : "+ checkAddCount);
                    Log.d(TAG, "changeCount is " + checkChangeCount);

                    if (checkChangeCount == 0 && checkAddCount == 0) {
                        // type 2: 변경사항이 없습니다
                        loadingEnd(2);
                    } else {
                        // type 1: 수정사항이 반영되었습니다
                        loadingEnd(1);
                    }

                    Log.d(TAG, "getViewModel().handleUserInfo()");
                    getViewModel().handleUserInfo();
                });

        // STEP 2, 변경 전
//        Bitmap bitmap = null;
//        ImageView newImageView = null;
//        for (Map<Integer, String> port : arrAddPortfolioText) {
//            for (Map.Entry<Integer, String> entry : port.entrySet()) {
//                _id = entry.getKey();
//                _text = entry.getValue();
//                Log.d(TAG, "_key : " + _id + ", _text : " + _text);
//
//                watingCount++;
//                changeCount++;
//                Log.d(TAG, "insertPortfolioText watingCount is " + watingCount);
//
//                if (!_text.equals("")) { // 텍스트 추가
//                    Log.d(TAG, "ADD Portfolio text id : " + _id);
//
//                    getViewModel().insertPortfolioText(_text);
//                } else {
//                    newImageView = (ImageView) getViewDataBinding().portfolioLayout.findViewById(_id);
//                    if (newImageView == null) {
//                        Log.e(TAG, "newImageView is null");
//                        return;
//                    }
//                    bitmap = ((BitmapDrawable)newImageView.getDrawable()).getBitmap();
//                    if (bitmap != null) {
//                        Log.d(TAG, "bitmap is not null");
//
//                        int newImageViewId = newImageView.getId();
//                        Log.d(TAG, "newImageViewId : " + newImageViewId);
//
//                        Log.d(TAG, "ADD Portfolio image id : " + _id);
//                        uploadImage(bitmap, newImageViewId);
//                    } else {
//                        Log.d(TAG, "bitmap is null");
//                    }
//                }
//            }
//        }

        // STEP 2-1, 신규 리스트 추가 요청 (image), S3 API 구현 이후 제공 예정
//        Bitmap bitmap = null;
//        ImageView newImageView = null;
//        for (int _imageId : arrAddPortfolioImage) {
//            Log.d(TAG, "_imageId : " + _imageId);
//
//            watingCount++;
//            changeCount++;
//
//            // id에 해당하는 이미지 뷰 가져오기
////            bitmap = ((BitmapDrawable)getViewDataBinding().profileEditProfile.getDrawable()).getBitmap();
//
//
//            newImageView = (ImageView) getViewDataBinding().portfolioLayout.findViewById(_imageId);
//            if (newImageView == null) {
//                Log.d(TAG, "newImageView is null");
//                return;
//            }
//            bitmap = ((BitmapDrawable)newImageView.getDrawable()).getBitmap();
//            if (bitmap != null) {
//                Log.d(TAG, "bitmap is not null");
//
//                int newImageViewId = newImageView.getId();
//                Log.d(TAG, "newImageViewId : " + newImageViewId);
//
//                uploadImage(bitmap, newImageViewId);
//            } else {
//                Log.d(TAG, "bitmap is null");
//            }
//        }


        bWaiting = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initSpinnerList() {
//        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.spinner_item);
////        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        adapter.addAll(spinnerValueLocation);
//        adapter.add("거주지");
//
//
//        getViewDataBinding().profileEditLocation.setAdapter(adapter);
//        getViewDataBinding().profileEditLocation.setSelection(adapter.getCount());
//
//        getViewDataBinding().profileEditLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) { }
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                if (getViewDataBinding().profileEditLocation.getSelectedItem() == "거주지") {
////                    //Do nothing.
////                    Toast.makeText(getContext(), "이거슨 힌트", Toast.LENGTH_LONG).show();
////                } else {
////                    Toast.makeText(getContext(), getViewDataBinding().profileEditLocation.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
////                }
//            }
//        });

        String[] locationList;
        locationList = dataManager.getLocations().stream()
                .map(item -> item.getName()).toArray(String[]::new);

        String[] jobList;
        jobList = (String[])dataManager.getJobs().stream()
                .map(item -> item.getName()).toArray(String[]::new);

//        initSpinner(getViewDataBinding().profileEditLocation,
//                locationList,
//                "거주지");
        initAutoCompleteText(getViewDataBinding().profileEditLocation, locationList);

//        initSpinner(getViewDataBinding().profileEditJob,
//                jobList,
//                "직종");
        initAutoCompleteText(getViewDataBinding().profileEditJob, jobList);

//        initSpinner(getViewDataBinding().profileEditJobDetail,
//                spinnerValueJobDetail,
//                "세부직종");

//        initSpinner(getViewDataBinding().profileEditGender,
//                spinnerValueGender,
//                "성별");
        initAutoCompleteText(getViewDataBinding().profileEditGender, spinnerValueGender);

//        initSpinner(getViewDataBinding().profileEditAge,
//                spinnerValueAge,
//                "나이대");
        initAutoCompleteText(getViewDataBinding().profileEditAge, spinnerValueAge);
    }

    private void initSpinner(Spinner spinner, String[] valueList, String hint) {
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.spinner_item);
        adapter.addAll(valueList);
        adapter.add(hint);

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (spinner.getSelectedItem() == "거주지") {
//                    //Do nothing.
//                    Toast.makeText(getContext(), "이거슨 힌트", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//                }

            }
        });
    }

    private void initAutoCompleteText(AutoCompleteTextView autoCompleteTextView, String[] valueList) {
        Log.d(TAG, "initAutoCompleteText");

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, valueList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.layout_hint_box,
                                                    R.id.tvHintCompletion, valueList);
        autoCompleteTextView.setAdapter(adapter);
    }


    private void moveScroll(boolean bDown) {
        Log.d(TAG, "moveScroll");

        getViewDataBinding().profileScroll.post(new Runnable() {
            @Override
            public void run() {
                if (bDown) {
                    getViewDataBinding().profileScroll.fullScroll(ScrollView.FOCUS_DOWN);
                } else {
                    getViewDataBinding().profileScroll.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateUser() {
        Log.d(TAG, "updateUser");

//        User info = getViewModel().myInfo;
//        List<Job> jobList = getViewModel().listJob;
        User info = dataManager.getMyInfo();
        List<Job> jobList = dataManager.getJobs();

        Job findJob = jobList.stream()
                .filter(item -> info.getJobList() != null && item.getId() == Integer.parseInt(info.getJobList()))
                .findAny()
                .orElse(null);

        Log.d(TAG, "url : " + info.getImageUrl());
        Glide.with(getContext())
                .load(info.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile_default_2)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
                .into(getViewDataBinding().profileEditProfile);
//                .into(getViewDataBinding().profileImageProfile);

        getViewDataBinding().profileEditNickname.setText(info.getNickName());
        getViewDataBinding().profileEditName.setText(info.getUserName());
        getViewDataBinding().profileEditEmail.setText(info.getEmail());
        getViewDataBinding().profileEditPhone.setText(info.getPhone());

        AutoCompleteTextView acTextView = null;

        if (info.getLocationList() != null && Integer.parseInt(info.getLocationList()) > 0) {
            // DB id가 1부터 시작함
            acTextView = getViewDataBinding().profileEditLocation;
            acTextView.setText(acTextView.getAdapter().getItem(Integer.parseInt(info.getLocationList())-1).toString(), false);
        }
        if (info.getJobList() != null && Integer.parseInt(info.getJobList()) > 0) {
            // DB id가 1부터 시작함
            acTextView = getViewDataBinding().profileEditJob;
            acTextView.setText(acTextView.getAdapter().getItem(Integer.parseInt(info.getJobList())-1).toString(), false);
        }
        acTextView = getViewDataBinding().profileEditGender;
        acTextView.setText(acTextView.getAdapter().getItem(info.getGender()).toString(), false);
        acTextView = getViewDataBinding().profileEditAge;
        acTextView.setText(acTextView.getAdapter().getItem(info.getAge()).toString(), false);



//        List<PortfolioInfo> portfolioList = response.body();
//        mPortfolioList = portfolioList;
//        for (PortfolioInfo data: portfolioList) {
//            if (data.getUserId() == userId && data.getType() == 1) {
//                Log.d(TAG, "data.getId()" + data.getId() + "");
//                Log.d(TAG, "data.getText()" + data.getText());
//
//                addPortfolioText(data.getText());
//            }
//        }
        Log.d(TAG, "updateUser end");
    }

    public void updateBookmark(boolean flag) {
        Log.d(TAG, "updatePortfolio");

    }

    public void updatePortfolio() {
        Log.d(TAG, "updatePortfolio");

//        List<Portfolio> portfolioList = getViewModel().myPortfolio;
        List<Portfolio> portfolioList = dataManager.getMyPortfolios();

        for (Portfolio data: portfolioList) {
            switch (data.getType()) {
                case 1: // TEXT
                    addPortfolioText(data.getId(), data.getText());
                    break;
                case 2: // IMAGE
                    addPortfolioImage(data.getId(), data.getImageUrl());
                    break;
                default:
                    break;
            }
        }

//        loadingEnd(0);
        Log.d(TAG, "updatePortfolio end");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateUser(User info) {
        Log.d(TAG, "updateUser info");
    }

    public void updatePortfolio(List<Portfolio> portfolioList) {
        Log.d(TAG, "updatePortfolio info");
    }

    private void addPortfolioText(int id, String text) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_text_edit,
                getViewDataBinding().portfolioLayout,
                true);

        EditText _edit = (EditText) newLayout.findViewById(R.id.portfolioText_edit_text);
        _edit.setId(PORTFOLIPO_EDIT_ID + portfolidIndex);
        _edit.setText(text);

        // for edit mode - add
        _edit.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override // 입력이 끝났을 때
            public void afterTextChanged(Editable arg0) {
                String value = arg0.toString();

                Map<Integer, String> map = new HashMap<Integer, String>();
                if (id == -1) {
                    // 신규 포트폴리오
                    // 중복 key가 있는지 체크
                    boolean bFind = false;
                    for(Map<Integer, String> port: arrAddPortfolioText) {
                        for (Map.Entry<Integer, String> entry : port.entrySet()) {
                            if (entry.getKey() == PORTFOLIPO_EDIT_ID + portfolidIndex) {
//                                map.replace(PORTFOLIPO_EDIT_ID + portfolidIndex, value);
                                Log.d(TAG, "신규 아이템 동일key 존재, value 변경 editId : " + PORTFOLIPO_EDIT_ID + portfolidIndex);
                                entry.setValue(value);
                                bFind = true;
                                break;
                            }
                        }
                    }
                    if (!bFind) {
                        Log.d(TAG, "신규 아이템 추가 editId : " + PORTFOLIPO_EDIT_ID + portfolidIndex);
                        map.put(PORTFOLIPO_EDIT_ID + portfolidIndex, value);
                        arrAddPortfolioText.add(map);
                    }
                } else {
//                    // 기존 포트폴리오 수정사항은 handleSave에서 처리함
//                    Log.d(TAG, "기존 아이템 수정 id : " + id + ", string : " + value);
//                    map.put(id, value);
//                    arrModifyPortfolio.add(map);
                }
            }
            @Override // 입력하기 전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override // 입력되는 텍스트에 변화가 있을 때
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        // for edit mode - delete
        ImageView deleteButton = (ImageView) newLayout.findViewById(R.id.portfolioText_edit_delete);
        deleteButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "TEXT 삭제 버튼 클릭");

                if (id == -1) {
                    // 추가 아이템이면 arrAddPortfolioText에서 삭제
                    int editId = PORTFOLIPO_EDIT_ID + portfolidIndex;
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 신규 추가 아이템에서 제거");
                    arrAddPortfolioText.removeIf(data -> data.containsKey(editId));
                } else {
                    // 기존 아이템이면 addDeletePortfolio에 추가
                    // 기존 아이템이면 addModifyPortfolio에서 삭제
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 기존 아이템");
                    arrDeletePortfolio.add(id);
                    arrModifyPortfolio.removeIf(data -> data.containsKey(id));
                }
                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
            }
        });

        // 버튼을 찾기 위한 id
        portfolidIndex++;

        // 신규 아이템 추가 시 scroll 최하단으로 이동
        if (id == -1) {
            moveScroll(true);
        }
    }

    private void addPortfolioImage(int id, String url) {
        Log.d(TAG, "addPortfolioImage : " + url);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image_edit,
                getViewDataBinding().portfolioLayout,
                true);

        // inflate 이후에 버튼을 가져올수 있음
//        TextView imageTitle = (TextView) newLayout.findViewById(R.id.portfolioImage_title);
//        imageTitle.setId(PORTFOLIPO_TITLE_ID + portfolidIndex);
//        imageTitle.setText("#" + portfolidIndex);

        ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
//        imageImage.setImageResource(R.drawable.profile_default);  // imageView에 내용 추가
        imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);
        Log.d(TAG, "imageview size  (width: " + imageImage.getWidth());
        Log.d(TAG, "imageview size  (height: " + imageImage.getHeight());

        ImageView imageDelete = (ImageView) newLayout.findViewById(R.id.portfolioImage_delete);
        imageDelete.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        imageDelete.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
//                arrDeletePortfolio.add(id);
//                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());

                if (id == -1) {
                    // 추가 아이템이면 arrAddPortfolioImage에서 삭제
                    int editId = PORTFOLIPO_EDIT_ID + portfolidIndex;
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 신규 추가 아이템에서 제거");
//                    arrAddPortfolioImage.removeIf(data -> data == editId);
                    arrAddPortfolioText.removeIf(data -> data.containsKey(editId));
                } else {
                    // 기존 아이템이면 addDeletePortfolio에 추가
                    // 기존 아이템이면 addModifyPortfolio에서 삭제
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 기존 아이템");
                    arrDeletePortfolio.add(id);
                    // 이미지는 수정하기 기능이 없음
//                    arrModifyPortfolio.removeIf(data -> data.containsKey(id));
                }
                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
            }
        });

        Log.d(TAG, "url : " + url);
        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile_default_picture)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
                .into(imageImage);

        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    private void addPortfolioImage(Bitmap bitmap) {
        Log.d(TAG, "addPortfolioImage bitmap ");

        if (bProfile) {
            Glide.with(getContext())
                    .load(bitmap)
//                .placeholder(R.drawable.profile_default_picture)
//                .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .listener(requestListener)
                    .into(getViewDataBinding().profileEditProfile);
        } else {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image_edit,
                    getViewDataBinding().portfolioLayout,
                    true);

            ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
//        imageImage.setImageResource(R.drawable.profile_default);  // imageView에 내용 추가
            imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);
            Log.d(TAG, "imageview size  (width: " + imageImage.getWidth());
            Log.d(TAG, "imageview size  (height: " + imageImage.getHeight());

            Glide.with(getContext())
                    .load(bitmap)
//                .placeholder(R.drawable.profile_default_picture)
//                .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .listener(requestListener)
                    .into(imageImage);

            ImageView imageDelete = (ImageView) newLayout.findViewById(R.id.portfolioImage_delete);
            imageDelete.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
            imageDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "IMAGE 삭제 버튼 클릭");
                    getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
                }
            });

//        arrAddPortfolioImage.add(PORTFOLIPO_IMAGE_ID + portfolidIndex);
            Map<Integer, String> map = new HashMap<Integer, String>();
            map.put(PORTFOLIPO_IMAGE_ID + portfolidIndex, "");
            arrAddPortfolioText.add(map);

            // 버튼을 찾기 위한 id
            portfolidIndex++;

//        // 신규 아이템 추가 시 scroll 최하단으로 이동 -> listener로 이동
//        moveScroll(true);
        }
    }

    public void insertProfile() {
        bProfile = true;
        selectImage();
    }

    public void insertImage() {
        selectImage();
    }

    public void insertText() {

        addPortfolioText(-1, "");
    }




    ////////////////////
    // image 추가 기능 //
    ////////////////////


    private RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Log.e(TAG, "onLoadFailed");
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            Log.d(TAG, "onResourceReady");
            Log.d(TAG, "이미지 로딩 후 스크롤 이동");

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // 신규 아이템 추가 시 scroll 최하단으로 이동
                            moveScroll(true);
                        }
                    }, 500);

            return false;
        }
    };

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();

        switch(orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj,
                null, null, null);
        Log.d(TAG, "cursor.moveToFirst() : " + cursor.moveToFirst());
        if(cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            Log.d(TAG, "column_index : " + column_index);

        }

        Log.d(TAG, "proj[0] : " + proj[0]);
        Log.d(TAG, "cursor.getColumnIndex(proj[0] : " + cursor.getColumnIndex(proj[0]));

        String realPath = cursor.getString(column_index);
        Log.d(TAG, "realPath : " + realPath);
        cursor.close();
        return realPath;
    }


    public int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else {
            return 0;
        }
    }

    private boolean checkPermissions() {
        Log.d(TAG, "checkPermissions");

        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), pm);

            //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }

        //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
        if (!permissionList.isEmpty()) {
            Log.d(TAG, "permissionList : " + permissionList.toString());

            ActivityCompat.requestPermissions((Activity)getContext(),
                    permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            Log.d(TAG, "checkPermissions return false");
            return false;
        }

        Log.d(TAG, "checkPermissions return true & permissionList is empty");
        return true;
    }

    private void selectImage() {
        Log.d(TAG, "selectImage");

        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                bProfile = false;
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(getContext())
                .setTitle("업로드할 이미지 선택")
                .setNeutralButton("취소", cancelListener)
                .setPositiveButton("앨범선택", albumListener)
                .setNegativeButton("사진촬영", cameraListener)
                .show();
    }

    private void galleryAddPic(){
        Log.i(TAG, "galleryAddPic");

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);

        Log.i(TAG, "after galleryAddPic");
        Toast.makeText(getContext(), "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();


//        makeImageContents(3, null, imageUri);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction()
    {
        Log.d(TAG, "doTakeAlbumAction");

        // type2, ACTION_GET_CONTENT 이용 방법
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 여러 이미지를 선택하는 경우

//        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
        startActivityForResult(Intent.createChooser(intent, "앨범 선택"), REQUEST_TAKE_ALBUM);
    }


    /**
     * 카메라에서 촬영 후 이미지 가져오기
     */
    private void doTakePhotoAction()
    {
        Log.d(TAG, "doTakePhotoAction");

        /*
         * 참고 해볼곳
         * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
         * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
         * http://www.damonkohler.com/2009/02/android-recipes.html
         * http://www.firstclown.us/tag/android/
         */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("captureCamera Error", ex.toString());
        }

        if (photoFile != null) {
            // getUriForFile의 두 번째 인자는 Manifest provider의 authorites와 일치해야 함

            Uri providerURI = FileProvider.getUriForFile(getContext(),
                    "com.fund.iam.provider",
                    photoFile);
            imageUri = providerURI;

            // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!,
            // providerURI의 값에 카메라 데이터를 넣어 보냄
            intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "IAM");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.i("mCurrentPhotoPath", mCurrentPhotoPath.toString());

        return imageFile;
    }

    private void makeImageContents(int type, Bitmap bitmap, Uri uri) {
        Log.d(TAG, "makeImageContents (type: " + type + ", bitmap: " + bitmap + ", uri : " + uri);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image_edit,
                getViewDataBinding().portfolioLayout,
                true);

        ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
        Log.d(TAG, "imageview size  (width: " + imageImage.getWidth());
        Log.d(TAG, "imageview size  (height: " + imageImage.getHeight());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageImage.setLayoutParams(lp);
        imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);
        Log.d(TAG, "imageview size  (width: " + imageImage.getWidth());
        Log.d(TAG, "imageview size  (height: " + imageImage.getHeight());


        // type (1: image, 2: uri, 3: scaled)
        switch (type) {
            case 1:
                imageImage.setImageBitmap(bitmap);

                break;
            case 2:
                imageImage.setImageURI(uri);
                break;
            case 3:

                File imgFile = new File(uri.getPath());
                Log.d(TAG, "makeImageContents type3 uri.path(" + uri.getPath() + ")");
                Log.d(TAG, "makeImageContents type3 imgFile.getAbsolutePath()(" + imgFile.getAbsolutePath() + ")");


                Log.d(TAG, "imgFile path : " + imgFile.getAbsolutePath());
                Log.d(TAG, "imgFile exist : " + imgFile.exists());

//                setScaledPicture(imageImage, mCurrentPhotoPath);
                setScaledPicture(imageImage, imgFile.getAbsolutePath());

                break;
        }

        ImageView imageDelete = (ImageView) newLayout.findViewById(R.id.portfolioImage_delete);
        imageDelete.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        imageDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
            }
        });

        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }


    // 카메라 전용 크랍
    public void cropImage(){
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + " / imageUri : " + imageUri);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(imageUri, "image/*");
        cropIntent.putExtra("outputX", 800); // crop한 이미지의 x축 크기, 결과물의 크기
        cropIntent.putExtra("outputY", 600); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 4); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 3); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", imageUri); // 크랍된 이미지를 해당 경로에 저장

        getActivity().grantUriPermission( getActivity().getPackageName(),
                imageUri ,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List list = getActivity().getPackageManager().queryIntentActivities(cropIntent, 0);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ResolveInfo res = (ResolveInfo)list.get(0);

        getActivity().grantUriPermission(res.activityInfo.packageName, imageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent i = new Intent(cropIntent);
////                ResolveInfo res = list.get(0);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        getActivity().grantUriPermission(res.activityInfo.packageName, imageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));


        startActivityForResult(i, REQUEST_IMAGE_CROP);
    }

    public static Bitmap readImageWithSampling(InputStream in, int targetWidth, int targetHeight) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, bmOptions);

        int photoWidth  = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;
        Log.d(TAG, "readImageWithSampling size  (width: " + photoWidth);
        Log.d(TAG, "readImageWithSampling size  (height: " + photoHeight);

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);

        // Decode the image file into a Bitmap sized to fill the View
//        Bitmap.Config bmConfig; // 필요한 경우 param으로 받을것
//        bmOptions.inPreferredConfig = bmConfig;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap  image = BitmapFactory.decodeStream(in, null, bmOptions);
        Log.d(TAG, "readImageWithSampling size  (targetWidth: " + targetWidth);
        Log.d(TAG, "readImageWithSampling size  (targetHeight: " + targetHeight);

        return image;
    }

    private void setScaledPicture(ImageView imageView, String photoPath){
        Log.d(TAG, "setScaledPicture ");
        int targetW = imageView.getWidth(); // ImageView 의 가로 사이즈 구하기
        int targetH = imageView.getHeight(); // ImageView 의 세로 사이즈 구하기
        Log.d(TAG, "setScaledPicture target : " + targetW + targetH);
        targetW = 100;
        targetH = 100;

        // Bitmap 정보를 가져온다.
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth; // 사진의 가로 사이즈 구하기
        int photoH = bmOptions.outHeight; // 사진의 세로 사이즈 구하기
        Log.d(TAG, "setScaledPicture photo : " + photoW + photoH);

        // 사진을 줄이기 위한 비율 구하기
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void uploadImage(Bitmap bitmap, int viewId) {
        try {
            File filesDir = getContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            User userInfo = dataManager.getMyInfo();

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
            getViewModel().insertPortfolioImage(body, reqFileName);


//            Call<ResponseBody> req = apiService.postImage(body, name);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    if (response.code() == 200) {
//                        textView.setText("Uploaded Successfully!");
//                        textView.setTextColor(Color.BLUE);
//                    }
//
//                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("Uploaded Failed!");
//                    textView.setTextColor(Color.RED);
//                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                }
//            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Single<Response<String>> singlProfileUpload(Bitmap bitmap) {
        try {
            File filesDir = getContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            User userInfo = dataManager.getMyInfo();

            String fileName = userInfo.getEmail() + "_" + userInfo.getSnsType()
                    + "_profile.jpg";
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

            return getViewModel().singleImageUpload(body, reqFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Single.error(new Throwable("test error"));
    }

    private Single<Response<Void>> singleImageUpload(Bitmap bitmap, String fileIndex) {
        try {
            File filesDir = getContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            User userInfo = dataManager.getMyInfo();

            String fileName = userInfo.getEmail() + "_" + userInfo.getSnsType()
                    + "_" + fileIndex + ".jpg";
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

            return getViewModel().singleInsertImage(body, reqFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Single.error(new Throwable("test error"));
    }

    private final BroadcastReceiver mYourBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Broadcast received!");

            //Do what you want when the broadcast is received...

            String action = intent.getAction();
            Log.d(TAG, "intent action  " + action);

            if (action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)){
//                Toast.makeText(getActivity(), "ACTION_MEDIA_SCANNER_SCAN_FILE", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ACTION_MEDIA_SCANNER_SCAN_FILE");

                Uri uri = intent.getData();
                Log.d(TAG, "intent uri : " + uri);

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "runOnUiThread");

                                makeImageContents(3, null, uri);
                            }
                        });
                    }
                }).start();
            } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
                Log.d(TAG, "ACTION_MEDIA_SCANNER_STARTED");

            } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
                Log.d(TAG, "ACTION_MEDIA_SCANNER_SCAN_FILE");

                Uri uri = intent.getData();
                Log.d(TAG, "intent uri : " + uri);

                makeImageContents(3, null, uri);
            }
        }
    };

    static class CustomSubscriber<T> extends DisposableSubscriber<T> {
        @Override
        public void onNext(T t) {
//            System.out.println(Thread.currentThread().getName() + " onNext( " + t + " )");
            Log.d(TAG, "onNext");
        }

        @Override
        public void onError(Throwable t) {
//            System.out.println(Thread.currentThread().getName() + " onError( " + t + ")");
            Log.d(TAG, "onError");
        }

        @Override
        public void onComplete() {
//            System.out.println(Thread.currentThread().getName() + " onComplete()");
            Log.d(TAG, "onComplete");
        }
    }
}
