package com.example.pgg.qboxdemo.me.weather.draw;

/**
 * Created by pgg on 2018/5/10.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

/**
 * 天气界面的所有元素的基类
 */
public abstract class BaseDrawer {

    /**
     * 所有元素类型的枚举
     * 默认，白天，夜晚，白天（下雨），夜晚（下雨），白天（下雪），夜晚（下雪）
     * 白天（多云），夜晚（多云），白天（阴天），夜晚（阴天），白天（雾天），夜晚（雾天）
     * 白天（雾霾），夜晚（雾霾），白天（沙尘），夜晚（沙尘），白天（刮风），夜晚（刮风），白天（雨夹雪），夜晚（雨夹雪）
     * 白天（未知天气），夜晚（未知天气）
     */
    public enum Type {
        DEFAULT, CLEAR_D, CLEAR_N, RAIN_D, RAIN_N, SNOW_D, SNOW_N,
        CLOUDY_D, CLOUDY_N, OVERCAST_D, OVERCAST_N, FOG_D, FOG_N,
        HAZE_D, HAZE_N, SAND_D, SAND_N, WIND_D, WIND_N, RAIN_SNOW_D, RAIN_SNOW_N,
        UNKNOWN_D, UNKNOWN_N
    }

    /**
     * 天气界面元素对应的背景颜色基类
     * 可自定义修改
     */
    public static final class SkyBackground {
        //默认
        public static final int[] BLACK = new int[]{0xff000000, 0xff000000};

        //下雨背景色
        public static final int[] CLEAR_D = new int[]{0xff303F9F, 0xff3F51B5};
        public static final int[] CLEAR_N = new int[]{0xff0b0f25, 0xff252b42};

        //阴天
        public static final int[] OVERCAST_D = new int[]{0xff151b45, 0xff192154};//0xff748798, 0xff617688
        public static final int[] OVERCAST_N = new int[]{0xff262921, 0xff23293e};

        //下雨
        public static final int[] RAIN_D = new int[]{0xff303f9f, 0xff303f9f};
        public static final int[] RAIN_N = new int[]{0xff0d0d15, 0xff22242f};
        //雾天
        public static final int[] FOG_D = new int[]{0xff3545ae, 0xff394bbd};
        public static final int[] FOG_N = new int[]{0xff2f3c47, 0xff24313b};

        //下雪
        public static final int[] SNOW_D = new int[]{0xff273381, 0xff2b3990};//临时用RAIN_D凑数的
        public static final int[] SNOW_N = new int[]{0xff1e2029, 0xff212630};

        //多云
        public static final int[] CLOUDY_D = new int[]{0xff222d72, 0xff273381};//临时用RAIN_D凑数的
        public static final int[] CLOUDY_N = new int[]{0xff071527, 0xff252b42};// 0xff193353 };//{ 0xff0e1623, 0xff222830 }

        //雾霾
        public static final int[] HAZE_D = new int[]{0xff616e70, 0xff474644};// 0xff999b95, 0xff818e90
        public static final int[] HAZE_N = new int[]{0xff373634, 0xff25221d};

        //沙尘
        public static final int[] SAND_D = new int[]{0xffb5a066, 0xffd5c086};//0xffa59056
        public static final int[] SAND_N = new int[]{0xff312820, 0xff514840};
    }

    /**
     * 根据天气选择对应的元素
     *
     * @param context
     * @param type
     * @return
     */
    public static BaseDrawer makeDrawerByType(Context context, Type type) {
        switch (type) {
            case CLEAR_D:
                return new SunnyDrawer(context);
            case CLEAR_N:
                return new StarDrawer(context);
            case RAIN_D:
                return new RainDrawer(context, true);
            case RAIN_N:
                return new RainDrawer(context, false);
            case SNOW_D:
                return new SnowDrawer(context, false);
            case SNOW_N:
                return new SnowDrawer(context, true);
            case CLOUDY_D:
                return new CloudyDrawer(context, false);
            case CLOUDY_N:
                return new CloudyDrawer(context, true);
            case OVERCAST_D:
                return new OvercastDrawer(context, false);
            case OVERCAST_N:
                return new OvercastDrawer(context, true);
            case FOG_D:
                return new FogDrawer(context, false);
            case FOG_N:
                return new FogDrawer(context, true);
            case HAZE_D:
                return new HazeDrawer(context, false);
            case HAZE_N:
                return new HazeDrawer(context, true);
            case SAND_D:
                return new SandDrawer(context, false);
            case SAND_N:
                return new SandDrawer(context, true);
            case WIND_D:
                return new WindDrawer(context, false);
            case WIND_N:
                return new WindDrawer(context, true);
            case RAIN_SNOW_D:
                return new RainAndSnowDrawer(context, false);
            case RAIN_SNOW_N:
                return new RainAndSnowDrawer(context, true);
            case UNKNOWN_D:
                return new UnknownDrawer(context, false);
            case UNKNOWN_N:
                return new UnknownDrawer(context, true);
            case DEFAULT:
            default:
                return new DefaultDrawer(context);

        }
    }

    protected Context context;
    private final float desity;//手机屏幕密度
    protected int width, height;
    private GradientDrawable skyDrawable;
    protected final boolean isNight;

    public BaseDrawer(Context context, boolean isNight) {
        super();
        this.context = context;
        this.desity = context.getResources().getDisplayMetrics().density;
        this.isNight = isNight;
    }

    /**
     * 渐变drawable的设置
     * 由上到下按照SkyBackground中的数组颜色进行渐变
     */
    public GradientDrawable makeSkyBackground() {
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getSkyBackgroundGradient());
    }

    /**
     * 绘制背景
     *
     * @param canvas
     * @param alpha
     */
    protected void drawSkyBackground(Canvas canvas, float alpha) {
        if (skyDrawable == null) {
            skyDrawable = makeSkyBackground();
            skyDrawable.setBounds(0, 0, width, height);
        }
        skyDrawable.setAlpha(Math.round(alpha * 255f));
        skyDrawable.draw(canvas);
    }

    public boolean draw(Canvas canvas, float alpha) {
        drawSkyBackground(canvas, alpha);
        boolean needDrawNextFrame = drawWeather(canvas, alpha);
        return needDrawNextFrame;
    }

    /**
     * 设置大小
     *
     * @param width
     * @param height
     */
    protected void setSize(int width, int height) {
        if (this.width != width && this.height != height) {
            this.width = width;
            this.height = height;
            if (this.skyDrawable != null) {
                skyDrawable.setBounds(0, 0, width, width);
            }
        }
    }

    protected int[] getSkyBackgroundGradient() {
        return isNight ? SkyBackground.CLEAR_N : SkyBackground.CLEAR_D;
    }

    /**
     * 绘制各个天气元素
     *
     * @param canvas
     * @param alpha
     * @return
     */
    public abstract boolean drawWeather(Canvas canvas, float alpha);

    /**
     * [min, max)
     * 获取最小值与最大值之间的一个随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static float getRandom(float min, float max) {
        if (max < min) {
            throw new IllegalArgumentException("max should bigger than min!!!!");
            // return 0f;
        }
        return (float) (min + Math.random() * (max - min));
    }

    public float dp2px(float dp) {
        return dp * desity;
    }

    /**
     * 必须取[0,1]之间的float
     *
     * @param alpha
     * @return
     */
    public static float fixAlpha(float alpha) {
        if (alpha > 1f) {
            return 1f;
        }
        if (alpha < 0f) {
            return 0f;
        }
        return alpha;
    }

    /**
     * 获取[min, max)内的随机数，越大的数概率越小
     * 参考http://blog.csdn.net/loomman/article/details/3861240
     *
     * @param min
     * @param max
     * @return
     */
    public static float getDownRandFloat(float min, float max) {
        float bigend = ((min + max) * max) / 2f;
        float x = getRandom(min, bigend);
        int sum = 0;
        for (int i = 0; i < max; i++) {
            sum += (max - i);
            if (sum > x) {
                return i;
            }
        }
        return min;
    }


    public static int convertAlphaColor(float percent, final int originalColor) {
        int newAlpha = (int) (percent * 255) & 0xFF;
        return (newAlpha << 24) | (originalColor & 0xFFFFFF);
    }
}
