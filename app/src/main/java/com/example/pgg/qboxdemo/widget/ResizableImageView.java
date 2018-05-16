package com.example.pgg.qboxdemo.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by pgg on 2018/5/16.
 */

public class ResizableImageView extends ImageView {

    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d=getDrawable();
        if (d!=null){
            int width= View.MeasureSpec.getSize(widthMeasureSpec);
            int height=(int)Math.ceil((float)width*(float)d.getIntrinsicHeight()/(float)d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
