package com.example.pgg.qboxdemo.network.api;


import com.example.pgg.qboxdemo.module.find.joke.NewTextJokeBean;
import com.example.pgg.qboxdemo.module.find.joke.RandomTextJoke;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/*******************************************************************
 * * * * *   * * * *   *     *       Created by OCN.Yang
 * *     *   *         * *   *       Time:2017/3/3 15:24.
 * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public interface TextJokeApi {
    @GET("joke/content/text.from")
    Observable<NewTextJokeBean> getNewTextJokeJoke(@Query("key") String appkey,
                                                   @Query("page") int pno,
                                                   @Query("pagesize") int ps);

    @GET("joke/randJoke.php")
    Observable<RandomTextJoke> getRandomTextJoke(@Query("key") String key);
}
