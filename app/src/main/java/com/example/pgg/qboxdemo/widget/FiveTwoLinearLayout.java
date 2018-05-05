package com.example.pgg.qboxdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.pgg.qboxdemo.utils.PixelUtil;

/**
 * Created by pgg on 2018/5/5.
 */

public class FiveTwoLinearLayout extends LinearLayout {


    public FiveTwoLinearLayout(Context context) {
        this(context,null);
    }

    public FiveTwoLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FiveTwoLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fourThreeHeight=MeasureSpec.makeMeasureSpec(((MeasureSpec.getSize(widthMeasureSpec)- PixelUtil.dp2px(40))/4)+PixelUtil.dp2px(20),MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, fourThreeHeight);
    }
}
