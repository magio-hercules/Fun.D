package com.example.bookmark.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookmark.BookmarkFragment.ChannelFragment;
import com.example.bookmark.BookmarkFragment.UserFragment;

public class BookmarkPagerAdapter extends FragmentStatePagerAdapter {

    private int pageCount;

    public BookmarkPagerAdapter(FragmentManager fragmentManager, int pageCount) {
        super(fragmentManager);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChannelFragment channelFragment = new ChannelFragment();
                return channelFragment;

            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
