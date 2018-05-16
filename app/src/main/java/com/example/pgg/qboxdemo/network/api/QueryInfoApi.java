package com.example.pgg.qboxdemo.network.api;

import com.example.pgg.qboxdemo.model.entities.QueryIDCard;
import com.example.pgg.qboxdemo.model.entities.QueryQQ;
import com.example.pgg.qboxdemo.model.entities.QueryTel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pgg on 2018/5/15.
 */

public interface QueryInfoApi {

    @GET("mobile/get")
    Observable<QueryTel> getTelInfo(@Query("key") String key, @Query("phone") String phone);

    @GET("qqevaluate/qq")
    Observable<QueryQQ> getQQInfo(@Query("key") String key, @Query("qq") String qq);

    @GET("idcard/index")
    Observable<QueryIDCard> getIDCardInfo(@Query("key") String key, @Query("cardno") String cardno);


}
