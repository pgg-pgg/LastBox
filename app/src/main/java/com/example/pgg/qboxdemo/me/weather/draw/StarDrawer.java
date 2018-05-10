package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

/**
 * 晴天的夜晚，满天繁星
 */

public class StarDrawer extends BaseDrawer {
    private GradientDrawable drawable;
    private ArrayList<StarHolder> holders = new ArrayList<StarHolder>();

    private static final int STAR_COUNT = 80;
    private static final float STAR_MIN_SIZE = 2f;// dp
    private static final float STAR_MAX_SIZE = 6f;// dp

    public StarDrawer(Context context) {
        super(context, true);
        drawable=new GradientDrawable(GradientDrawable.Orientation.BL_TR,new int[]{0xffffffff,0x00ffffff});
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setGradientRadius((float)Math.sqrt(2)*60);
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        for (StarHolder holder:holders){
            holder.updateRandom(drawable,alpha);
            try {
                drawable.draw(canvas);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.holders.size()==0){
            final float starMinSize=dp2px(STAR_MIN_SIZE);
            final float starMaxSize=dp2px(STAR_MAX_SIZE);
            for (int i=0;i<STAR_COUNT;i++){
                final float starSize=getRandom(starMinSize,starMaxSize);
                final float y=getDownRandFloat(0,height);
                //20%的上半部分屏幕最高alpha为1，其余的越靠下最高alpha越小
                final float maxAlpha=0.2f+0.8f*(1f-y/height);
                StarHolder holder=new StarHolder(getRandom(0,width),y,starSize,starSize,maxAlpha);
                holders.add(holder);
            }
        }
    }

    public static class StarHolder{
        public float x;
        public float y;
        public float w;
        public float h;
        public final float maxAlpha;// [0,1]
        public float curAlpha;// [0,1]
        public boolean alphaIsGrowing = true;


        public StarHolder(float x,float y,float w,float h,float maxAlpha){
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
            this.maxAlpha=maxAlpha;
            this.curAlpha=getRandom(0f,maxAlpha);
        }

        public void  updateRandom(GradientDrawable drawable,float alpha){
            final float delta=getRandom(0.003f*maxAlpha,0.012f*maxAlpha);
            if (alphaIsGrowing){
                curAlpha+=delta;
                if (curAlpha>maxAlpha){
                    curAlpha=maxAlpha;
                    alphaIsGrowing=false;
                }
            }else {
                curAlpha-=delta;
                if (curAlpha<0){
                    curAlpha=0;
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
