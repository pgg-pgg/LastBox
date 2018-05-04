package com.example.pgg.qboxdemo.module.start.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.greendao.db.CategoryEntityDao;
import com.example.pgg.qboxdemo.model.AllCategoryBean;
import com.example.pgg.qboxdemo.model.entities.CategoryEntity;
import com.example.pgg.qboxdemo.network.NetWork;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.utils.StateBarTranslucentUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/3.
 */

public class WelcomeGuideActivity extends AppCompatActivity {

    private Subscription mSubscription;
    private Observer<AllCategoryBean> mObserver=new Observer<AllCategoryBean>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("OnError:"+e.getMessage());
            saveCategoryToDB(null);
        }

        @Override
        public void onNext(AllCategoryBean allCategoryBean) {
            if (allCategoryBean!=null&&"200".equals(allCategoryBean.getRetCode())){
                List<CategoryEntity> categoryEntities=new ArrayList<>();
                List<AllCategoryBean.ResultBean> result=allCategoryBean.getResult();
                for (int i=0;i<result.size();i++){
                    AllCategoryBean.ResultBean resultBean = result.get(i);
                    CategoryEntity categoryEntity = new CategoryEntity(null, resultBean.getName(), resultBean.getCid(), i);
                    categoryEntities.add(categoryEntity);
                }
                saveCategoryToDB(categoryEntities);
            }else {
                saveCategoryToDB(null);
            }
        }
    };

    private void saveCategoryToDB(List<CategoryEntity> categoryEntities) {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        if(savedInstanceState==null){
            //更换Fragment
           replaceTutorialFragment();
        }
        //第一次打开APP，将功能类别存储到本地数据库
        requestFunctionToDB();
        //第一次打开APP，将news的所有类别保存到本地数据库
        requestCategory();
    }

    private void requestCategory() {
        unsubscribe();
        mSubscription= NetWork.getmAllCategoryApi()
                .getAllCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void unsubscribe() {
        if (mSubscription!=null&&!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    private void requestFunctionToDB() {

    }

    private void replaceTutorialFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_welcome,new CustomPresentationPagerFragment())
                .commit();
    }

    public static void start(Context context) {
        SPUtils.put(context, Constant.FIRST_OPEN,true);
        context.startActivity(new Intent(context,WelcomeGuideActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
