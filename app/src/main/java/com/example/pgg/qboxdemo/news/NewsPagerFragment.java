package com.example.pgg.qboxdemo.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by pgg on 2018/5/5.
 */

public class NewsPagerFragment extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    public NewsPagerFragment(FragmentManager childFragmentManager, List<Fragment> fragments) {
        super(childFragmentManager);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments==null?0:fragments.size();
    }
}
