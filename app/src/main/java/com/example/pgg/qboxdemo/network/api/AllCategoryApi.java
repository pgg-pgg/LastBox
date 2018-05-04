package com.example.pgg.qboxdemo.network.api;


import com.example.pgg.qboxdemo.model.AllCategoryBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 获取种类的api接口
 */


public interface AllCategoryApi {
    @GET("wx/article/category/query?key=1cc099ede9137")
    Observable<AllCategoryBean> getAllCategory();
}
