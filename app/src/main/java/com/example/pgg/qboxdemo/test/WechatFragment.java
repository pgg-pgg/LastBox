package com.example.pgg.qboxdemo.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.WechatItem;
import com.example.pgg.qboxdemo.network.NetWork;
import com.example.pgg.qboxdemo.utils.PixelUtil;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.wechat.WeChatDetailsActivity;
import com.example.pgg.qboxdemo.wechat.WechatItemAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/4.
 */

public class WechatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    //每页请求的item数量
    public final int mPs=21;
    public int mPageMark=1;
    public boolean mRefreshMark;

    @BindView(R.id.swiper_wechat)
    SwipeRefreshLayout swiper_wechat;

    @BindView(R.id.recycle_wechat)
    RecyclerView recycle_wechat;

    @BindView(R.id.floatingactionbutton_wechat)
    FloatingActionButton floatingactionbutton_wechat;

    private View notDataView;
    private View errorView;
    private String mParam1;
    private String mParam2;

    private List<WechatItem.ResultBean.ListBean> mListBeanList;
    private WechatItemAdapter mWechatItemAdapter;

    private Subscription mSubscription;

    public WechatFragment(){

    }

    public static WechatFragment newInstance(String mParam1,String mParam2){
        WechatFragment fragment=new WechatFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_PARAM1,mParam1);
        bundle.putString(ARG_PARAM2,mParam2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mWechatItemAdapter.setEnableLoadMore(false);
        mRefreshMark=true;
        mPageMark=1;
        requestData();
    }

    @Override
    public void onLoadMoreRequested() {
        swiper_wechat.setEnabled(false);
        requestData();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_wechat;
    }

    @Override
    public void initView() {
        //初始化右下角浮动按钮
        initFAB();
        //初始化下拉刷新
        initSwipeRefresh();
        //初始化加载空|错误的view
        initEmptyView();
        //初始化RecycleView
        initRecyclerWechat();
        //正在加载
        onLoading();
        //请求数据
        requestData();
    }

    private void requestData() {
        unsubscribe();
        mSubscription= NetWork.getWeChatApi()
                .getWechat("8",mPageMark,mPs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WechatItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError:" + e.getMessage());
                        onErrorView();
                        if (swiper_wechat.isRefreshing()){
                            swiper_wechat.setRefreshing(false);
                        }
                        if (swiper_wechat.isEnabled()){
                            swiper_wechat.setEnabled(false);
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
            mPageMark++;
            List<WechatItem.ResultBean.ListBean> newData=wechatItem.getResult().getList();
            WechatItem.ResultBean.ListBean listBean = newData.get(0);
            listBean.setItemType(1);
            listBean.setSpansize(2);
            if (mRefreshMark){
                mWechatItemAdapter.setNewData(newData);
                mRefreshMark=false;
            }else {
                mWechatItemAdapter.addData(newData);
            }
            if (swiper_wechat.isRefreshing()){
                swiper_wechat.setRefreshing(false);
            }
            if (!swiper_wechat.isEnabled()){
                swiper_wechat.setEnabled(true);
            }
            if (mWechatItemAdapter.isLoading()){
                mWechatItemAdapter.loadMoreComplete();
            }
            if (!mWechatItemAdapter.isLoadMoreEnable()){
                mWechatItemAdapter.setEnableLoadMore(true);
            }
        }else {
            if (mWechatItemAdapter.isLoading()) {
                Toast.makeText(getContext(), R.string.load_more_error, Toast.LENGTH_SHORT).show();
                mWechatItemAdapter.loadMoreFail();
            } else {
                mWechatItemAdapter.setEmptyView(notDataView);
                if (swiper_wechat.isRefreshing()) {
                    swiper_wechat.setRefreshing(false);
                }
                if (swiper_wechat.isEnabled()) {
                    swiper_wechat.setEnabled(false);
                }
            }
        }
    }

    private void onErrorView() {
        mWechatItemAdapter.setEmptyView(errorView);
    }

    private void unsubscribe() {
        if (mSubscription!=null&&mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    private void onLoading() {
        mWechatItemAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) recycle_wechat.getParent());
    }

    private void initEmptyView() {
/**
 * 网络请求失败没有数据
 */
        notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) recycle_wechat.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onRequestAgain();
            }
        });

        /**
         * 网络请求错误 | 没有网络
         */
        errorView = getActivity().getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) recycle_wechat.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onRequestAgain();
            }
        });
    }

    private void initRecyclerWechat() {
        recycle_wechat.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(),2));
        mListBeanList=new ArrayList<>();
        boolean isNotLoad= (boolean) SPUtils.get(getContext(), Constant.SLLMS,false);
        int imgWidth= PixelUtil.getWindowWidth();
        int imgHeight=imgWidth*3/4;
        mWechatItemAdapter=new WechatItemAdapter(mListBeanList,isNotLoad,imgWidth,imgHeight);
        mWechatItemAdapter.openLoadAnimation();
        mWechatItemAdapter.setOnLoadMoreListener(this);
        mWechatItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (position==0){
                    return 2;
                }else {
                    return mWechatItemAdapter.getData().get(position).getSpansize();
                }
            }
        });
        recycle_wechat.setAdapter(mWechatItemAdapter);
        recycle_wechat.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                WechatItem.ResultBean.ListBean listBean= (WechatItem.ResultBean.ListBean) adapter.getData().get(position);

                Intent intent=new Intent(getContext(), WeChatDetailsActivity.class);
                intent.putExtra("wechat",listBean);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.img_wechat_style), getString(R.string.transition_wechat_img));
                ActivityCompat.startActivity(getContext(),intent,activityOptionsCompat.toBundle());
            }
        });
    }

    private void initSwipeRefresh() {
        swiper_wechat.setOnRefreshListener(this);
        swiper_wechat.setColorSchemeColors(Color.rgb(47,223,189));
        swiper_wechat.setEnabled(false);
    }

    private void initFAB() {
        floatingactionbutton_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWechatItemAdapter!=null&&mWechatItemAdapter.getData()!=null&&mWechatItemAdapter.getData().size()>0){
                    recycle_wechat.scrollToPosition(0);
                }
            }
        });
    }

    @Override
    protected void managerArguments() {
        mParam1=getArguments().getString(ARG_PARAM1);
        mParam2=getArguments().getString(ARG_PARAM2);

    }

    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSimpleName() + "-"
                + this.getClass().getSimpleName();
    }
}
