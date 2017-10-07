package com.example.mine.mychatapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    public ViewPagerAdapter(FragmentManager fm,int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new AllSectonsFragment();
            case 1:
                return new PostsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
