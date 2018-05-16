package com.example.pgg.qboxdemo.module.find;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.module.find.joke.ImgJokeFragment;
import com.example.pgg.qboxdemo.module.find.joke.RefreshJokeStyleEvent;
import com.example.pgg.qboxdemo.module.find.joke.TextJokeFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by pgg on 2018/5/7.
 */

public class JokeActivity extends BaseCommonActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public int mJokestyle;
    private ViewPager mViewPager;
    private List<String> mTitleStrings;
    private List<Fragment> mFragmentList;


    public static final int JOKESTYLE_NEW = 1;
    public static final int JOKESTYLE_RANDOM = 2;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_joke);
    }

    @Override
    public void initView() {
        mJokestyle = JOKESTYLE_RANDOM;
        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        mTitleStrings = Arrays.asList("故事","趣图");
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitleStrings.size(); i++) {
            switch(mTitleStrings.get(i)){
                case "故事":
                    mFragmentList.add(i, TextJokeFragment.newInstance("",""));
                    break;
                case "趣图":
                    mFragmentList.add(i, ImgJokeFragment.newInstance("",""));
                    break;
                default:
                    break;
            }
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this,mFragmentList,mTitleStrings);

        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_joke, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==android.R.id.home){
            finish();
            return true;
        }

        if (id == R.id.action_new) {
            mJokestyle = JOKESTYLE_NEW;
            EventBus.getDefault().post(new RefreshJokeStyleEvent(mJokestyle));
            return true;
        }
        if (id == R.id.action_random) {
            mJokestyle = JOKESTYLE_RANDOM;
            EventBus.getDefault().post(new RefreshJokeStyleEvent(mJokestyle));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getJokestyle() {
        return mJokestyle;
    }
}
