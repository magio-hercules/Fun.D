package com.project.study.studyproject.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.study.studyproject.GalleryFragment;
import com.project.study.studyproject.Memo1Fragment;
import com.project.study.studyproject.Memo2Fragment;
import com.project.study.studyproject.map.MapFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                Memo1Fragment memo1Fragment = new Memo1Fragment();
                return memo1Fragment;
            case 1:
                Memo2Fragment memo2Fragment = new Memo2Fragment();
                return memo2Fragment;
            case 2:
                GalleryFragment galleryFragment = new GalleryFragment();
                return galleryFragment;
            case 3:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
