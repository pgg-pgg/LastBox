package com.example.pgg.qboxdemo.test;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by pgg on 2018/5/4.
 */

public class WechatFragment extends BaseFragment {

    @BindView(R.id.swiper_wechat)
    SwipeRefreshLayout swiper_wechat;

    @BindView(R.id.recycle_wechat)
    RecyclerView recycle_wechat;

    @BindView(R.id.floatingactionbutton_wechat)
    FloatingActionButton floatingactionbutton_wechat;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_wechat;
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
