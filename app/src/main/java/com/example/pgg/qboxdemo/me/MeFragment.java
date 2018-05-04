package com.example.pgg.qboxdemo.me;

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
import com.example.pgg.qboxdemo.me.weather.WeatherActivity;
import com.example.pgg.qboxdemo.model.entities.RefreshMeFragmentEvent;
import com.example.pgg.qboxdemo.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pgg on 2018/5/3.
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.homebg_me)
    ImageView mHomebgMe;
    @BindView(R.id.motto_me)
    TextView mMottoMe;
    @BindView(R.id.userhead_me)
    CircleImageView mUserheadMe;
    @BindView(R.id.username_me)
    TextView mUsernameMe;
    @BindView(R.id.rili_me)
    LinearLayout mRiliMe;
    @BindView(R.id.tianqi_me)
    LinearLayout mTianqiMe;
    @BindView(R.id.led_me)
    LinearLayout mLedMe;
    @BindView(R.id.sdt_me)
    LinearLayout mSdtMe;
    @BindView(R.id.erweima_me)
    LinearLayout mErweimaMe;
    @BindView(R.id.setting_me)
    LinearLayout mSettingMe;
    @BindView(R.id.fab)
    FloatingActionButton mFab;


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

        //订阅事件
        EventBus.getDefault().register(this);
        initUserInfo();
    }

    private void initUserInfo() {
        String username=(String) SPUtils.get(getContext(), Constant.USER_NAME,"");
        String userhader = (String) SPUtils.get(getContext(), Constant.USER_HEADER, "");
        String usergeyan = (String) SPUtils.get(getContext(), Constant.USER_GEYAN, "我愿做你世界里的太阳，给你温暖。");
        if (!TextUtils.isEmpty(username)){
            mUsernameMe.setText(username);
        }
        if (!TextUtils.isEmpty(userhader)){
            Glide.with(getContext()).load(new File(userhader)).into(mUserheadMe);
        }
        if (!TextUtils.isEmpty(usergeyan)){
            mMottoMe.setText(usergeyan);
        }
    }

    @Override
    protected void managerArguments() {

    }

    @OnClick({R.id.rili_me,R.id.tianqi_me,R.id.led_me,R.id.sdt_me,R.id.erweima_me,R.id.setting_me,R.id.fab})
    public void onClick(View view){
        switch (view.getId()){
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
                startActivity(new Intent(getContext(),SettingActivity.class));
                break;
            case R.id.fab:
                Intent intent=new Intent(getContext(),UserInfoActivity.class);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mUserheadMe, getString(R.string.transition_userhead));
                ActivityCompat.startActivity(getContext(),intent,activityOptionsCompat.toBundle());
                break;

        }
    }

    public void OnRefreshUserInfo(RefreshMeFragmentEvent event){
        if (event.getMark_code()==0x1000){
            initUserInfo();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSuperclass().getName()+"-"+this.getClass().getName();
    }
}
