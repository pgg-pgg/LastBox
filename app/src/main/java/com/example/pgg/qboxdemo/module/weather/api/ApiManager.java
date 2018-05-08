package com.example.pgg.qboxdemo.module.weather.api;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.global.MyApplication;
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

    private static boolean acceptWeather(Weather weather) {
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
}
