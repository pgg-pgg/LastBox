package com.example.pgg.qboxdemo.me.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.me.weather.draw.BaseDrawer;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.module.weather.api.entity.Weather;

/**
 * Created by pgg on 2018/5/11.
 */

public class WeatherFragment extends BaseWeatherFragment {

    private ApiManager.Area mArea;
    private BaseDrawer.Type mDrawerType = BaseDrawer.Type.UNKNOWN_D;
    private Weather mWeather;
    private static final String BUNDLE_EXTRA_AREA = "BUNDLE_EXTRA_AREA";
    private static final String BUNDLE_EXTRA_WEATHER = "BUNDLE_EXTRA_WEATHER";
    private View mRootView;

    public static WeatherFragment makeInstance(@NonNull ApiManager.Area area, Weather weather){
        WeatherFragment fragment=new WeatherFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(BUNDLE_EXTRA_AREA,weather);
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
            mRootView=inflater.inflate(R.layout.fragment_wechat,null);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public BaseDrawer.Type getDrawerType() {
        return this.mDrawerType;
    }
}
