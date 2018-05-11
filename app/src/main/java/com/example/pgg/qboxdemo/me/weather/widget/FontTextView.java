package com.example.pgg.qboxdemo.me.weather.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.pgg.qboxdemo.me.weather.WeatherActivity;

/**
 * Created by pgg on 2018/5/11.
 */

public class FontTextView extends AppCompatTextView {


    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()){
            return;
        }
        setTypeface(WeatherActivity.getTypeface(context));
    }
}
