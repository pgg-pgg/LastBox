package com.example.pgg.qboxdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by pgg on 2018/5/6.
 */

public class FourThreeImageView extends ImageView {

    public FourThreeImageView(Context context) {
        super(context);
    }

    public FourThreeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FourThreeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fourThreeHeight=MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec)*3/4,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, fourThreeHeight);
    }
}
