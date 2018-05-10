package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

/**
 * 晴天的元素背景类
 */
public class SunnyDrawer extends BaseDrawer{
    private GradientDrawable drawable;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿

    private final float centerOfWidth = 0.02f;//关于圆中点设置
    private static final int SUNNY_COUNT = 3;

    private ArrayList<SunnyHolder> holders = new ArrayList<>();
    public SunnyDrawer(Context context) {
        super(context, false);
        drawable=new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{0x20ffffff,0x10ffffff});//从左下角向右上角渐变
        drawable.setShape(GradientDrawable.OVAL);//椭圆形
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);//放射性渐变
        paint.setColor(0x33ffffff);
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        final float size=width*centerOfWidth;
        for (SunnyHolder holder:holders){
            holder.updateRandom(drawable,alpha);
            drawable.draw(canvas);
        }
        paint.setColor(Color.argb((int)(alpha*0.18f*255f),0xff,0xff,0xff));
        canvas.drawCircle(size,size,width*0.12f,paint);
        return true;
    }

    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size()==0){
            final float minSize=width*0.16f;
            final float maxSize=width*1.5f;
            final float center=width*centerOfWidth;
            float deltaSize=(maxSize-minSize)/SUNNY_COUNT;
            for (int i=0;i<SUNNY_COUNT;i++){
                final float curSize=maxSize-i*deltaSize*getRandom(0.9f,1.1f);
                SunnyHolder holder=new SunnyHolder(center,center,curSize,curSize);
                holders.add(holder);
            }
        }
    }

    /**
     * 晴天元素的渐变效果的控制类
     */
    public static class SunnyHolder{
        public float x;
        public float y;
        public float w;
        public float h;
        public final float maxAlpha=1f;//最大透明度
        public float curAlpha;//当前的透明度
        public boolean alphaIsGrowing=true;
        private final float minAlpha=0.5f;//最小透明度

        public SunnyHolder(float x,float y,float w,float h){
            super();
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
            this.curAlpha=getRandom(minAlpha,maxAlpha);
        }

        //更新透明度
        public void updateRandom(GradientDrawable drawable,float alpha){
            //渐变值取值
            final float delta=getRandom(0.002f*maxAlpha,0.005f*maxAlpha);
            if (alphaIsGrowing){
                curAlpha+=delta;
                if (curAlpha>maxAlpha){
                    curAlpha=maxAlpha;
                    alphaIsGrowing=false;
                }
            }else {
                curAlpha-=delta;
                if (curAlpha<minAlpha){
                    curAlpha=minAlpha;
                    alphaIsGrowing=true;
                }
            }
            final int left=Math.round(x-w/2f);
            final int right=Math.round(x+w/2f);
            final int top=Math.round(y-h/2f);
            final int bottom=Math.round(y+h/2f);
            drawable.setBounds(left,top,right,bottom);
            drawable.setGradientRadius(w/2.2f);
            drawable.setAlpha((int)(255*curAlpha*alpha));
        }

    }
}
