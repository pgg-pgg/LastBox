package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

/**
 * 下雨元素
 */
public class RainDrawer extends BaseDrawer {

    /**
     * 设置雨滴绘制
     */
    public static class RainDrawable{
        public float x,y;
        public float length;
        public Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

        public RainDrawable(){
            paint.setStyle(Paint.Style.STROKE);
        }

        public void setColor(int color){
            this.paint.setColor(color);
        }
        public void setStrokeWidth(float strokeWidth){
            this.paint.setStrokeWidth(strokeWidth);
        }
        public void setLocation(float x,float y,float length){
            this.x=x;
            this.y=y;
            this.length=length;
        }
        public void draw(Canvas canvas){
            canvas.drawLine(x,y-length,x,y,paint);
        }
    }

    private RainDrawable drawable;
    private ArrayList<RainHolder> holders=new ArrayList<>();

    private final int cfg_count=50;//雨滴个数
    public RainDrawer(Context context, boolean isNight) {
        super(context, isNight);
        drawable=new RainDrawable();
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        for (RainHolder holder:holders){
            holder.updateRandom(drawable,alpha);
            drawable.draw(canvas);
        }
        return true;
    }

    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size()==0){
            final float rainWidth=dp2px(2);
            final float minRainHeight=dp2px(8);
            final float maxRainHeight=dp2px(14);
            final float speed=dp2px(400);
            for (int i=0;i<cfg_count;i++){
                float x=getRandom(0f,width);
                RainHolder holder=new RainHolder(x,rainWidth,minRainHeight,maxRainHeight,height,speed);
                holders.add(holder);
            }
        }
    }

    @Override
    protected int[] getSkyBackgroundGradient() {
        return isNight?SkyBackground.RAIN_N:SkyBackground.RAIN_D;
    }

    public static class RainHolder{
        public float x;
        public final float rainWidth;
        public final float rainHeight;
        public final float maxY;//最大的下落的高度，超过此高度则消失
        public float curTime;//当前雨滴下落的时间
        public final int rainColor;//雨滴颜色
        public final float v;//雨滴下落速度

        public RainHolder(float x,float rainWidth,float minRainHeight,float maxRainHeight,float maxY,float speed){
            this.x=x;
            this.rainWidth=rainWidth;
            this.rainHeight=getRandom(minRainHeight,maxRainHeight);
            this.rainColor= Color.argb((int)(getRandom(0.1f,0.5f)*255f),0xff,0xff,0xff);
            this.maxY=maxY;
            this.v=speed*getRandom(0.9f,1.1f);
            final float maxTime=maxY/this.v;
            this.curTime=getRandom(0,maxTime);
        }

        public void updateRandom(RainDrawable drawable,float alpha){
            curTime+=0.025f;
            float curY=curTime*this.v;
            if ((curY-this.rainHeight)>this.maxY){
                this.curTime=0f;
            }
            drawable.setColor(Color.argb((int)(Color.alpha(rainColor)*alpha),0xff,0xff,0xff));
            drawable.setStrokeWidth(rainWidth);
            drawable.setLocation(x,curY,rainHeight);
        }
    }
}
