package com.example.pgg.qboxdemo.network.api;

import com.example.pgg.qboxdemo.model.entities.Constellation;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pgg on 2018/5/7.
 */

public interface ConstellationApi {

    @GET("constellation/getAll")
    Observable<Constellation> getConstellation(@Query("consName") String consName, @Query("type") String type, @Query("key") String key);
}
