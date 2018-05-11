package com.example.pgg.qboxdemo.me.weather.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by pgg on 2018/5/11.
 */

/***
 * 第一个child高度为ScrollView的高度
 */
public class FirstMatchInScrollViewLinearLayout extends LinearLayout {

    public FirstMatchInScrollViewLinearLayout(Context context) {
        super(context);
    }

    public FirstMatchInScrollViewLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstMatchInScrollViewLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount()>0){
            final ViewParent parent=getParent();
            if (parent!=null&&parent instanceof ScrollView){
                final int height=((ScrollView)parent).getMeasuredHeight();
                if (height>0){
                    final View firstChild=getChildAt(0);
                    LayoutParams layoutParams = (LayoutParams) firstChild.getLayoutParams();
                    layoutParams.height=height;
                    firstChild.setLayoutParams(layoutParams);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
