package com.leesc.tazza.ui.splash;

import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.databinding.ActivitySplashBinding;
import com.leesc.tazza.ui.base.BaseActivity;
import com.leesc.tazza.ui.lobby.LobbyActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    SplashViewModel splashViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        return splashViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel.setNavigator(this);
        Log.d("lsc","SplashActivity onCreate");
		
		//마시멜로우 이상 버전 권한 체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
	
    }

    @Override
    public void startLobbyActivity() {
        LobbyActivity.start(this);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void handleError(Throwable throwable) {

    }

	//마시멜로우 이상 버전 권한 체크
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "위치 권한 승인", Toast.LENGTH_LONG).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "위치 권한 거부.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "위치 권한 승인 부여 받지 못함", Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
	 	
		
		
}