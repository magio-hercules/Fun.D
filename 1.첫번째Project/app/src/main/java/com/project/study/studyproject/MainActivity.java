package com.project.study.studyproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.project.study.studyproject.map.MapFragment;
import com.project.study.studyproject.util.TabPagerAdapter;

public class MainActivity extends AppCompatActivity
                              implements Memo1Fragment.OnFragmentInteractionListener,
                                         Memo2Fragment.OnFragmentInteractionListener,
                                         GalleryFragment.OnFragmentInteractionListener,
                                         MapFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayout 을 객체화, Tab 의 이름을 정의하고 정렬한다.
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Memo 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Memo 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ViewPager 를 객체화한다.
        viewPager = findViewById(R.id.pager);

        // Adapter 를 객체화한다.
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        // ViewPager 에 adapter 지정
        viewPager.setAdapter(tabPagerAdapter);

        // ViewPager 내부에 있는 페이지에 변화가 생겼을 때 호출된다.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
