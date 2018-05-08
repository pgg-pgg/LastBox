package com.example.pgg.qboxdemo.module.weather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.City;
import com.example.pgg.qboxdemo.network.NetWork;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.logger.Logger;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnChipClickListener;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pgg on 2018/5/8.
 */

public class AddressActivity extends BaseCommonActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    @BindView(R.id.context_address)
    LinearLayout mContextAddress;
    @BindView(R.id.hotcity_grid)
    GridView mHotCityGrid;

    private Subscription mSubscription;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_address);
    }

    @Override
    public void initView() {
        initToolbar();
        initGridView();
        initSearchView();
    }

    private void initSearchView() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.color_cursor_white);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.arrays_address));
        mSearchView.setSuggestionIcon(getResources().getDrawable(R.drawable.ic_add_location_black_24dp));

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestData(query);
                Logger.e("search-submit"+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.e("search-change:" + newText);
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }

    private void initGridView() {
        SimpleAdapter adapter=new SimpleAdapter(this,getData(),R.layout.item_address_activity,new String[]{"address"},new int[]{R.id.item_address});
        mHotCityGrid.setAdapter(adapter);
        mHotCityGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String searchText=((TextView)view).getText().toString();
                requestData(searchText);
            }
        });

    }

    private ArrayList<HashMap<String, String>> getData() {
        String[] stringArray=getResources().getStringArray(R.array.arrays_address);
        ArrayList<HashMap<String,String>> list=new ArrayList<>();
        HashMap<String,String> map=null;
        for (int i=1;i<stringArray.length;i++){
            map=new HashMap<>();
            map.put("address",stringArray[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.address,menu);

        MenuItem item = menu.findItem(R.id.search_address);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        }else {
            super.onBackPressed();
        }
    }

    private void requestData(String searchText) {
        unsubscribe();
        mSubscription= NetWork.getCityApi()
                .getCity("def9a507328e4cd395d983fe2589586e",searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<City>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(City city) {
                        showAddress(city.getHeWeather5());
                    }
                });
    }

    private void showAddress(List<City.HeWeather5Bean> heWeather5) {
        if (mContextAddress.getChildCount()!=0){
            mContextAddress.removeAllViews();
        }
        for (int i=0;i<heWeather5.size();i++){
            City.HeWeather5Bean.BasicBean basicBean=null;
            if ("ok".equals(heWeather5.get(i).getStatus())){
                basicBean=heWeather5.get(i).getBasic();
                Logger.e(basicBean.getId());
            }else {
                continue;
            }
            final Chip chip=new Chip(this);
            chip.setHasIcon(true);
            chip.setTextColor(Color.WHITE);

            chip.setChipText(basicBean.getProv()+"-"+basicBean.getCity());
            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_add_location_black_24dp));
            chip.setSelectable(true);
            final City.HeWeather5Bean.BasicBean finalBasicBean = basicBean;
            chip.setOnChipClickListener(new OnChipClickListener() {
                @Override
                public void onChipClick(View v) {
                    Toast.makeText(AddressActivity.this, finalBasicBean.toString() + "\n\n点击勾选即可确定选择", Toast.LENGTH_SHORT).show();
                }
            });
            chip.setOnSelectClickListener(new OnSelectClickListener() {
                @Override
                public void onSelectClick(View v, boolean selected) {
                    if (selected){
                        Intent intent=new Intent();
                        intent.putExtra("data",finalBasicBean);
                        setResult(Constant.RESULTES_CODE_ADDRESS,intent);
                        EventBus.getDefault().post(finalBasicBean);
                        finish();
                    }
                }
            });
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chip.requestLayout();
                }
            },250);
            mContextAddress.addView(chip);
        }
    }

    private void unsubscribe() {
        if (mSubscription!=null&&!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initPresenter() {

    }
}
