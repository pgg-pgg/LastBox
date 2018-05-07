package com.example.pgg.qboxdemo.test;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.WebViewActivity;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.database.FunctionDao;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.ChinaCalendar;
import com.example.pgg.qboxdemo.model.entities.Constellation;
import com.example.pgg.qboxdemo.model.entities.DayJoke;
import com.example.pgg.qboxdemo.model.entities.FindBg;
import com.example.pgg.qboxdemo.model.entities.FunctionBean;
import com.example.pgg.qboxdemo.module.find.ChinaCalendarActivity;
import com.example.pgg.qboxdemo.module.find.ConstellationActivity;
import com.example.pgg.qboxdemo.module.find.FindAdapter;
import com.example.pgg.qboxdemo.module.find.FindMoreActivity;
import com.example.pgg.qboxdemo.module.find.JokeActivity;
import com.example.pgg.qboxdemo.module.find.PinImageActivity;
import com.example.pgg.qboxdemo.module.find.QueryInfoActivity;
import com.example.pgg.qboxdemo.module.find.RefreshFindFragmentEvent;
import com.example.pgg.qboxdemo.network.NetWork;
import com.example.pgg.qboxdemo.utils.DateUtils;
import com.example.pgg.qboxdemo.utils.PixelUtil;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.widget.slidinglayout.SlidingLayout;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/4.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.bg_find_find)
    KenBurnsView bg_find_find;

    @BindView(R.id.web_layout)
    SlidingLayout web_layout;

    @BindView(R.id.Recycler_find)
    RecyclerView Recycler_find;

    @BindView(R.id.xiaohua_find)
    TextView xiaohua_find;

    @BindView(R.id.year_calendar)
    TextView year_calendar;

    @BindView(R.id.years_calendar)
    TextView years_calendar;

    @BindView(R.id.day_clendar)
    TextView day_clendar;

    @BindView(R.id.nongli_calendar)
    TextView nongli_calendar;

    @BindView(R.id.jieri_calendar)
    TextView jieri_calendar;

    @BindView(R.id.week_calendar)
    TextView week_calendar;

    @BindView(R.id.yi_calendar)
    TextView yi_calendar;

    @BindView(R.id.ji_calendar)
    TextView ji_calendar;

    @BindView(R.id.qfriend_star_find)
    TextView qfriend_star_find;

    @BindView(R.id.xz_star_find)
    TextView xz_star_find;

    @BindView(R.id.yunshi_star_find)
    TextView yunshi_star_find;

    @BindView(R.id.bg_title_find)
    TextView bg_title_find;

    @BindView(R.id.before_find)
    ImageButton before_find;

    @BindView(R.id.next_find)
    ImageButton next_find;

    @BindView(R.id.thefooter_find)
    LinearLayout thefooter_find;

    @BindView(R.id.star_find)
    LinearLayout mStarFind;

    @BindView(R.id.joke_find)
    LinearLayout mJokeFind;

    @BindView(R.id.wannianli_find)
    LinearLayout mWannianliFind;

    public static final String BG_BASE_URL = "http://www.bing.com";

    private String mParam1;
    private String mParam2;

    private int mBgFlag;
    public List<FindBg.ImagesBean> mImages;

    public Subscription mConstellationSubscription;
    public Subscription mDayJokeSubscribe;
    public Subscription mBgSubscription;
    public Subscription mChinaCalendarSubscription;

    public String mNowBgUrl;
    public String mNowBgName;

    private List<FunctionBean> mFindList;
    public FindAdapter mFindAdapter;
    public ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    public ItemTouchHelper mItemTouchHelper;


    public FindFragment() {

    }

    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, param1);
        bundle.putString(ARG_PARAM2, param2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mBgFlag = 0;
        initBg();
        initBottomContext();
        initRecy();
    }

    private void initRecy() {
        mFindList = initData();

        Recycler_find.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mFindAdapter = new FindAdapter(mFindList);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mFindAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(Recycler_find);

        //开启拖拽
        mFindAdapter.enableDragItem(mItemTouchHelper);
        mFindAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                FunctionDao dao = new FunctionDao(getContext().getApplicationContext());
                List<FunctionBean> data = mFindAdapter.getData();

                for (int i = 0; i < data.size(); i++) {
                    FunctionBean functionBean = data.get(i);
                    if (functionBean.getId() != i) {
                        functionBean.setId(i);
                        dao.updateFunctionBean(functionBean);
                    }
                }
            }
        });

        Recycler_find.setAdapter(mFindAdapter);
        Recycler_find.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                String name = ((FunctionBean) adapter.getData().get(position)).getName();
                itemActionEvent(name);
            }
        });
    }

    private void itemActionEvent(String name) {
        switch (name) {
            case "万年历":
                startActivity(new Intent(getContext(), ChinaCalendarActivity.class));
                break;
            case "快递查询":
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://m.kuaidi100.com/");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            case "黄金数据":
                notOpen();
                break;
            case "股票数据":
                notOpen();
                break;
            case "更多":
                startActivity(new Intent(getContext(), FindMoreActivity.class));
                break;
            case "身份证查询":
                Intent intent_idcard = new Intent(getContext(), QueryInfoActivity.class);
                intent_idcard.putExtra(QueryInfoActivity.QUERY_STYLE, QueryInfoActivity.QUERY_IDCARD);
                startActivity(intent_idcard);
                break;
            case "邮编查询":
                notOpen();
                break;
            case "手机归属地":
                Intent intent_tel = new Intent(getContext(), QueryInfoActivity.class);
                intent_tel.putExtra(QueryInfoActivity.QUERY_STYLE, QueryInfoActivity.QUERY_TEL);
                startActivity(intent_tel);
                break;
            case "QQ吉凶":
                Intent intent_qq = new Intent(getContext(), QueryInfoActivity.class);
                intent_qq.putExtra(QueryInfoActivity.QUERY_STYLE, QueryInfoActivity.QUERY_QQ);
                startActivity(intent_qq);
                break;
            case "星座运势":
                startActivity(new Intent(getContext(), ConstellationActivity.class));
                break;
            case "周公解梦":
                notOpen();
                break;
            case "汇率":
                notOpen();
                break;
            case "笑话大全":
                startActivity(new Intent(getContext(), JokeActivity.class));
                break;
            default:
                break;
        }
    }

    public void notOpen() {
        Toast.makeText(getContext(), "该功能现在暂时未开放！", Toast.LENGTH_SHORT).show();
    }

    private List<FunctionBean> initData() {
        FunctionDao dao = new FunctionDao(getContext().getApplicationContext());
        return dao.queryFunctionBeanListSmallNeed();
    }

    private void initBottomContext() {
        boolean starIsOpen = (boolean) SPUtils.get(getContext(), Constant.STAR_IS_OPEN, true);
        boolean jokeIsOpen = (boolean) SPUtils.get(getContext(), Constant.JOKE_IS_OPEN, true);
        boolean wannianliIsOpen = (boolean) SPUtils.get(getContext(), Constant.STUFF_IS_OPEN, true);

        if (!starIsOpen && !jokeIsOpen && !wannianliIsOpen) {
            thefooter_find.setVisibility(View.GONE);
            return;
        } else {
            if (thefooter_find.getVisibility() == View.GONE) {
                thefooter_find.setVisibility(View.VISIBLE);
            }
        }
        if (starIsOpen) {
            mStarFind.setVisibility(View.VISIBLE);
            String starName = (String) SPUtils.get(getContext(), Constant.USER_STAR, "水瓶座");
            xz_star_find.setText("-" + starName);
            mStarFind.setOnClickListener(this);
            requestStarData(starName);
        } else {
            mStarFind.setVisibility(View.GONE);
        }

        if (jokeIsOpen) {
            mJokeFind.setVisibility(View.VISIBLE);
            mJokeFind.setOnClickListener(this);
            requestJokeData();
        } else {
            mJokeFind.setVisibility(View.GONE);
        }

        if (wannianliIsOpen) {
            mWannianliFind.setVisibility(View.VISIBLE);
            mWannianliFind.setOnClickListener(this);
            requestWannianli();
        } else {
            mWannianliFind.setVisibility(View.GONE);
        }
    }

    private void initBg() {
        before_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImages != null) {
                    setBg(--mBgFlag);
                }
            }
        });
        next_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImages != null) {
                    setBg(++mBgFlag);
                }
            }
        });
        bg_title_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mNowBgUrl)) {
                    Intent intent = new Intent(getContext(), PinImageActivity.class);
                    intent.putExtra(PinImageActivity.IMG_NAME, TextUtils.isEmpty(mNowBgName) ? "" : mNowBgName);
                    intent.putExtra(PinImageActivity.IMG_URL, mNowBgUrl);

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), bg_find_find, getString(R.string.transition_pinchimageview));
                    ActivityCompat.startActivity(getContext(), intent, activityOptionsCompat.toBundle());
                }
            }
        });
        requestBg();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshNewsFragmentEvent(RefreshFindFragmentEvent event){
        if (event.getFlagBig()>0){
            initBottomContext();
        }
        if (event.getFlagSmall()>0){
            mFindAdapter.setNewData(initData());
        }
    }

    @Override
    protected void managerArguments() {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSimpleName()+"-"
                +this.getClass().getSimpleName();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.joke_find:
                startActivity(new Intent(getContext(), JokeActivity.class));
                break;
            case R.id.star_find:
                startActivity(new Intent(getContext(), ConstellationActivity.class));
                break;
            case R.id.wannianli_find:
                startActivity(new Intent(getContext(), ChinaCalendarActivity.class));
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        bg_find_find.resume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (bg_title_find.getFitsSystemWindows()==false){
                bg_title_find.setFitsSystemWindows(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    bg_title_find.requestApplyInsets();
                }}
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bg_find_find.pause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (bg_title_find != null) {
            if (hidden) {
                bg_title_find.setFitsSystemWindows(false);
            }else {
                bg_title_find.setFitsSystemWindows(true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                bg_title_find.requestApplyInsets();
            }
        }
        super.onHiddenChanged(hidden);
    }

    //==============================网络请求部分===============================

    /**
     * 星座运势
     *
     * @param starName 星座名称
     */
    private void requestStarData(String starName) {
        ubsubscribe("star");
        mConstellationSubscription = NetWork.getConstellationApi()
                .getConstellation(starName, "today", "35de7ea555a8b5d58ce2d7e4f8cb7c9f")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Constellation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(Constellation constellation) {
                        if (constellation.getError_code() == 0) {
                            showConstellation(constellation);
                        }
                    }
                });
    }

    private void showConstellation(Constellation constellation) {
        qfriend_star_find.setText(constellation.getQFriend());
        yunshi_star_find.setText(constellation.getSummary());
    }

    /**
     * 每日笑话
     */
    private void requestJokeData() {
        ubsubscribe("joke");
        mDayJokeSubscribe = NetWork.getDayJokeApi()
                .getDayJoke("39094c8b40b831b8e7b7a19a20654ed7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DayJoke>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("每日一笑" + e.getMessage());
                    }

                    @Override
                    public void onNext(DayJoke dayJoke) {
                        if (dayJoke.getError_code() == 0 && dayJoke.getResult() != null && dayJoke.getResult().getData().size() > 0) {
                            showDayJoke(dayJoke.getResult().getData().get(0));
                        }
                    }
                });
    }

    private void showDayJoke(DayJoke.ResultBean.DataBean dataBean) {
        String jokeContent = dataBean.getContent();
        if (!TextUtils.isEmpty(jokeContent)) {
            xiaohua_find.setText(jokeContent);
        }
    }

    /**
     * 背景图片与新闻
     */
    private void requestBg() {
        ubsubscribe("bg");
        mBgSubscription = NetWork.getFindBgApi()
                .getFindBg("js", 0, 8)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FindBg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FindBg findBg) {
                        if (findBg != null) {
                            mImages = findBg.getImages();
                        }
                        setBg(mBgFlag);
                    }
                });
    }

    private void setBg(int bgFlag) {
        if (bgFlag <= 0) {
            before_find.setEnabled(false);
            if (bgFlag == 0) {
                showBg(mImages.get(0));
            }
        } else if (bgFlag >= mImages.size() - 1) {
            next_find.setEnabled(false);
            if (bgFlag == mImages.size() - 1) {
                showBg(mImages.get(mImages.size() - 1));
            }
        } else {
            showBg(mImages.get(bgFlag));
            if (!before_find.isEnabled()) {
                before_find.setEnabled(true);
            }
            if (!next_find.isEnabled()) {
                next_find.setEnabled(true);
            }
        }
    }

    private void showBg(FindBg.ImagesBean imagesBean) {
        mNowBgUrl = BG_BASE_URL + imagesBean.getUrl();
        Glide.with(getContext())
                .load(mNowBgUrl)
                .override(PixelUtil.getWindowWidth(), PixelUtil.getWindowHeight())
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimaryDark)
                .into(bg_find_find);
        mNowBgName = imagesBean.getCopyright();
        bg_title_find.setText(mNowBgName);
    }

    /**
     * 万年历
     */
    private void requestWannianli() {
        String mDate = new StringBuffer()
                .append(DateUtils.getCurrYear()).append("-")
                .append(DateUtils.getCurrMonth()).append("-")
                .append(DateUtils.getCurrDay())
                .toString();
        ubsubscribe("chinacalendar");
        mChinaCalendarSubscription = NetWork.getsChinaCalendarApi()
                .getChinaCalendar("3f95b5d789fbc83f5d2f6d2479850e7e", mDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChinaCalendar>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ChinaCalendar chinaCalendar) {
                        if (chinaCalendar.getError_code() == 0) {
                            initDateView(chinaCalendar.getResult().getData());
                        } else {
                            yunshi_star_find.setText("请求数据失败");
                        }
                    }
                });
    }

    private void initDateView(ChinaCalendar.ResultBean.DataBean data) {
        jieri_calendar.setText(data.getHoliday() + "");
        nongli_calendar.setText("农历" + data.getLunar());
        year_calendar.setText(data.getYearmonth() + "");
        day_clendar.setText(data.getDate().split("-")[2] + "");
        years_calendar.setText(data.getAnimalsYear() + "." + data.getLunarYear());
        week_calendar.setText(data.getWeekday() + "");
        yi_calendar.setText(data.getSuit() + "");
        ji_calendar.setText(data.getAvoid() + "");
    }

    //=====================网络请求数据结束==================

    private void ubsubscribe(String star) {
        switch (star) {
            case "bg":
                if (mBgSubscription != null && !mBgSubscription.isUnsubscribed()) {
                    mBgSubscription.isUnsubscribed();
                }
                break;
            case "joke":
                if (mDayJokeSubscribe != null && !mDayJokeSubscribe.isUnsubscribed()) {
                    mDayJokeSubscribe.unsubscribe();
                }
                break;
            case "star":
                if (mConstellationSubscription != null && !mConstellationSubscription.isUnsubscribed()) {
                    mConstellationSubscription.unsubscribe();
                }
                break;
            case "chinacalendar":
                if (mChinaCalendarSubscription != null && mChinaCalendarSubscription.isUnsubscribed()) {
                    mChinaCalendarSubscription.unsubscribe();
                }
                break;
            default:
                break;
        }
    }
}
