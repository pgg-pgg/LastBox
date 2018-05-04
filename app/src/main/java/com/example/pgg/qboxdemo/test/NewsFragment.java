package com.example.pgg.qboxdemo.test;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;

import butterknife.BindView;

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

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }
}
