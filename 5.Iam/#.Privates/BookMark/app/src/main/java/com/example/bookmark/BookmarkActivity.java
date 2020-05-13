package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.bookmark.Adapter.BookmarkPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class BookmarkActivity extends AppCompatActivity {

    private Context context;

    private TabLayout tabLayout;

    private ViewPager viewPager;
    private BookmarkPagerAdapter bookmarkPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        context = getApplicationContext();

        tabLayout = (TabLayout) findViewById(R.id.tl_bookmark);

        viewPager = (ViewPager) findViewById(R.id.vp_bookmark);

        bookmarkPagerAdapter = new BookmarkPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(bookmarkPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
}
