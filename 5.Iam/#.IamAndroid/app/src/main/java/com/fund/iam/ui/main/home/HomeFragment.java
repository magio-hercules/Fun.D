package com.fund.iam.ui.main.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.fund.iam.BR;
import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.Portfolio;
import com.fund.iam.data.model.User;
import com.fund.iam.databinding.FragmentHomeBinding;
import com.fund.iam.di.ViewModelProviderFactory;
import com.fund.iam.ui.base.BaseFragment;
import com.fund.iam.ui.letter.LetterActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements HomeNavigator, View.OnClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private final int PORTFOLIPO_TITLE_ID = 0x3000;
    private final int PORTFOLIPO_EDIT_ID = 0x4000;
    private final int PORTFOLIPO_DELETE_ID = 0x5000;
    private final int PORTFOLIPO_IMAGE_ID = 0x6000;

    // TODO : DB에 테이블로 추가될 정보
    String[] spinnerValueAge = {
            "10대",
            "20대",
            "30대",
            "40대",
            "50대",
            "60대",
    };

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;


    //

    private int portfolidIndex = 1;

    //

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
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

    /////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViews() {
        Log.d(TAG, "initViews");

        updateUser();
        updatePortfolio();
    }

    public void onSuccess() {
        Log.d(TAG, "onSuccess");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateUser() {
        Log.d(TAG, "updateUser");


        // Use filter to find all elements greater than 20.
//        IntStream result = Arrays.stream(values).filter(element -> element >= 20);

//        // Find first match.
//        OptionalInt firstMatch = result.findFirst();
//
//        // Print first matching int.
//        if (firstMatch.isPresent()) {
//            System.out.println("First: " + firstMatch.getAsInt());
//        }

//        User info = getViewModel().myInfo;
//        List<Job> jobList = getViewModel().listJob;
        User info = dataManager.getMyInfo();
        List<Job> jobList = dataManager.getJobs();
        List<Location> locationList = dataManager.getLocations();
        Logger.d(info);
        Logger.d(jobList);
        Logger.d(locationList);

        Job findJob = jobList.stream()
                .filter(item -> info.getJobList() != null && item.getId() == Integer.parseInt(info.getJobList()))
                .findAny()
                .orElse(null);


        Location findLocation = locationList.stream()
                .filter(item -> info.getLocationList() != null && item.getId() == Integer.parseInt(info.getLocationList()))
                .findAny()
                .orElse(null);

        Log.d(TAG, "url : " + info.getImageUrl());
        Glide.with(getContext())
                .load(info.getImageUrl())
                .placeholder(R.drawable.profile_default_2)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
//                .centerCrop()
                .into(getViewDataBinding().profileImageProfile);

        getViewDataBinding().profileName.setText(info.getUserName());
        getViewDataBinding().profileJob.setText(findJob != null ? findJob.getName() : "직업 없음");
        getViewDataBinding().profileAge.setText(spinnerValueAge[info.getAge()]);
        getViewDataBinding().profileLocation.setText(findLocation != null ? findLocation.getName() : "");

        getViewDataBinding().profileNick.setText(info.getNickName());
        getViewDataBinding().profilePhone.setText(info.getPhone());
        getViewDataBinding().profileEmail.setText(info.getEmail());
        getViewDataBinding().profileLocation2.setText(findLocation != null ? findLocation.getName() : "");
        getViewDataBinding().profileJob2.setText(findJob != null ? findJob.getName() : "직업 없음");
        getViewDataBinding().profileGender.setText(info.getGender() == 0 ? "남자" : "여자");
        getViewDataBinding().profileAge2.setText(spinnerValueAge[info.getAge()]);

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

    public void updatePortfolio() {
        Log.d(TAG, "updatePortfolio");

//        List<Portfolio> portfolioList = getViewModel().myPortfolio;
        List<Portfolio> portfolioList = dataManager.getMyPortfolios();
        Log.d(TAG, "updatePortfolio count : " + portfolioList.size()) ;

        for (Portfolio data : portfolioList) {
//            if (data.getUserId() != userInfo.getId()) {
//                Log.d(TAG, "data.getUserId() != userInfo.getId()");
//                return;
//            }

            switch (data.getType()) {
                case 1: // TEXT
                    addPortfolioText(data.getText());
                    break;
                case 2: // IMAGE
                    addPortfolioImage(data.getImageUrl());
                    break;
                default:
                    break;
            }
        }
    }

    private void addPortfolioText(String text) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_text,
                getViewDataBinding().portfolioLayout,
                true);

        TextView textEdit = (TextView) newLayout.findViewById(R.id.portfolioText_text);
        textEdit.setId(PORTFOLIPO_EDIT_ID + portfolidIndex);
        textEdit.setText(text);

        // for edit mode
//        ImageView textButton = (ImageView) newLayout.findViewById(R.id.portfolioText_delete);
//        textButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
//        textButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "TEXT 삭제 버튼 클릭");
//                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
//            }
//        });


        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }

    private void addPortfolioImage(String url) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.portfolio_image,
                getViewDataBinding().portfolioLayout,
                true);

//        //attachToRoot가 true로 설정되어 있으므로 필요 없음
//        layout.addView(linearLayout);

        // inflate 이후에 버튼을 가져올수 있음
        TextView imageTitle = (TextView) newLayout.findViewById(R.id.portfolioImage_title);
        imageTitle.setId(PORTFOLIPO_TITLE_ID + portfolidIndex);
        imageTitle.setText("#" + portfolidIndex);

        ImageView imageImage = (ImageView) newLayout.findViewById(R.id.portfolioImage_image);
//        imageImage.setImageResource(R.drawable.profile_default);  // imageView에 내용 추가
        imageImage.setId(PORTFOLIPO_IMAGE_ID + portfolidIndex);

        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.profile_default_picture)
//                .apply(RequestOptions.centerCropTransform())
                .fitCenter()
                .into(imageImage);

        // for edit 모드
//        Button textButton = (Button) newLayout.findViewById(R.id.portfolioImage_delete);
//        textButton.setId(PORTFOLIPO_DELETE_ID + portfolidIndex);
//        textButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "IMAGE 삭제 버튼 클릭");
//                getViewDataBinding().portfolioLayout.removeView((View) view.getParent());
//            }
//        });


        // 버튼을 찾기 위한 id
        portfolidIndex++;
    }


    public void insertImage() {
        Log.d(TAG, "insertImage");
    }

    public void insertText() {
        Log.d(TAG, "insertText");
    }
}
