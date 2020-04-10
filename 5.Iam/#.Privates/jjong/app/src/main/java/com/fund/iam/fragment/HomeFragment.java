package com.fund.iam.fragment;

import android.Manifest;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.fund.iam.R;
import com.fund.iam.api.RetrofitService;
import com.fund.iam.model.PortfolioInfo;
import com.fund.iam.model.UserInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "[IAM][HOME]";

    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    private static final int REQUEST_IMAGE_CROP_AFTER = 5555;

    private final int GET_GALLERY_IMAGE = 200;

    private final int PORTFOLIPO_TITLE_ID  = 0x3000;
    private final int PORTFOLIPO_EDIT_ID  = 0x4000;
    private final int PORTFOLIPO_DELETE_ID  = 0x5000;
    private final int PORTFOLIPO_IMAGE_ID  = 0x6000;

    //권한 설정 변수
    private String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA};

    //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수
    private static final int MULTIPLE_PERMISSIONS = 101;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @BindView(R.id.profile_name)
    TextView text_name;
    @BindView(R.id.profile_job_age)
    TextView text_job_age;
    @BindView(R.id.profile_location)
    TextView text_location;
    @BindView(R.id.profile_phone)
    TextView text_phone;
    @BindView(R.id.profile_email)
    TextView text_email;
    @BindView(R.id.profile_gender)
    TextView text_gender;

    @BindView(R.id.button_add_text)
    Button buttonAddText;
    @BindView(R.id.button_add_image)
    Button buttonAddImage;
    @BindView(R.id.portfolio_layout)
    LinearLayout portfolioLayout;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //// MY
    private Context context = null;
    private int portfolidIndex = 1;


    // 캡쳐 후 저장될 사진의 path
    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI;

    Retrofit retrofit;
    RetrofitService retrofitService;

    int userId;
    List<UserInfo> mUserList;
    List<PortfolioInfo> mPortfolioList;

    //// end of MY


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        context = getActivity();

        checkPermissions();


        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        // for test
        userId = 1;

        getUserInfo();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addDataScheme("file");
        context.registerReceiver(mYourBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        try {
            context.unregisterReceiver(mYourBroadcastReceiver);
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
            context.unregisterReceiver(mYourBroadcastReceiver);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "unregisterReceiver IllegalArgumentException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "unregisterReceiver Exception : " + e);
        }/*finally {
        }*/

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

    @Override
    public void onAttach(Context _context) {
        super.onAttach(_context);

        context = _context;
        if (_context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) _context;
        } else {
            throw new RuntimeException(_context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Optional
    @OnClick({R.id.button_add_text, R.id.button_add_image, R.id.button_prev_user, R.id.button_next_user, R.id.profile_modify})
    public void onViewClicked(View view) {
        Log.d(TAG, view.toString());

        switch (view.getId()) {
            case R.id.button_add_text:
                Log.d(TAG, "텍스트 추가");
//                addText();
                addPortfolioText(null);
                break;
            case R.id.button_add_image:
                Log.d(TAG, "이미지 추가");
//                addImage();
//                addPortfolioImage();
                selectImage();
                break;
            case R.id.button_prev_user:
//                getUserList();
//                getUserPortfolio();

                setPrevUser();
                break;
            case R.id.button_next_user:
                setNextUser();
                break;
            case R.id.profile_modify:
                Toast.makeText(context, "수정하기 추가ㅏ 예정", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
    }




    private void addPortfolioText(String text) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_text,
                portfolioLayout,
                true);

//        //attachToRoot가 true로 설정되어 있으므로 필요 없음
//        layout.addView(linearLayout);

        // inflate 이후에 버튼을 가져올수 있음

        // #index
//        TextView textTitle = (TextView) newLayout.findViewById(R.id.portfolioText_title);
//        textTitle.setId(PORTFOLIPO_TITLE_ID + portfolidIndex);
//        textTitle.setText("#" + portfolidIndex);

        TextView textEdit = (TextView) newLayout.findViewById(R.id.portfolioText_edit);
        textEdit.setId(PORTFOLIPO_EDIT_ID + portfolidIndex);
        textEdit.setText(text);

        Button textButton = (Button) newLayout.findViewById(R.id.portfolioText_delete);
        textButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        textButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "TEXT 삭제 버튼 클릭");
                portfolioLayout.removeView((View) view.getParent());
            }
        });


        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    private void addPortfolioImage() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image,
                portfolioLayout,
                true);

//        //attachToRoot가 true로 설정되어 있으므로 필요 없음
//        layout.addView(linearLayout);

        // inflate 이후에 버튼을 가져올수 있음
        TextView imageTitle = (TextView) newLayout.findViewById(R.id.portfolioImage_title);
        imageTitle.setId(PORTFOLIPO_TITLE_ID + portfolidIndex);
        imageTitle.setText("#" + portfolidIndex);

        ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
        imageImage.setImageResource(R.drawable.profile_default);  // imageView에 내용 추가
        imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);

        Button textButton = (Button) newLayout.findViewById(R.id.portfolioImage_delete);
        textButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        textButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
                portfolioLayout.removeView((View) view.getParent());
            }
        });


        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    public void addText() {
        // textView가 추가될 linearLayout
//        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.portfolio_layout);

        // layout param 생성
        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */,
                        LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */,
                        1f /* layout_weight */);

        TextView tv = new TextView(getActivity());  // 새로 추가할 textView 생성
        tv.setText("이거슨 동적으로 만들어진 TextView");  // textView에 내용 추가
        tv.setLayoutParams(textParams);  // textView layout 설정
        tv.setGravity(Gravity.CENTER);  // textView layout 설정

        portfolioLayout.addView(tv); // 기존 linearLayout에 textView 추가

    }

    public void addImage() {
        // imageView가 추가될 linearLayout
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.portfolio_layout);

        // layout param 생성
        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);

        ImageView iv = new ImageView(getActivity());  // 새로 추가할 imageView 생성
        iv.setImageResource(R.drawable.profile_default);  // imageView에 내용 추가
        iv.setLayoutParams(imageParams);  // imageView layout 설정

        layout.addView(iv); // 기존 linearLayout에 imageView 추가
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

        new AlertDialog.Builder(context)
                .setTitle("업로드할 이미지 선택")
                .setNeutralButton("취소", cancelListener)
                .setPositiveButton("앨범선택", albumListener)
                .setNegativeButton("사진촬영", cameraListener)
                .show();
    }

    private void getUserInfo() {
        Log.d(TAG, "getUserInfo");

        retrofitService.getUserInfo(userId).enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserInfo>> call,
                                   @NonNull Response<List<UserInfo>> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "======================================");
                    Log.e(TAG, "response.isSuccessful getUserInfo 출력 시작");

                    List<UserInfo> userList = response.body();
                    mUserList = userList;
                    for (UserInfo data: userList) {
                        Log.d("data.getId()", data.getId() + "");
                        Log.d("data.getEmail()", data.getEmail());
                        Log.d("data.getUser_name()", data.getUser_name());
                        Log.d("data.getNick_name()", data.getNick_name());

                        if (data.getId() == userId) {
                            changeUIUserInfo(data);
                        }
                    }

                    getUserPortfolio();

                    Log.e(TAG, "response.isSuccessful getUserInfo 출력 끝");
                    Log.e(TAG, "======================================");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserInfo>> call, @NonNull Throwable t) {
                Log.d(TAG, "retrofitService.getUserInfo onFailure");

                Toast.makeText(getActivity(), "서버를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "end getUserInfo");
    }

    private void getUserPortfolio() {
        Log.d(TAG, "getUserPortfolio");

        retrofitService.getPortfolioList(userId).enqueue(new Callback<List<PortfolioInfo>>() {
            @Override
            public void onResponse(@NonNull Call<List<PortfolioInfo>> call,
                                   @NonNull Response<List<PortfolioInfo>> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "======================================");
                    Log.e(TAG, "response.isSuccessful getPortfolioList 출력 시작");

                    List<PortfolioInfo> portfolioList = response.body();
                    mPortfolioList = portfolioList;
                    for (PortfolioInfo data: portfolioList) {
                        if (data.getUserId() == userId && data.getType() == 1) {
                            Log.d(TAG, "data.getId()" + data.getId() + "");
                            Log.d(TAG, "data.getText()" + data.getText());

                            addPortfolioText(data.getText());
                        }
                    }

                    Log.e(TAG, "response.isSuccessful getPortfolioList 출력 끝");
                    Log.e(TAG, "======================================");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PortfolioInfo>> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "retrofitService.PortfolioInfo onFailure");

                Toast.makeText(getActivity(), "서버를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "end getUserPortfolio");
    }

    private void setPrevUser() {
        Log.d(TAG, "setPrevUser");

        if (userId > 1) {
            resetLayout();
            userId--;
            getUserInfo();
        }

        Log.d(TAG, "end setPrevUser");
    }

    private void setNextUser() {
        Log.d(TAG, "setNextUser");

        resetLayout();
        userId++;

        getUserInfo();

//        for (UserInfo data: mUserList) {
//            if (data.getId() == userId) {
//                Log.d("data.getId()", data.getId() + "");
//                Log.d("data.getEmail()", data.getEmail());
//                Log.d("data.getUser_name()", data.getUser_name());
//                Log.d("data.getNick_name()", data.getNick_name());
//
//                changeUIUserInfo(data);
//            }
//        }
//
//        for (PortfolioInfo data: mPortfolioList) {
//            if (data.getUserId() == userId && data.getType() == 1) {
//                Log.d(TAG, "data.getId()" + data.getId() + "");
//                Log.d(TAG, "data.getText()" + data.getText());
//
//                addPortfolioText(data.getText());
//            }
//        }

        Log.d(TAG, "end setNextUser");
    }

    private void resetLayout() {
        portfolioLayout.removeAllViews();
    }

    private void changeUIUserInfo(UserInfo data) {
        text_name.setText(data.getUser_name());
        text_job_age.setText(data.getNick_name());
        text_location.setText(data.getAge() + "세"); // TODO : location으로 변경 예정
        text_email.setText(data.getEmail());
        text_gender.setText(data.getGender() == 0 ? "남자" : "여자");
        text_phone.setText(data.getPhone());
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
                    Bitmap image = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
//                    imageView.setImageBitmap(img);
                    makeImageContents(1, image, null);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "REQUEST_IMAGE_CROP OK");

                    galleryAddPic();

                    File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "IAM");
                    Log.i(TAG, "storageDir: " + storageDir.toString());
                    Log.i(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
                    Log.i(TAG, "imageUri.getPath(): " + imageUri.getPath());

//                    if (!storageDir.exists()) {
//                        Log.i("mCurrentPhotoPath1", storageDir.toString());
//                        storageDir.mkdirs();
//                    }
//

//                    imageFile = new File(storageDir, imageFileName);
//                    mCurrentPhotoPath = imageFile.getAbsolutePath();

                    //앨범에 사진을 보여주기 위해 Scan을 합니다.
//                    MediaScannerConnection.scanFile(context,
//                            new String[]{imageUri.getPath()}, null,
//                            new MediaScannerConnection.OnScanCompletedListener() {
//                                public void onScanCompleted(String path, Uri uri) {
//                                    Log.i(TAG, "!!! onScanCompleted");
//
////                                    makeImageContents(3, null, imageUri);
//
//                                    Intent intent = new Intent(getActivity().getApplicationContext(),
//                                                MainActivity.class);
//                                    getActivity().setResult(RESULT_OK, intent);
//                                    startActivityForResult(intent, REQUEST_IMAGE_CROP_AFTER);
//                                }
//                            });
                }
                break;

            case REQUEST_IMAGE_CROP_AFTER:
                Log.i(TAG, "!!! REQUEST_IMAGE_CROP_AFTER");

                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "REQUEST_IMAGE_CROP_AFTER OK");
                    makeImageContents(3, null, imageUri);
                }
                break;

            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i(TAG, "REQUEST_TAKE_PHOTO OK");
                        Log.i(TAG, "OK before CropImage");

                        //사진 자르기
                        cropImage();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Toast.makeText(context, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentName);
    }





    //////////////////////////////////////






    private void makeImageContents(int type, Bitmap bitmap, Uri uri) {
        Log.d(TAG, "makeImageContents (type: " + type + ", bitmap: " + bitmap + ", uri : " + uri);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image,
                portfolioLayout,
                true);

        // inflate 이후에 버튼을 가져올수 있음
        TextView imageTitle = (TextView) newLayout.findViewById(R.id.portfolioImage_title);
        imageTitle.setId(PORTFOLIPO_TITLE_ID + portfolidIndex);
        imageTitle.setText("#" + portfolidIndex);

        ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
        imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);

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

        Button textButton = (Button) newLayout.findViewById(R.id.portfolioImage_delete);
        textButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
        textButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
                portfolioLayout.removeView((View) view.getParent());
            }
        });

        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    private boolean checkPermissions() {
        Log.d(TAG, "checkPermissions");

        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(context, pm);

            //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }

        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions((Activity)context, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            Log.d(TAG, "checkPermissions return false");
            return false;
        }

        Log.d(TAG, "checkPermissions return true");
        return true;
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
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
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
            // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

            Uri providerURI = FileProvider.getUriForFile(getContext(),
                    "com.fund.iam.fileprovider",
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

    private void galleryAddPic(){
        Log.i(TAG, "galleryAddPic");

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);

        Log.i(TAG, "after galleryAddPic");
        Toast.makeText(context, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();

//        makeImageContents(3, null, imageUri);
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
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
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
}
