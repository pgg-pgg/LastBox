package com.example.pgg.qboxdemo.me;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pgg on 2018/5/14.
 */

public class AboutMeActivity extends BaseCommonActivity {

    private List<Fragment> mFragmentList;
    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager viewPager;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_about_me);
    }

    @Override
    public void initView() {
        initToolBar();
        initViewPager();
    }

    private void initViewPager() {
        mFragmentList=new ArrayList<>();
        mFragmentList.add(AboutQboxFragment.newInstance());
        mFragmentList.add(AboutStoryFragment.newInstance());
        mFragmentList.add(AboutMeFragment.newInstance());
        mSectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        viewPager=findViewById(R.id.container);
        viewPager.setAdapter(mSectionPagerAdapter);

        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mFragmentList.size()-1);
    }

    private void initToolBar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initPresenter() {

    }

    public class SectionPagerAdapter extends FragmentPagerAdapter{


        public SectionPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position) ;
        }

        @Override
        public int getCount() {
            return mFragmentList==null?0:mFragmentList.size();
        }
    }
}
