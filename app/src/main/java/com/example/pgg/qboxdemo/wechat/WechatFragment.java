package com.example.pgg.qboxdemo.wechat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by pgg on 2018/5/4.
 */

public class WechatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }

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
