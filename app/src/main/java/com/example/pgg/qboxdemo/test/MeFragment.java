package com.example.pgg.qboxdemo.test;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.me.CalendarActivity;
import com.example.pgg.qboxdemo.me.FlashActivity;
import com.example.pgg.qboxdemo.me.LEDActivity;
import com.example.pgg.qboxdemo.me.SettingActivity;
import com.example.pgg.qboxdemo.me.UserInfoActivity;
import com.example.pgg.qboxdemo.me.ZxingActivity;
import com.example.pgg.qboxdemo.me.weather.WeatherActivity;
import com.example.pgg.qboxdemo.utils.SPUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pgg on 2018/5/4.
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.motto_me)
    TextView motto_me;

    @BindView(R.id.userhead_me)
    CircleImageView userhead_me;

    @BindView(R.id.username_me)
    TextView username_me;

    @BindView(R.id.rili_me)
    LinearLayout rili_me;

    @BindView(R.id.tianqi_me)
    LinearLayout tianqi_me;

    @BindView(R.id.led_me)
    LinearLayout led_me;

    @BindView(R.id.sdt_me)
    LinearLayout sdt_me;

    @BindView(R.id.erweima_me)
    LinearLayout erweima_me;

    @BindView(R.id.setting_me)
    LinearLayout setting_me;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    public MeFragment(){

    }

    public static MeFragment newInstance(){
        MeFragment fragment=new MeFragment();
        return fragment;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me_new;
    }

    @Override
    public void initView() {

        initUserInfo();
    }

    private void initUserInfo() {
        String username = (String) SPUtils.get(getContext(), Constant.USER_NAME, "");
        String userhader = (String) SPUtils.get(getContext(), Constant.USER_HEADER, "");
        String usergeyan = (String) SPUtils.get(getContext(), Constant.USER_GEYAN, "我愿做你世界里的太阳，给你温暖。");
        if (!TextUtils.isEmpty(username)) {
            username_me.setText(username);
        }
        if (!TextUtils.isEmpty(userhader)) {
            Glide.with(getContext()).load(new File(userhader)).into(userhead_me);
        }
        if (!TextUtils.isEmpty(usergeyan)) {
            motto_me.setText(usergeyan);
        }
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSimpleName()+"-" +this.getClass().getSimpleName();
    }

    @OnClick({R.id.rili_me, R.id.tianqi_me, R.id.led_me, R.id.sdt_me, R.id.erweima_me, R.id.setting_me, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rili_me:
                startActivity(new Intent(getContext(),CalendarActivity.class));
                break;
            case R.id.tianqi_me:
                startActivity(new Intent(getContext(), WeatherActivity.class));
                break;
            case R.id.led_me:
                startActivity(new Intent(getContext(),LEDActivity.class));
                break;
            case R.id.sdt_me:
                startActivity(new Intent(getContext(),FlashActivity.class));
                break;
            case R.id.erweima_me:
                startActivity(new Intent(getContext(),ZxingActivity.class));
                break;
            case R.id.setting_me:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fab:
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),userhead_me, getString(R.string.transition_userhead));
                ActivityCompat.startActivity(getContext(),intent,optionsCompat.toBundle());
                break;
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
