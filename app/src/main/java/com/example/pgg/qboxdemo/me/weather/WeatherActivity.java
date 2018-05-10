package com.example.pgg.qboxdemo.me.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.pgg.qboxdemo.R;

/**
 * Created by pgg on 2018/5/3.
 */

public class WeatherActivity extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
    }
}
