package com.example.pgg.qboxdemo.me;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.me.adapter.AreaSetAdapter;
import com.example.pgg.qboxdemo.model.entities.City;
import com.example.pgg.qboxdemo.module.weather.AddressActivity;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.pgg.qboxdemo.module.weather.api.ApiManager.KEY_SELECTED_AREA;

/**
 * Created by pgg on 2018/5/14.
 */

public class WeatherAddressActivity extends BaseCommonActivity{

    @BindView(R.id.recy_area_set)
    RecyclerView mRecyAreaSet;
    @BindView(R.id.fab_area_set)
    FloatingActionButton mFabAreaSet;

    AreaSetAdapter mAreaSetAdapter;
    ArrayList<ApiManager.Area> mSelectedAreas;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_weather_address);
    }

    @Override
    public void initView() {
        initToolbar();
        EventBus.getDefault().register(this);

        initRecy();
    }

    private void initRecy() {
        mSelectedAreas=ApiManager.loadSelectedArea(this);
        mRecyAreaSet.setLayoutManager(new LinearLayoutManager(this));
        mAreaSetAdapter=new AreaSetAdapter(mSelectedAreas,getResources().getStringArray(R.array.bgimg));

        ItemDragAndSwipeCallback itemDragAndSwipeCallback=new ItemDragAndSwipeCallback(mAreaSetAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyAreaSet);
        //开启滑动删除
        mAreaSetAdapter.enableSwipeItem();
        mAreaSetAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                ApiManager.Area area=mSelectedAreas.get(pos);
                removeAddress(area);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        mRecyAreaSet.setAdapter(mAreaSetAdapter);
    }

    private void removeAddress(ApiManager.Area area) {
        List<ApiManager.Area> areas=new ArrayList<>();
        String s = (String) SPUtils.get(this, KEY_SELECTED_AREA, "");
        if (!TextUtils.isEmpty(s)){
            ApiManager.Area[] aa=new Gson().fromJson(s, ApiManager.Area[].class);
            if (aa!=null){
                Collections.addAll(areas,aa);
            }
        }

        for (ApiManager.Area areaitem:areas){
            if (area.getId().equals(areaitem.getId())){
                areas.remove(areaitem);
                break;
            }
        }
        SPUtils.put(getContext(),KEY_SELECTED_AREA,new Gson().toJson(areas));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weatheraddress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventCome(City.HeWeather5Bean.BasicBean BasicBean) {

        List<ApiManager.Area> areas = new ArrayList<>();
        String s = (String) SPUtils.get(this, KEY_SELECTED_AREA, "");
        if (!TextUtils.isEmpty(s)) {
            ApiManager.Area[] aa = new Gson().fromJson(s, ApiManager.Area[].class);
            if (aa != null) {
                Collections.addAll(areas, aa);
            }
        }

        boolean flag = false;
        for (ApiManager.Area area : areas) {
            if (BasicBean.getId().equals(area.getId())) {
                flag = true;
                break;
            }
        }

        ApiManager.Area e = new ApiManager.Area();

        if (!flag) {
            e.setCity(BasicBean.getCity());
            e.setProvince(BasicBean.getProv());
            e.setId(BasicBean.getId());
            e.setName_cn(BasicBean.getCity());
            areas.add(e);
        }

        SPUtils.put(this, KEY_SELECTED_AREA, new Gson().toJson(areas));
        initRecy();
    }

    @OnClick(R.id.fab_area_set)
    public void onViewClicked() {
        if (mAreaSetAdapter.getData().size() < 3) {
            startActivity(new Intent(this, AddressActivity.class));
        } else {
            Toast.makeText(this, "对不起！最多支持添加3个地点。", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void initPresenter() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
