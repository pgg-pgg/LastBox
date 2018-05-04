package com.example.pgg.qboxdemo.news;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.RefreshNewsFragmentEvent;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pgg on 2018/5/3.
 */

public class NewsFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.add_btn)
    ImageView mAddBtn;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.logo_news)
    ImageView mLogoNews;
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1,String param2){
        NewsFragment fragment=new NewsFragment();
        Bundle args=new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            if (mLogoNews.getFitsSystemWindows()==false){
                mLogoNews.setFitsSystemWindows(true);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
                    mLogoNews.requestApplyInsets();
                }
            }
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mLogoNews!=null){
            if (hidden){
                mLogoNews.setFitsSystemWindows(false);
            }else {
                mLogoNews.setFitsSystemWindows(true);
            }
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
                mLogoNews.requestApplyInsets();
            }
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
