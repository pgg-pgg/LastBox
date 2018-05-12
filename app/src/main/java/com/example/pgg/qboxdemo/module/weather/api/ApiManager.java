package com.example.pgg.qboxdemo.module.weather.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;

import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.global.MyApplication;
import com.example.pgg.qboxdemo.me.weather.draw.BaseDrawer;
import com.example.pgg.qboxdemo.module.weather.api.entity.DailyForecast;
import com.example.pgg.qboxdemo.module.weather.api.entity.HourlyForecast;
import com.example.pgg.qboxdemo.module.weather.api.entity.Weather;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.utils.UiUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by pgg on 2018/5/8.
 */

public class ApiManager {

    private static AreaData AREA_DATA;

    static final Gson GSON = new Gson();
    static final String TAG = ApiManager.class.getSimpleName();
    static final String URLSITE = "https://free-api.heweather.com/v5/weather?key=def9a507328e4cd395d983fe2589586e&city="/*"https://api.heweather.com/x3/weather?key=yourkey&cityid="*/;
    static String getApiUrlFromAreaId(String areaId) {
        return URLSITE + areaId;
    }
    public static final String KEY_SELECTED_AREA = "KEY_SELECTED_AREA";

    public static final String GITHUB_SAMPLE_SELECTED_AREA = "[{\"city\":\"杭州\",\"id\":\"CN101210101\",\"name_cn\":\"杭州\",\"name_en\":\"beijing\",\"province\":\"北京\"},{\"city\":\"石家庄\",\"id\":\"CN101090101\",\"name_cn\":\"石家庄\",\"name_en\":\"shijiazhuang\",\"province\":\"河北\"},{\"city\":\"上海\",\"id\":\"CN101020100\",\"name_cn\":\"上海\",\"name_en\":\"shanghai\",\"province\":\"上海\"}]";

    public static ArrayList<Area> loadSelectedArea(Context context){
        ArrayList<Area> areas=new ArrayList<>();
        String json= MyApplication.USE_SAMPLE_DATA?GITHUB_SAMPLE_SELECTED_AREA: (String) SPUtils.get(context, KEY_SELECTED_AREA, "");
        Logger.e("存储的数据："+json);
        if (TextUtils.isEmpty(json)){
            Area e=new Area();
            if (!TextUtils.isEmpty((String) SPUtils.get(context, Constant.USER_ADDRESS_ID, ""))) {
                e.setId(((String) SPUtils.get(context, Constant.USER_ADDRESS_ID, "")));
                e.setName_cn(((String) SPUtils.get(context, Constant.USER_ADDRESS_CITY, "")));
                e.setProvince(((String) SPUtils.get(context, Constant.USER_ADDRESS_PROV, "")));
                e.setCity(((String) SPUtils.get(context, Constant.USER_ADDRESS_CITY, "")));
                areas.add(e);
                return areas;
            } else {
                json = GITHUB_SAMPLE_SELECTED_AREA;
            }
        }
        try {
            Area[] aa = GSON.fromJson(json, Area[].class);
            if (aa != null) {
                Collections.addAll(areas, aa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (areas.size() == 0) {
            if (!TextUtils.isEmpty((String) SPUtils.get(context, Constant.USER_ADDRESS_ID, ""))) {
                Area e = new Area();
                e.setId(((String) SPUtils.get(context, Constant.USER_ADDRESS_ID, "")));
                e.setName_cn(((String) SPUtils.get(context, Constant.USER_ADDRESS_CITY, "")));
                e.setProvince(((String) SPUtils.get(context, Constant.USER_ADDRESS_PROV, "")));
                e.setCity(((String) SPUtils.get(context, Constant.USER_ADDRESS_CITY, "")));

                areas.add(e);
                return areas;
            } else {
                json = GITHUB_SAMPLE_SELECTED_AREA;
                Area[] aa = GSON.fromJson(json, Area[].class);
                if (aa != null) {
                    Collections.addAll(areas, aa);
                }
            }
        }
        return areas;
    }


    public static boolean isToday(String date) {
        if (TextUtils.isEmpty(date) || date.length() < 10) {// 2015-11-05
            // length=10
            return false;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String today = format.format(new Date());
            if (TextUtils.equals(today, date.substring(0, 10))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 转换日期2015-11-05为今天、明天、昨天，或者是星期几
     *
     * @param date
     * @return
     */
    public static String prettyDate(String date) {
        try {
            final String[] strs = date.split("-");
            final int year = Integer.valueOf(strs[0]);
            final int month = Integer.valueOf(strs[1]);
            final int day = Integer.valueOf(strs[2]);
            Calendar c = Calendar.getInstance();
            int curYear = c.get(Calendar.YEAR);
            int curMonth = c.get(Calendar.MONTH) + 1;// Java月份从0月开始
            int curDay = c.get(Calendar.DAY_OF_MONTH);
            if (curYear == year && curMonth == month) {
                if (curDay == day) {
                    return "今天";
                } else if ((curDay + 1) == day) {
                    return "明天";
                } else if ((curDay - 1) == day) {
                    return "昨天";
                }
            }
            c.set(year, month - 1, day);
            // http://www.tuicool.com/articles/Avqauq
            // 一周第一天是否为星期天
            boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
            // 获取周几
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            // 若一周第一天为星期天，则-1
            if (isFirstSunday) {
                dayOfWeek = dayOfWeek - 1;
                if (dayOfWeek == 0) {
                    dayOfWeek = 7;
                }
            }
            // 打印周几
            // System.out.println(weekDay);

            // 若当天为2014年10月13日（星期一），则打印输出：1
            // 若当天为2014年10月17日（星期五），则打印输出：5
            // 若当天为2014年10月19日（星期日），则打印输出：7
            switch (dayOfWeek) {
                case 1:
                    return "周一";
                case 2:
                    return "周二";
                case 3:
                    return "周三";
                case 4:
                    return "周四";
                case 5:
                    return "周五";
                case 6:
                    return "周六";
                case 7:
                    return "周日";
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Weather loadWeather(@NonNull Context context, @NonNull String id) {
        return null;
    }

    public static boolean isNeedUpdate(Weather mWeather) {
        if (!acceptWeather(mWeather)){
            return true;
        }
        try {
            final String updateTime=mWeather.get().basic.update.loc;
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date updateDate=format.parse(updateTime);
            Date curDate=new Date();
            long interval=curDate.getTime()-updateDate.getTime();//时间间隔 ms
            if ((interval>=0)&&(interval<75*60*1000)){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static class Area implements Serializable{

        public Area() {
            super();
        }

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("name_en")
        @Expose
        public String name_en;

        @SerializedName("name_cn")
        @Expose
        public String name_cn;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("province")
        @Expose
        public String province;

        @Override
        public String toString() {
            return name_cn+"["+city+","+province+"]";
        }

        @Override
        public int hashCode() {
            final int prime=31;
            int result=1;
            result=prime*result+((city==null)?0:city.hashCode());
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            result = prime * result + ((name_cn == null) ? 0 : name_cn.hashCode());
            result = prime * result + ((name_en == null) ? 0 : name_en.hashCode());
            result = prime * result + ((province == null) ? 0 : province.hashCode());
            return result;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        @Override
        public boolean equals(Object obj) {
            if (this==obj){
                return true;
            }
            if (obj==null){
                return false;
            }
            if (getClass()!=obj.getClass()){
                return false;
            }
            Area other= (Area) obj;
            if (city==null){
                if (other.city!=null){
                    return false;
                }
            }else if (!city.equals(other.city)){
                return false;
            }
            if (id==null){
                if (other.id!=null){
                    return false;
                }
            }else if (!id.equals(other.id)){
                return false;
            }
            if (name_cn==null){
                if (other.name_cn!=null){
                    return false;
                }
            }else if (!name_cn.equals(other.name_cn)){
                return false;
            }
            if (name_en==null){
                if (other.name_en!=null){
                    return false;
                }
            }else if (!name_en.equals(other.name_en)){
                return false;
            }
            if (province==null){
                if (other.province!=null){
                    return false;
                }
            }else if (!province.equals(other.province)){
                return false;
            }
            return true;
        }
    }

    public static class AreaData implements Serializable{

        @SerializedName("list")
        @Expose
        public ArrayList<Area> list=new ArrayList<>();

        @SerializedName("create")
        @Expose
        public String create;

        public AreaData(){
            super();
        }
    }

    static class ApiTask extends AsyncTask<Void,Void,Weather>{

        private final Context context;
        private final String url;
        private final ApiListener listener;

        public ApiTask(Context context,String url,ApiListener listener) {
            super();
            this.context=context;
            this.url=url;
            this.listener=listener;
        }

        @Override
        protected Weather doInBackground(Void... voids) {
            return updateWeatherFromInternet(context,url);
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            if (listener!=null){
                if (weather!=null){
                    listener.onReceiveWeather(weather,true);
                }else {
                    listener.onUpdateError();
                    UiUtil.toastDebug(context,"更新失败");
                }
            }
        }
    }

    static Weather updateWeatherFromInternet(Context context, String url) {
        try {
            String json="";
            if (MyApplication.USE_SAMPLE_DATA){
                String areaName=url.substring(url.length()-11);
                InputStream is=context.getAssets().open(areaName);
                InputStreamReader isr=new InputStreamReader(is);
                JsonReader reader=new JsonReader(isr);
                Weather weather = GSON.fromJson(reader, Weather.class);
                reader.close();
                is.close();
                Calendar calendar = Calendar.getInstance();
                weather.get().basic.update.loc=(new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
                final ArrayList<DailyForecast> dailyForecast = weather.get().dailyForecast;
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (DailyForecast dailyForecast1:dailyForecast){
                    dailyForecast1.date=simpleDateFormat.format(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_YEAR,+1);
                }
                final ArrayList<HourlyForecast> hourlyForecasts = weather.get().hourlyForecast;//"2016-08-16 13:00"
                final String todayDate = simpleDateFormat.format(System.currentTimeMillis());
                for (HourlyForecast hourlyForecast : hourlyForecasts) {
                    hourlyForecast.date = hourlyForecast.date.substring(todayDate.length());
                    hourlyForecast.date = todayDate + hourlyForecast.date;
                }
                Thread.sleep(800);//这个速度有点快，慢一些
                return weather;
            }else {
                json = doHttpGet(url);
                Logger.e("请求成功"+json);
                if (!TextUtils.isEmpty(json)){
                    Weather weather = GSON.fromJson(json, Weather.class);
                    if (acceptWeather(weather)){
                        return weather;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean acceptWeather(Weather weather) {
        if (weather==null||!weather.isOK()){
            return false;
        }
        return true;
    }

    private static String doHttpGet(String url) {
        try{
            final int TIMEOUT=30000;
            String result=null;
            HttpURLConnection urlConnection= (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(5*1000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                InputStream inputStream = urlConnection.getInputStream();
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer=new byte[1024];
                    int len=0;
                    while ((len=inputStream.read(buffer))!=-1){
                        baos.write(buffer,0,len);
                    }
                    baos.close();
                    inputStream.close();
                    byte[] byteArray=baos.toByteArray();
                    result=new String(byteArray);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
            urlConnection.disconnect();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public interface ApiListener {
        void onReceiveWeather(Weather weather, boolean updated);

        void onUpdateError();
    }

    public interface LoadAreaDataListener {
        void onLoaded(AreaData areaData);

        void onError();
    }

    public static void updateWeather(@NonNull Context context, @NonNull String areaId,@NonNull ApiListener listener){
        if (TextUtils.isEmpty(areaId)){
            return;
        }
        final String url=getApiUrlFromAreaId(areaId);
        ApiTask apiTask=new ApiTask(context,url,listener);
        AsyncTaskCompat.executeParallel(apiTask);
    }

    public static BaseDrawer.Type convertWeatherType(Weather weather){
        if (weather==null||!weather.isOK()){
            return BaseDrawer.Type.DEFAULT;
        }

        final boolean isNight=isNight(weather);
        try {
            final int w=Integer.valueOf(weather.get().now.cond.code);
            switch (w){
                case 100:
                    return isNight? BaseDrawer.Type.CLEAR_N: BaseDrawer.Type.CLEAR_D;
                case 101://多云
                case 102://少云
                case 103://晴间多云
                    return isNight? BaseDrawer.Type.CLOUDY_N: BaseDrawer.Type.CLOUDY_D;
                case 104://阴
                    return isNight? BaseDrawer.Type.OVERCAST_N: BaseDrawer.Type.OVERCAST_D;
                // 200 - 213是风
                case 200:
                case 201:
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                    return isNight ? BaseDrawer.Type.WIND_N : BaseDrawer.Type.WIND_D;
                case 300:// 阵雨Shower Rain
                case 301:// 强阵雨 Heavy Shower Rain
                case 302:// 雷阵雨 Thundershower
                case 303:// 强雷阵雨 Heavy Thunderstorm
                case 304:// 雷阵雨伴有冰雹 Hail
                case 305:// 小雨 Light Rain
                case 306:// 中雨 Moderate Rain
                case 307:// 大雨 Heavy Rain
                case 308:// 极端降雨 Extreme Rain
                case 309:// 毛毛雨/细雨 Drizzle Rain
                case 310:// 暴雨 Storm
                case 311:// 大暴雨 Heavy Storm
                case 312:// 特大暴雨 Severe Storm
                case 313:// 冻雨 Freezing Rain
                    return isNight ? BaseDrawer.Type.RAIN_N : BaseDrawer.Type.RAIN_D;
                case 400:// 小雪 Light Snow
                case 401:// 中雪 Moderate Snow
                case 402:// 大雪 Heavy Snow
                case 403:// 暴雪 Snowstorm
                case 407:// 阵雪 Snow Flurry
                    return isNight ? BaseDrawer.Type.SNOW_N : BaseDrawer.Type.SNOW_D;
                case 404:// 雨夹雪 Sleet
                case 405:// 雨雪天气 Rain And Snow
                case 406:// 阵雨夹雪 Shower Snow
                    return isNight ? BaseDrawer.Type.RAIN_SNOW_N : BaseDrawer.Type.RAIN_SNOW_D;
                case 500:// 薄雾
                case 501:// 雾
                    return isNight ? BaseDrawer.Type.FOG_N : BaseDrawer.Type.FOG_D;
                case 502:// 霾
                case 504:// 浮尘
                    return isNight ? BaseDrawer.Type.HAZE_N : BaseDrawer.Type.HAZE_D;
                case 503:// 扬沙
                case 506:// 火山灰
                case 507:// 沙尘暴
                case 508:// 强沙尘暴
                    return isNight ? BaseDrawer.Type.SAND_N : BaseDrawer.Type.SAND_D;
                default:
                    return isNight ? BaseDrawer.Type.UNKNOWN_N : BaseDrawer.Type.UNKNOWN_D;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isNight? BaseDrawer.Type.UNKNOWN_N: BaseDrawer.Type.UNKNOWN_D;
    }

    /**
     * 判断是否是夜晚
     * @param weather
     * @return
     */
    private static boolean isNight(Weather weather) {
        if (weather==null||!weather.isOK()){
            return false;
        }
        try {
            final Date date=new Date();
            String todaydate=(new SimpleDateFormat("yyyy-MM-dd")).format(date);
            String todaydate1=(new SimpleDateFormat("yyyy-M-d")).format(date);
            DailyForecast todayForecast=null;
            for (DailyForecast forecast:weather.get().dailyForecast){
                if (TextUtils.equals(todaydate,forecast.date)||TextUtils.equals(todaydate1, forecast.date)){
                    todayForecast=forecast;
                    break;
                }
            }
            if (todayForecast!=null){
                final int curTime=Integer.valueOf(new SimpleDateFormat("HHmm").format(date));
                final int srTime=Integer.valueOf(todayForecast.astro.sr.replaceAll(":",""));//日出
                final int ssTime=Integer.valueOf(todayForecast.astro.ss.replaceAll(":",""));//日落
                if (curTime>srTime&&curTime<=ssTime){
                    //是白天
                    return false;
                }else {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
