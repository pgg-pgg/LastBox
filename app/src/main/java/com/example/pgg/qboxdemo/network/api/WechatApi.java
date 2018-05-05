package com.example.pgg.qboxdemo.network.api;

import com.example.pgg.qboxdemo.model.entities.WechatItem;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pgg on 2018/5/5.
 */

public interface WechatApi {

    @GET("wx/article/search?key=1cc099ede9137")
    Observable<WechatItem> getWechat(@Query("cid") String cid, @Query("page") int page, @Query("size") int size);
}
