package com.example.pgg.qboxdemo.network;

import android.os.Environment;

import com.example.pgg.qboxdemo.network.api.AllCategoryApi;
import com.example.pgg.qboxdemo.network.api.ChinaCalendarApi;
import com.example.pgg.qboxdemo.network.api.CityApi;
import com.example.pgg.qboxdemo.network.api.ConstellationApi;
import com.example.pgg.qboxdemo.network.api.DayJokeApi;
import com.example.pgg.qboxdemo.network.api.FindBgApi;
import com.example.pgg.qboxdemo.network.api.WechatApi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pgg on 2018/5/2.
 */

public class NetWork {


    public static final String MOB_ROOT_URL = "http://apicloud.mob.com/";

    private static AllCategoryApi mAllCategoryApi;
    private static ConstellationApi sConstellationApi;
    private static WechatApi mWechatApi;
    private static DayJokeApi sDayJokeApi;
    private static FindBgApi sFindBgApi;
    public static ChinaCalendarApi sChinaCalendarApi;
    public static CityApi sCityApi;

    private static final long cacheSize=1024*1024*20;//缓存文件大小
    private static String cacheDirectory= Environment.getExternalStorageState()+"/okhttpcaches";
    private static Cache cache=new Cache(new File(cacheDirectory),cacheSize);
    private static final OkHttpClient cacheClient;

    static {
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);
        builder.writeTimeout(8,TimeUnit.SECONDS);
        builder.readTimeout(8,TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.cache(cache);
        //添加日志信息拦截器
        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        cacheClient=builder.build();
    }

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static AllCategoryApi getmAllCategoryApi(){
        if (mAllCategoryApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl(MOB_ROOT_URL)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            mAllCategoryApi=retrofit.create(AllCategoryApi.class);
        }
        return mAllCategoryApi;
    }

    public static WechatApi getWeChatApi(){
        if (mWechatApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl(MOB_ROOT_URL)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            mWechatApi=build.create(WechatApi.class);
        }
        return mWechatApi;
    }

    public static ConstellationApi getConstellationApi(){
        if (sConstellationApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl("http://web.juhe.cn:8080/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sConstellationApi=build.create(ConstellationApi.class);
        }
        return sConstellationApi;
    }

    public static DayJokeApi getDayJokeApi(){
        if (sDayJokeApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl("http://japi.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sDayJokeApi=build.create(DayJokeApi.class);
        }
        return sDayJokeApi;
    }

    public static FindBgApi getFindBgApi(){
        if (sFindBgApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl("http://www.bing.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sFindBgApi=build.create(FindBgApi.class);
        }
        return sFindBgApi;
    }

    public static ChinaCalendarApi getsChinaCalendarApi(){
        if (sChinaCalendarApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl("http://v.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sChinaCalendarApi= build.create(ChinaCalendarApi.class);
        }
        return sChinaCalendarApi;
    }

    public static CityApi getCityApi(){
        if (sCityApi==null){
            Retrofit build = new Retrofit.Builder()
                    .client(cacheClient)
                    .baseUrl("https://api.heweather.com/")
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            sCityApi=build.create(CityApi.class);
        }
        return sCityApi;
    }
}
