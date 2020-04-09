package com.fund.iam.ui.main.home;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
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
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

    String[] spinnerValueAge = {
            "10대",
            "20대",
            "30대",
            "40대",
            "50대",
            "60대",
    };


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

    // Portfolio 관리 정보 //
    // 추가될 신규 포트폴리오 (type, text or imageUrl)
    List<Map<Integer, String>> arrAddPortfolioText = new ArrayList<Map<Integer, String>>();
    List<Map<Integer, String>> arrAddPortfolioImage = new ArrayList<Map<Integer, String>>();
    // 삭제될 기존 포트폴리오
    List<Integer> arrDeletePortfolio = new ArrayList<Integer>();


    // end of Home variable //


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
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.i("onCreate");
        getViewModel().setNavigator(this);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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
//            Log.e(TAG, "onPause registerReceiver unregister OK");
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
            Log.e(TAG, "onDestroyView registerReceiver unregister OK");
//            getContext().unregisterReceiver(mYourBroadcastReceiver);
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mYourBroadcastReceiver);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "unregisterReceiver IllegalArgumentException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "unregisterReceiver Exception : " + e);
        }/*finally {
        }*/

    }

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

                    Uri selectedimg = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedimg);

//                    makeImageContents(1, image, null);
                    addPortfolioImage(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "REQUEST_IMAGE_CROP OK");

                    galleryAddPic();

                    File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "IAM");
                    Log.i(TAG, "storageDir: " + storageDir.toString());
                    Log.i(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
                    Log.i(TAG, "imageUri.getPath(): " + imageUri.getPath());
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

    //아래는 권한 요청 Callback 함수입니다. PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다. 아래에서는 !=를 사용했기에
//권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
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


    private void initViews() {
        Log.d(TAG, "initViews");

//        getViewModel().getJobList();

        // TODO : 필요시 edit용 함수를 만들예정
        getViewModel().getUserInfo();
        getViewModel().getUserPortfolio();


    }

    private void initHandlers() {
        Log.d(TAG, "initHandlers");

        getViewDataBinding().profileEditSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "profile_edit_save onClick");

                handleSave(view);
            }
        });

    }

    private void handleSave(View view) {
        Log.d(TAG, "handleSave");

        List<Portfolio> listPortfolio = getViewModel().myPortfolio;

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
        for(int i=0; i<count; i++) {
            innerLayout = (LinearLayout)layout.getChildAt(i);
            v = innerLayout.getChildAt(1);

            // 신규 아이템


            if (v instanceof ImageView) {
                imageView = (ImageView) v;
                Log.d(TAG, i + "번째 imageView url : " + imageView);

                // TODO 이미지가 변경된지는 어떻게 알지? -> 별도의 arr로 관리하자 (arrAdd, arrDelete)

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
                }
            }
        }

        Log.d(TAG, "arrAddPortfolioText : " + arrAddPortfolioText.toString());
        Log.d(TAG, "arrAddPortfolioImage : " + arrAddPortfolioImage.toString());
        Log.d(TAG, "arrDeletePortfolio : " + arrDeletePortfolio.toString());

        // TODO userId 가져오기
        int userId = 1;

        // 서버에 신규 리스트 추가 요청 (type 구분)

        int _key;
        String _text;
        for (Map<Integer, String> port : arrAddPortfolioText) {
//            Map.Entry<String, Object> entry = port.entrySet();
            for (Map.Entry<Integer, String> entry : port.entrySet()) {
                _key = entry.getKey();
                _text = entry.getValue();
                Log.d(TAG, "_key : " + _key + ", _text : " + _text);

                getViewModel().insertPortfolioText(_text);
            }

//            if (arrAddPortfolioText.contains(_id)) {
//                Log.d(TAG, "추가 요청 id : " + _id);
//                Log.d(TAG, "type : " + port.getType() +
//                        ", text or url : " + (port.getType() == 1 ? port.getText() : port.getImageUrl()));
//
//                insertPortfolioText(text);
//            }
        }


        // 서버에 제거 리스트 삭제 요청
        int _id;
        for (Portfolio port : listPortfolio) {
            _id = port.getId();
            if (arrDeletePortfolio.contains(_id)) {
                Log.d(TAG, "삭제 요청 id : " + _id);
                Log.d(TAG, "type : " + port.getType() +
                ", text or url : " + (port.getType() == 1 ? port.getText() : port.getImageUrl()));

                getViewModel().deletePortfolioText(port.getId());
            }
        }

    }

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

        initSpinner(getViewDataBinding().profileEditLocation,
                    spinnerValueLocation,
                "거주지");

        initSpinner(getViewDataBinding().profileEditJob,
                spinnerValueJob,
                "직종");

        initSpinner(getViewDataBinding().profileEditJobDetail,
                spinnerValueJobDetail,
                "세부직종");

        initSpinner(getViewDataBinding().profileEditGender,
                spinnerValueGender,
                "성별");

        initSpinner(getViewDataBinding().profileEditAge,
                spinnerValueAge,
                "나이대");
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateUser() {
        Log.d(TAG, "updateUI");

        User info = getViewModel().myInfo;
        List<Job> jobList = getViewModel().listJob;

        Job findJob = jobList.stream()
                .filter(item -> item.getId() == Integer.parseInt(info.getJobList()))
                .findAny()
                .orElse(null);


        Log.d(TAG, "url : " + info.getImageUrl());
        Glide.with(getContext())
                .load(info.getImageUrl())
                .placeholder(R.drawable.profile_default_2)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
                .into(getViewDataBinding().profileEditProfile);

        getViewDataBinding().profileEditNickname.setText(info.getNickName());
        getViewDataBinding().profileEditName.setText(info.getUserName());
        getViewDataBinding().profileEditEmail.setText(info.getEmail());
//        getViewDataBinding().profileEditJob.setText(findJob != null ? findJob.getName() : "직업 없음");
//        getViewDataBinding().profileEditLocation.setText();
//        getViewDataBinding().profileEditPhone.setText(info.getPhone());
//        getViewDataBinding().profileEditGender.setText(info.getGender() == 0 ? "남자" : "여자");

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

    }

    public void updatePortfolio() {
        Log.d(TAG, "updatePortfolio");

        List<Portfolio> portfolioList = getViewModel().myPortfolio;
//        User userInfo = getViewModel().myInfo;

        for (Portfolio data: portfolioList) {
//            if (data.getUserId() != userInfo.getId()) {
//                Log.d(TAG, "data.getUserId() != userInfo.getId()");
//                return;
//            }

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
    }

    private void addPortfolioText(int id, String text) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_text_edit,
                getViewDataBinding().portfolioLayout,
                true);

        EditText _edit = (EditText) newLayout.findViewById(R.id.portfolioText_edit_text);
        _edit.setId(PORTFOLIPO_EDIT_ID + portfolidIndex);
        _edit.setText(text);
        _edit.addTextChangedListener(new TextWatcher() {
            @Override // 입력되는 텍스트에 변화가 있을 때
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override // 입력이 끝났을 때
            public void afterTextChanged(Editable arg0) {
                String value = arg0.toString();

                // 신규 포트폴리오
                Map<Integer, String> map = new HashMap<Integer, String>();
                if (id == -1) {
//                    for (Map<Integer, String> port : arrAddPortfolioText) {
////            Map.Entry<String, Object> entry = port.entrySet();
//                        for (Map.Entry<Integer, String> entry : port.entrySet()) {
//                            _key = entry.getKey();
//                            _text = entry.getValue();


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
                }
            }

            @Override // 입력하기 전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        // for edit mode
        ImageView deleteButton = (ImageView) newLayout.findViewById(R.id.portfolioText_edit_delete);
        deleteButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "TEXT 삭제 버튼 클릭");

                // 신규 추가 아이템이면 arrAddPortfolioText에서 삭제
                // 기존 아이템이면 addDeletePortfolio에 추가
                if (id == -1) {
                    int editId = PORTFOLIPO_EDIT_ID + portfolidIndex;
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 신규 추가 아이템");
                    arrAddPortfolioText.removeIf(data -> data.containsKey(editId));
                } else {
                    Log.d(TAG, "TEXT 삭제 버튼 클릭 - 기존 아이템");
                    arrDeletePortfolio.add(id);
                }
                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
            }
        });

        // 버튼을 찾기 위한 id
        portfolidIndex++;
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
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
                arrDeletePortfolio.add(id);
                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
            }
        });

        Log.d(TAG, "url : " + url);
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.profile_default_picture)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
                .into(imageImage);

        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    private void addPortfolioImage(Bitmap bitmap) {
        Log.d(TAG, "addPortfolioImage bitmap ");

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
                .fitCenter()
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


        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    public void insertImage() {

        selectImage();
    }

    public void insertText() {

        addPortfolioText(-1, "");
    }




    ////////////////////
    // image 추가 기능
    ////////////////////




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

        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions((Activity)getContext(), permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            Log.d(TAG, "checkPermissions return false");
            return false;
        }

        Log.d(TAG, "checkPermissions return true");
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
        cropIntent.putExtra("outputX", 100); // crop한 이미지의 x축 크기, 결과물의 크기
        cropIntent.putExtra("outputY", 100); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
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

}
