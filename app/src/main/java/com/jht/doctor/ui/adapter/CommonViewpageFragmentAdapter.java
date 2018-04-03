package com.jht.doctor.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 通用的ViewpageFragment 的Adapter
 */
public class CommonViewpageFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[] mTitles;

    public CommonViewpageFragmentAdapter(FragmentManager fm, List<Fragment> mFragments, String[] title) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = title;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
