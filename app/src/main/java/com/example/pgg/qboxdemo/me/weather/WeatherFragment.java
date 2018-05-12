package com.example.pgg.qboxdemo.me.weather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.me.weather.draw.BaseDrawer;
import com.example.pgg.qboxdemo.me.weather.widget.AqiView;
import com.example.pgg.qboxdemo.me.weather.widget.AstroView;
import com.example.pgg.qboxdemo.me.weather.widget.DailyForecastView;
import com.example.pgg.qboxdemo.me.weather.widget.HourlyForecastView;
import com.example.pgg.qboxdemo.me.weather.widget.PullRefreshLayout;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.module.weather.api.entity.HeWeatherDataService30;
import com.example.pgg.qboxdemo.module.weather.api.entity.Weather;
import com.example.pgg.qboxdemo.utils.MxxNetworkUtil;

/**
 * Created by pgg on 2018/5/11.
 */

public class WeatherFragment extends BaseWeatherFragment {
    private Weather mWeather;
    private static final String BUNDLE_EXTRA_AREA = "BUNDLE_EXTRA_AREA";
    private static final String BUNDLE_EXTRA_WEATHER = "BUNDLE_EXTRA_WEATHER";
    private View mRootView;

    private DailyForecastView mDailyForecastView;
    private PullRefreshLayout pullRefreshLayout;
    private HourlyForecastView mHourlyForecastView;
    private AqiView mAqiView;
    private AstroView mAstroView;
    private ApiManager.Area mArea;
    private ScrollView mScrollView;
    private BaseDrawer.Type mDrawerType=BaseDrawer.Type.UNKNOWN_D;

    public static WeatherFragment makeInstance(@NonNull ApiManager.Area area, Weather weather){
        WeatherFragment fragment=new WeatherFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(BUNDLE_EXTRA_AREA,area);
        if (weather!=null){
            bundle.putSerializable(BUNDLE_EXTRA_WEATHER,weather);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private void fetchArguments(){
        if (this.mArea==null){
            try {
                this.mArea=(ApiManager.Area)getArguments().getSerializable(BUNDLE_EXTRA_AREA);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (this.mWeather==null){
            try{
                this.mWeather=(Weather)getArguments().getSerializable(BUNDLE_EXTRA_WEATHER);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView==null){
            mRootView=inflater.inflate(R.layout.fragment_weather,null);
            mDailyForecastView=mRootView.findViewById(R.id.w_dailyForecastView);
            pullRefreshLayout=mRootView.findViewById(R.id.w_PullRefreshLayout);
            mHourlyForecastView=mRootView.findViewById(R.id.w_hourlyForecastView);
            mAqiView=mRootView.findViewById(R.id.w_aqi_view);
            mAstroView=mRootView.findViewById(R.id.w_astroView);
            mScrollView=mRootView.findViewById(R.id.w_WeatherScrollView);

            pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ApiManager.updateWeather(getActivity(), mArea.id, new ApiManager.ApiListener() {
                        @Override
                        public void onReceiveWeather(Weather weather, boolean updated) {
                            pullRefreshLayout.setRefreshing(false);
                            if (updated){
                                if (ApiManager.acceptWeather(weather)){
                                    WeatherFragment.this.mWeather=weather;
                                    updateWeatherUI();
                                }
                            }
                        }

                        @Override
                        public void onUpdateError() {
                            pullRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
            debug();
            if (mWeather!=null){
                updateWeatherUI();
            }
        }else {
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.scrollTo(0,0);
                }
            });
        }
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchArguments();
    }

    private void debug() {

    }

    public String getCityName() {
        fetchArguments();
        if (this.mArea != null) {
            return mArea.name_cn;
        } else {
            return "Error";
        }
    }

    private void updateDrawerTypeAndNotify(){
        final BaseDrawer.Type curType=ApiManager.convertWeatherType(mWeather);
        this.mDrawerType=curType;
        notifyActivityUpdate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchArguments();
        if (this.mArea==null){
            return;
        }
        TimingLogger logger=new TimingLogger("WeatherFragment","WeatherFragment.onActivityCreated");
        if (this.mWeather==null){
            this.mWeather=ApiManager.loadWeather(getActivity(),mArea.id);
            logger.addSplit("loadWeather");
            updateWeatherUI();
            logger.addSplit("updateWeatherUI");
        }
        logger.dumpToLog();
        if (mWeather == null) {
            postRefresh();
        }
    }

    private void postRefresh() {
        if (pullRefreshLayout!=null){
            Activity activity=getActivity();
            if (activity!=null){
                if (MxxNetworkUtil.isNetworkAvailable(activity)){
                    pullRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullRefreshLayout.setRefreshing(true,true);
                        }
                    },100);
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            checkRefresh();
        }
    }

    private void checkRefresh() {
        if (mArea==null){
            return;
        }
        if (getUserVisibleHint()){
            if (ApiManager.isNeedUpdate(mWeather)){
                postRefresh();
            }
        }
    }

    private void updateWeatherUI() {
        if (!ApiManager.acceptWeather(mWeather)) {
            return;
        }
        try {
            final Weather weather=mWeather;
            updateDrawerTypeAndNotify();

            HeWeatherDataService30 w=weather.get();
            mDailyForecastView.setData(weather);
            mHourlyForecastView.setData(weather);
            mAqiView.setData(w.aqi);
            mAstroView.setData(weather);

            final String tmp=w.now.tmp;
            try {
                final int tmp_int=Integer.valueOf(tmp);
                if (tmp_int < 0) {
                    setTextViewString(R.id.w_now_tmp, String.valueOf(-tmp_int));
                    mRootView.findViewById(R.id.w_now_tmp_minus).setVisibility(View.VISIBLE);
                } else {
                    setTextViewString(R.id.w_now_tmp, tmp);
                    mRootView.findViewById(R.id.w_now_tmp_minus).setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
                setTextViewString(R.id.w_now_tmp,tmp);
                mRootView.findViewById(R.id.w_now_tmp_minus).setVisibility(View.GONE);
            }
            setTextViewString(R.id.w_now_cond_text,w.now.cond.txt);

            if (ApiManager.isToday(w.basic.update.loc)) {
                setTextViewString(R.id.w_basic_update_loc, w.basic.update.loc.substring(11) + " 发布");
            } else {
                setTextViewString(R.id.w_basic_update_loc, w.basic.update.loc.substring(5) + " 发布");
            }

            setTextViewString(R.id.w_todaydetail_bottomline, w.now.cond.txt + "  " + mWeather.getTodayTempDescription());
            setTextViewString(R.id.w_todaydetail_temp, w.now.tmp + "°");
            setTextViewString(R.id.w_now_fl, w.now.fl + "°");
            setTextViewString(R.id.w_now_hum, w.now.hum + "%");// 湿度
            setTextViewString(R.id.w_now_vis, w.now.vis + "km");// 能见度
            setTextViewString(R.id.w_now_pcpn, w.now.pcpn + "mm"); // 降雨量

            if (weather.hasAqi()) {
                setTextViewString(R.id.w_aqi_text, w.aqi.city.qlty);

                setTextViewString(R.id.w_aqi_detail_text, w.aqi.city.qlty);
                setTextViewString(R.id.w_aqi_pm25, w.aqi.city.pm25 + "μg/m³");
                setTextViewString(R.id.w_aqi_pm10, w.aqi.city.pm10 + "μg/m³");
                setTextViewString(R.id.w_aqi_so2, w.aqi.city.so2 + "μg/m³");
                setTextViewString(R.id.w_aqi_no2, w.aqi.city.no2 + "μg/m³");

            } else {
                setTextViewString(R.id.w_aqi_text, "");
            }
            if (w.suggestion != null) {
                setTextViewString(R.id.w_suggestion_comf, w.suggestion.comf.txt);
                setTextViewString(R.id.w_suggestion_cw, w.suggestion.cw.txt);
                setTextViewString(R.id.w_suggestion_drsg, w.suggestion.drsg.txt);
                setTextViewString(R.id.w_suggestion_flu, w.suggestion.flu.txt);
                setTextViewString(R.id.w_suggestion_sport, w.suggestion.sport.txt);
                setTextViewString(R.id.w_suggestion_tarv, w.suggestion.trav.txt);
                setTextViewString(R.id.w_suggestion_uv, w.suggestion.uv.txt);

                setTextViewString(R.id.w_suggestion_comf_brf, w.suggestion.comf.brf);
                setTextViewString(R.id.w_suggestion_cw_brf, w.suggestion.cw.brf);
                setTextViewString(R.id.w_suggestion_drsg_brf, w.suggestion.drsg.brf);
                setTextViewString(R.id.w_suggestion_flu_brf, w.suggestion.flu.brf);
                setTextViewString(R.id.w_suggestion_sport_brf, w.suggestion.sport.brf);
                setTextViewString(R.id.w_suggestion_tarv_brf, w.suggestion.trav.brf);
                setTextViewString(R.id.w_suggestion_uv_brf, w.suggestion.uv.brf);
            }
        }catch (Exception e){
            e.printStackTrace();
            toast(mArea.name_cn+"Error\n"+e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setTextViewString(int w_now_tmp, String tmp) {
        TextView tv = (TextView) mRootView.findViewById(w_now_tmp);
        if (tv != null) {
            tv.setText(tmp);
        } else {
            toast("Error NOT found textView id->" + Integer.toHexString(w_now_tmp));
        }
    }

    @Override
    public String getTitle() {
        return getCityName();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRefresh();
    }

    @Override
    public void onSelected() {

    }

    @Override
    public BaseDrawer.Type getDrawerType() {
        return this.mDrawerType;
    }
}
