package com.example.pgg.qboxdemo.network;

import android.os.Environment;

import com.example.pgg.qboxdemo.network.api.AllCategoryApi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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
        cacheClient=builder.build();
    }

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static AllCategoryApi getmAllCategoryApi(){
        if (mAllCategoryApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(MOB_ROOT_URL)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            mAllCategoryApi=retrofit.create(AllCategoryApi.class);
        }
        return mAllCategoryApi;
    }
}
