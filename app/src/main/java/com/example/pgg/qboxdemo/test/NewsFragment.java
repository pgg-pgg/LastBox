package com.example.pgg.qboxdemo.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.database.CategoryDao;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.CategoryEntity;
import com.example.pgg.qboxdemo.model.entities.RefreshNewsFragmentEvent;
import com.example.pgg.qboxdemo.news.DefaultStyleFragment;
import com.example.pgg.qboxdemo.news.NewsPagerFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pgg on 2018/5/4.
 */

public class NewsFragment extends BaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magic_indicator;

    @BindView(R.id.add_btn)
    ImageView add_btn;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.logo_news)
    ImageView logo_news;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<CategoryEntity> mCategoryEntities;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(String mParam1, String mParam2) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, mParam1);
        bundle.putString(ARG_PARAM2, mParam2);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (logo_news.getFitsSystemWindows() == false) {
                logo_news.setFitsSystemWindows(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    logo_news.requestApplyInsets();
                }
            }
        }
    }

    @Override
    public void initView() {
        mCategoryEntities = getCategoryFromDB();
        if (mCategoryEntities==null){
            mCategoryEntities=new ArrayList<>();
        }
        List<Fragment> fragments=new ArrayList<>();
        for (int i=0;i<mCategoryEntities.size();i++){
            if (true){
                DefaultStyleFragment fragment=DefaultStyleFragment.newInstance(mCategoryEntities.get(i).getKey());
                fragments.add(fragment);
            }else {
                MeFragment meFragment=new MeFragment();
                fragments.add(meFragment);
            }
        }
        viewPager.setAdapter(new NewsPagerFragment(getChildFragmentManager(),fragments));
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator=new CommonNavigator(getContext());
        commonNavigator.setScrollPivotX(0.35f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mCategoryEntities==null?0:mCategoryEntities.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView=new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mCategoryEntities.get(index).getName());
                simplePagerTitleView.setNormalColor(Color.WHITE);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorAccent));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator=new WrapPagerIndicator(context);
                indicator.setFillColor(Color.WHITE);
                return indicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magic_indicator,viewPager);
    }

    private List<CategoryEntity> getCategoryFromDB() {
        CategoryDao categoryDao = new CategoryDao(getContext().getApplicationContext());
        return categoryDao.queryCategoryList();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (logo_news!=null){
            if (hidden){
                logo_news.setFitsSystemWindows(false);
            }else {
                logo_news.setFitsSystemWindows(true);
            }
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
            logo_news.requestApplyInsets();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void managerArguments() {
        mParam1=getArguments().getString(ARG_PARAM1);
        mParam2=getArguments().getString(ARG_PARAM2);
    }

    @OnClick(R.id.add_btn)
    public void onClick(){
        EventBus.getDefault().post(new RefreshNewsFragmentEvent(Constant.NEWSFRAGMENT_CATEGORYACTIVITY_REQUESTCODE));
    }

    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSimpleName() + "-"
                + this.getClass().getSimpleName();
    }
}
