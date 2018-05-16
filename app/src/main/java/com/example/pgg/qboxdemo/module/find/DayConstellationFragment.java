package com.example.pgg.qboxdemo.module.find;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.model.constellaction.DayConstellation;
import com.example.pgg.qboxdemo.network.NetWork;
import com.example.pgg.qboxdemo.widget.MapTextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/16.
 */

public class DayConstellationFragment extends BaseFragment{
    private static final String CONSTELLATION_FLAG = "CONSTELLATION_FLAG";
    private static final String DAY_FLAG = "DAY_FLAG";
    public static final String DAY_TODAY = "today";
    public static final String DAY_TOMORROW = "tomorrow";

    @BindView(R.id.radarchart_day_star)
    RadarChart mChart;

    @BindView(R.id.summary_day_star)
    TextView mSummaryDayStar;
    @BindView(R.id.luckycolor_day_star)
    MapTextView mLuckyColor;
    @BindView(R.id.luckylove_day_star)
    MapTextView mLuckyLove;
    @BindView(R.id.luckynumber_day_star)
    MapTextView mLuckyNumber;

    private String mConstellation;
    private String mDayFlag;
    private Subscription mSubscription;
    ArrayList<RadarEntry> entries1;


    public static DayConstellationFragment newInstance(String constellation, String dayFlag) {
        DayConstellationFragment fragment = new DayConstellationFragment();
        Bundle args = new Bundle();
        args.putString(CONSTELLATION_FLAG, constellation);
        args.putString(DAY_FLAG, dayFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_day_constellation;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        initChart();
        requestStarData();
    }

    private void requestStarData() {
        if (mDayFlag==DAY_TODAY||mDayFlag==DAY_TOMORROW){
            unsubscribe();
            mSubscription= NetWork.getDayConstellationsApi()
                    .getDayConstellation(TextUtils.isEmpty(mConstellation)?"水瓶座":mConstellation,
                            mDayFlag,"35de7ea555a8b5d58ce2d7e4f8cb7c9f")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DayConstellation>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(DayConstellation dayConstellation) {
                            if (dayConstellation.getError_code()==0){
                                setDataToView(dayConstellation);
                            }
                        }
                    });
        }
    }

    private void setDataToView(DayConstellation dayConstellation) {
        if (entries1 == null) {
            entries1 = new ArrayList<>();
        } else {
            entries1.clear();
        }
        entries1.add(new RadarEntry(Float.valueOf(formatStringToFloat(dayConstellation.getAll()))));
        entries1.add(new RadarEntry(Float.valueOf(formatStringToFloat(dayConstellation.getHealth()))));
        entries1.add(new RadarEntry(Float.valueOf(formatStringToFloat(dayConstellation.getLove()))));
        entries1.add(new RadarEntry(Float.valueOf(formatStringToFloat(dayConstellation.getMoney()))));
        entries1.add(new RadarEntry(Float.valueOf(formatStringToFloat(dayConstellation.getWork()))));
        setData();

        mLuckyColor.setValueText(dayConstellation.getColor());
        mLuckyLove.setValueText(dayConstellation.getQFriend());
        mLuckyNumber.setValueText(dayConstellation.getNumber()+"");
        mSummaryDayStar.setText(""+dayConstellation.getSummary());
    }

    private void setData() {
        if (entries1==null||entries1.size()==0){
            float mult=80;
            float min=20;
            int cnt=5;

            entries1=new ArrayList<>();
            for (int i=0;i<cnt;i++){
                float val1=(float) (Math.random()*mult)+min;
                entries1.add(new RadarEntry(val1));
            }
        }

        RadarDataSet set1 = new RadarDataSet(entries1, mDayFlag.equals(DAY_TODAY)?"今日运势各项指数":"明日运势各项指数");
        set1.setColor(getResources().getColor(R.color.colorPrimaryDark));//表格数据连线的颜色
        set1.setFillColor(getResources().getColor(R.color.colorPrimary));//表格数据连线内部的颜色
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);//表格数据连线内部颜色的透明度
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(getResources().getColor(R.color.colorAccent));
        mChart.setData(data);

        mChart.setRotationEnabled(false);//禁止手滑动选择
        showValues();//显示对应的值

        mChart.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshConstellationEvent(RefreshConstellationEvent refreshConstellationEvent){
        mConstellation = refreshConstellationEvent.getConstellation();
        initChart();
        requestStarData();
    }

    private void showValues() {
        for (IDataSet<?> set : mChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.rgb(103,110,129));//网格连接到网格中心点的串连颜色
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.rgb(103,110,129));//网格一圈一圈的颜色
        mChart.setWebAlpha(100);

        MarkerView mv=new RadarMarkerView(getContext(),R.layout.radar_markerview);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        mChart.animateXY(1400,1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInQuad);

        XAxis xAxis=mChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities=new String[]{"综合指数", "健康指数", "爱情指数", "财运指数", "工作指数"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int)value%mActivities.length];
            }
        });

        xAxis.setTextColor(getResources().getColor(R.color.colorAccent));

        YAxis yAxis=mChart.getYAxis();

        yAxis.setLabelCount(5,false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(getResources().getColor(R.color.colorAccent));//表格数据标题的颜色
    }

    @Override
    protected void managerArguments() {
        mConstellation = getArguments().getString(CONSTELLATION_FLAG);
        mDayFlag = getArguments().getString(DAY_FLAG);
    }

    @Override
    public String getUmengFragmentName() {
        return  getContext().getClass().getSimpleName()+"-"
                +this.getClass().getSimpleName();
    }

    public float formatStringToFloat(String string) {
        String oldValue = string;
        if (oldValue.contains("%")) {
            oldValue = oldValue.replaceAll("%", "");
            try {
                return Float.valueOf(oldValue).floatValue();
            } catch (NumberFormatException e) {
                Logger.e(e.getMessage());
                return 93;
            }
        } else {
            try {
                return Float.valueOf(oldValue).floatValue();
            } catch (NumberFormatException e) {
                Logger.e(e.getMessage());
                return 93;
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
