package com.example.pgg.qboxdemo.me.weather;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TimingLogger;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.me.weather.draw.DynamicWeather;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.utils.UiUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pgg on 2018/5/3.
 */

public class WeatherActivity extends FragmentActivity {

    public static Typeface typeface;
    public SimpleFragmentPagerAdapter mFragmentPagerAdapter;
    public List<BaseWeatherFragment> mFragmentList;

    public static Typeface getTypeface(Context context){
        return typeface;
    }

    private DynamicWeather weatherView;
    private ViewPager viewPager;
    AlphaAnimation alphaAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimingLogger logger=new TimingLogger("WeatherActivity","WeatherActivity onCreate");
        if (typeface==null){
            typeface=Typeface.createFromAsset(getAssets(),"fonts/mxx_font2.ttf");
        }
        logger.addSplit("Typeface.createFromAsset");
        setContentView(R.layout.activity_weather);
        logger.addSplit("setContentView");


        viewPager=findViewById(R.id.main_viewpager);
        if (Build.VERSION.SDK_INT>=19){
            viewPager.setPadding(0, UiUtil.getStatusBarHeight(),0,0);
        }
        alphaAnimation=new AlphaAnimation(0,1f);
        alphaAnimation.setDuration(260);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        viewPager.setAnimation(alphaAnimation);
        logger.addSplit("findViewPager");
        weatherView=findViewById(R.id.main_dynamicweatherview);
        logger.addSplit("findWeatherView");
        loadAreaToViewPager();
        logger.addSplit("loadAreaToViewPager");
        logger.dumpToLog();
    }

    /**
     * 获取显示天气的地区
     */
    private void loadAreaToViewPager() {
        final ArrayList<ApiManager.Area> selectdAreas=ApiManager.loadSelectedArea(this);
        final BaseWeatherFragment[] fragments=new BaseWeatherFragment[selectdAreas.size()];
        for (int i=0;i<selectdAreas.size();i++){
            final ApiManager.Area area=selectdAreas.get(i);
            fragments[i]=WeatherFragment.makeInstance(area,ApiManager.loadWeather(this,area.id));
        }

        mFragmentList= Arrays.asList(fragments);
        mFragmentPagerAdapter=new SimpleFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                weatherView.setDrawerType(((SimpleFragmentPagerAdapter)viewPager.getAdapter()).getItem(position).getDrawerType());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    /**
     * 更换天气元素
     */
    public void updateCurDrawerType() {
        weatherView.setDrawerType(((SimpleFragmentPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem()).getDrawerType());
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherView.onResume();
    }

    @Override
    protected void onPause() {
        weatherView.onPause();
        viewPager.clearAnimation();
        alphaAnimation.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        weatherView.onDestroy();
        super.onDestroy();
    }

    static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{

        private List<BaseWeatherFragment> fragments;

        @Override
        public BaseWeatherFragment getItem(int position) {
            BaseWeatherFragment fragment=fragments.get(position);
            fragment.setRetainInstance(true);//保存fragment
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return fragments==null?0:fragments.size();
        }

        public SimpleFragmentPagerAdapter(FragmentManager fm, List<BaseWeatherFragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((Fragment)object).getView());
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
