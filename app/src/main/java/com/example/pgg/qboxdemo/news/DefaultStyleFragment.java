package com.example.pgg.qboxdemo.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.WechatItem;
import com.example.pgg.qboxdemo.network.NetWork;
import com.example.pgg.qboxdemo.news_details.NewsDetailsActivity;
import com.example.pgg.qboxdemo.utils.PixelUtil;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/5.
 */

public class DefaultStyleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    private static final String ARG_PARAM1="param1";

    private int mPage=1;
    private static final int TOTAL_COUNTER=30;

    private int mCurrentCounter;

    @BindView(R.id.news_list)
    RecyclerView news_list;
    @BindView(R.id.swiperrefresh)
    SwipeRefreshLayout swiperrefersh;
    BaseQuickAdapter baseQuickAdapter;
    private int delayMillis=1000;

    private String mParam1;

    public Subscription mSubscription;
    private View notDataView;
    private View errorView;
    private List<WechatItem.ResultBean.ListBean> mNewsItemList;

    public static DefaultStyleFragment newInstance(String mParam1){
        DefaultStyleFragment fragment=new DefaultStyleFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_PARAM1,mParam1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh() {
        baseQuickAdapter.setEnableLoadMore(false);
        onRequestAgain();
    }

    @Override
    public void onLoadMoreRequested() {
        swiperrefersh.setEnabled(false);
        news_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter>=TOTAL_COUNTER){
                    baseQuickAdapter.loadMoreEnd();
                }else {
                    baseQuickAdapter.addData(mNewsItemList);
                    mCurrentCounter=baseQuickAdapter.getData().size();
                    baseQuickAdapter.loadMoreComplete();//加载完成
                }
                swiperrefersh.setEnabled(true);
            }
        },delayMillis);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_default_style;
    }

    @Override
    public void initView() {
        //初始化下拉刷新
        initSwipeRefresh();
        //初始化内容为空的页面
        initEmptyView();
        //初始化RecycleView
        initRecycleView();
        //开始加载数据
        onLoading();
        //请求数据
        requestNews();
    }

    private void requestNews() {
        unsubscribe();
        mSubscription= NetWork.getWeChatApi()
                .getWechat(mParam1,mPage,TOTAL_COUNTER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WechatItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("OnError"+e.getMessage());
                        //显示错误界面
                        onErrorView();
                        if (swiperrefersh.isRefreshing()){
                            swiperrefersh.setRefreshing(false);
                        }
                        if (swiperrefersh.isEnabled()){
                            swiperrefersh.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNext(WechatItem wechatItem) {
                        setNewDataAddList(wechatItem);
                    }
                });
    }

    private void setNewDataAddList(WechatItem wechatItem) {
        if (wechatItem!=null&&"200".equals(wechatItem.getRetCode())){
            baseQuickAdapter.setNewData(wechatItem.getResult().getList());
            if (swiperrefersh.isRefreshing()){
                swiperrefersh.setRefreshing(false);
            }
            if (!swiperrefersh.isEnabled()){
                swiperrefersh.setEnabled(true);
            }
            if (!baseQuickAdapter.isLoadMoreEnable()){
                baseQuickAdapter.setEnableLoadMore(true);
            }
        }else {
            baseQuickAdapter.setEmptyView(notDataView);
            if (swiperrefersh.isRefreshing()){
                swiperrefersh.setRefreshing(false);
            }
        }
    }

    private void onErrorView() {
        baseQuickAdapter.setEmptyView(errorView);
    }

    private void unsubscribe() {
        if (mSubscription!=null&&!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    private void onLoading() {
        baseQuickAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) news_list.getParent());
    }

    private void initRecycleView() {
        news_list.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        mNewsItemList=new ArrayList<>();
        boolean isNotLoad= (boolean) SPUtils.get(getContext(), Constant.SLLMS,false);
        int imgWidth=(PixelUtil.getWindowWidth()-PixelUtil.dp2px(40))/3;
        int imgheight=imgWidth/4*3;
        baseQuickAdapter=new DefaultStyleItemAdapter(R.layout.news_item_default,isNotLoad,imgWidth,imgheight);
        baseQuickAdapter.openLoadAnimation();
        baseQuickAdapter.setOnLoadMoreListener(this);
        news_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<WechatItem.ResultBean.ListBean> data = adapter.getData();
                Bundle bundle=new Bundle();
                bundle.putString("url",data.get(position).getSourceUrl());
                bundle.putString("title",data.get(position).getTitle());
                Intent intent=new Intent(getContext(),NewsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        news_list.setAdapter(baseQuickAdapter);
        mCurrentCounter=baseQuickAdapter.getData().size();
    }

    private void initEmptyView() {
        /**
         * 网络请求失败没有数据
         */
        notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) news_list.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestAgain();
            }
        });

        /**
         * 网络请求错误|没有网络
         */
        errorView = getActivity().getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) news_list.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestAgain();
            }
        });

    }

    private void onRequestAgain() {
        mPage++;
        requestNews();
    }

    private void initSwipeRefresh() {
        swiperrefersh.setOnRefreshListener(this);
        swiperrefersh.setColorSchemeColors(47,223,189);
        swiperrefersh.setEnabled(false);
    }

    @Override
    protected void managerArguments() {
        mParam1 = getArguments().getString(ARG_PARAM1);
    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }
}
