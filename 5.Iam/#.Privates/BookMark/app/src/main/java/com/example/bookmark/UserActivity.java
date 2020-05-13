package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_addToFriend;
    private TextView tv_userName;
    private TextView tv_job;
    private TextView tv_userInformation;
    private TextView tv_sendNote;

    private ImageView iv_userBackground;
    private ImageView iv_bookmark;
    private ImageView iv_userInformationBackground;
    private ImageView iv_userProfile;
    private ImageView iv_sendNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


    }
}
