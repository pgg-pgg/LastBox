package com.example.pgg.qboxdemo.me.weather;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.pgg.qboxdemo.me.weather.draw.BaseDrawer;

/**
 * Created by pgg on 2018/5/11.
 */

/**
 * 所有天气界面fragment的基类
 */
public abstract class BaseWeatherFragment extends Fragment {

    //获取标题
    public abstract String getTitle();

    public abstract void onSelected();

    //获取各个元素的类型
    public abstract BaseDrawer.Type getDrawerType();

    //懒加载
    protected void notifyActivityUpdate(){
        if (getUserVisibleHint()){
            Activity activity=getActivity();
            if (activity!=null){
                ((WeatherActivity)activity).updateCurDrawerType();
            }
        }
    }

    protected void toast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
