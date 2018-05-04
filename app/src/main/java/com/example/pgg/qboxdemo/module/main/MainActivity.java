package com.example.pgg.qboxdemo.module.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCustomActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.global.MyApplication;
import com.example.pgg.qboxdemo.global.StatusBarCompat;
import com.example.pgg.qboxdemo.test.FindFragment;
import com.example.pgg.qboxdemo.test.MeFragment;
import com.example.pgg.qboxdemo.test.NewsFragment;
import com.example.pgg.qboxdemo.test.WechatFragment;
import com.example.pgg.qboxdemo.utils.AppUtils;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.utils.StateBarTranslucentUtils;
import com.example.pgg.qboxdemo.widget.TabBar_Main;
import com.orhanobut.logger.Logger;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseCustomActivity {

    public static List<String> logList=new CopyOnWriteArrayList<>();

    private static final String NEWS_FRAGMENT="NEWS_FRAGMENT";
    private static final String WECHAT_FRAGMENT = "WECHAT_FRAGMENT";
    private static final String FIND_FRAGMENT = "FIND_FRAGMENT";
    public static final String ME_FRAGMENT = "ME_FRAGMENT";
    public static final String FROM_FLAG = "FROM_FLAG";

    @BindView(R.id.frameLayout_main)
    FrameLayout sFrameLayoutMain;
    @BindView(R.id.recommend_main)
    TabBar_Main sRecommendMain;
    @BindView(R.id.cityfinder_mains)
    TabBar_Main sCityfinderMains;
    @BindView(R.id.findtravel_mains)
    TabBar_Main sFindtravelMains;
    @BindView(R.id.me_mains)
    TabBar_Main sMeMains;

    public MeFragment mMeFragment;
    public NewsFragment mNewsFragment;
    public WechatFragment mWechatFragment;
    public FindFragment mFindFragment;

    private FragmentManager sBaseFragmentManager;
    boolean isRestart=false;

    /**
     * 存储当前Fragment的标记
     */
    private String mCurrentIndex;
    private boolean mIsExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initContentView() {
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        setContentView(R.layout.activity_main);
        MyApplication.setMainActivity(this);
        StatusBarCompat.compat(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        sBaseFragmentManager=getBaseFragmentManager();

        String startPage=NEWS_FRAGMENT;
        String s=(String) SPUtils.get(this, Constant.OPENNEWS,"nomagic");

        if (s.equals("magicopen")){
            sRecommendMain.setVisibility(View.VISIBLE);
            startPage=NEWS_FRAGMENT;
        }
        if (savedInstanceState!=null){
            initByRestart(savedInstanceState);
        }else {
            switchToFragment(startPage);
            mCurrentIndex=startPage;
        }

        int qbox_version=(int)SPUtils.get(this,Constant.QBOX_NEW_VERSION,0);
        if (qbox_version!=0&&qbox_version> AppUtils.getVersionCode(this)){
            //todo 通知提示升级

        }

        //EventBus.getDefault().register(this);
    }



    private void switchToFragment(String index) {
        hideAllFragment();
        switch (index){
            case NEWS_FRAGMENT:
                if (sRecommendMain.getVisibility()==View.VISIBLE){
                    showNewsFragment();
                    Logger.e("newsopen:101");
                }
                break;
            case WECHAT_FRAGMENT:
                showWechatFragment();
                break;
            case FIND_FRAGMENT:
                showFindFragment();
                break;
            case ME_FRAGMENT:
                showMeFragment();
                break;
            default:
                break;
        }
        mCurrentIndex=index;
    }

    private void showMeFragment() {
        if (false==sMeMains.isSelected()){
            sMeMains.setSelected(true);
        }
        if (mMeFragment==null){
            mMeFragment=MeFragment.newInstance();
            addFragment(R.id.frameLayout_main,mMeFragment,ME_FRAGMENT);
        }else if (isRestart==true){
            getFragmentTransaction().show(mMeFragment).commit();
            isRestart=false;
        }else {
            showFragment(mMeFragment);
        }
    }

    private void showFindFragment() {
        if (false==sFindtravelMains.isSelected()){
            sFindtravelMains.setSelected(true);
        }
        if (mFindFragment==null){
            mFindFragment=new FindFragment();
            addFragment(R.id.frameLayout_main,mFindFragment,FIND_FRAGMENT);
        }else if (isRestart=true){
            isRestart=false;
            getFragmentTransaction().show(mFindFragment).commit();
        }else {
            showFragment(mFindFragment);
        }
    }

    private void showWechatFragment() {
        if (false==sCityfinderMains.isSelected()){
            sCityfinderMains.setSelected(true);
        }
        if (mWechatFragment==null){
            mWechatFragment=new WechatFragment();
            addFragment(R.id.frameLayout_main,mWechatFragment,WECHAT_FRAGMENT);
        }else if (isRestart=true){
            isRestart=false;
            getFragmentTransaction().show(mWechatFragment).commit();
        }else {
            showFragment(mWechatFragment);
        }
    }

    private void showNewsFragment() {
        if (sRecommendMain.getVisibility()!=View.VISIBLE){
            return;
        }
        if (false==sRecommendMain.isSelected()){
            sRecommendMain.setSelected(true);
        }
        if (mNewsFragment==null){
            Logger.e("恢复状态："+"null");
            mNewsFragment=new NewsFragment();
            addFragment(R.id.frameLayout_main,mNewsFragment,NEWS_FRAGMENT);
        }else if (isRestart=true){
            isRestart=false;
            getFragmentTransaction().show(mNewsFragment).commit();
        }else {
            showFragment(mNewsFragment);
        }
    }

    private void hideAllFragment() {
        if (mNewsFragment!=null){
            hideFragment(mNewsFragment);
        }
        if (mWechatFragment!=null){
            hideFragment(mWechatFragment);
        }
        if (mFindFragment!=null){
            hideFragment(mFindFragment);
        }
        if (mMeFragment!=null){
            hideFragment(mMeFragment);
        }
        if (sRecommendMain.getVisibility()==View.VISIBLE){
            sRecommendMain.setSelected(false);
        }
        sFindtravelMains.setSelected(false);
        sCityfinderMains.setSelected(false);
        sMeMains.setSelected(false);

    }

    private void initByRestart(Bundle savedInstanceState) {
        mCurrentIndex=savedInstanceState.getString("mCurrentIndex");

        isRestart=true;
        Logger.e("恢复状态"+mCurrentIndex);
        mMeFragment= (MeFragment) sBaseFragmentManager.findFragmentByTag(ME_FRAGMENT);
        if (sRecommendMain.getVisibility()==View.VISIBLE){
            mNewsFragment= (NewsFragment) sBaseFragmentManager.findFragmentByTag(NEWS_FRAGMENT);
        }
        mWechatFragment= (WechatFragment) sBaseFragmentManager.findFragmentByTag(WECHAT_FRAGMENT);
        mFindFragment= (FindFragment) sBaseFragmentManager.findFragmentByTag(FIND_FRAGMENT);

        switchToFragment(mCurrentIndex);
    }

    @OnClick({R.id.recommend_main,R.id.cityfinder_mains,R.id.findtravel_mains,R.id.me_mains})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.recommend_main:
                if (!mCurrentIndex.equals(NEWS_FRAGMENT)) {
                    switchToFragment(NEWS_FRAGMENT);
                }
                break;
            case R.id.cityfinder_mains:
                if (!mCurrentIndex.equals(WECHAT_FRAGMENT)){
                    switchToFragment(WECHAT_FRAGMENT);
                }
                break;
            case R.id.findtravel_mains:
                if (!mCurrentIndex.equals(FIND_FRAGMENT)){
                    switchToFragment(FIND_FRAGMENT);
                }
                break;
            case R.id.me_mains:
                if (!mCurrentIndex.equals(ME_FRAGMENT)){
                    switchToFragment(ME_FRAGMENT);
                }
                break;
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.NEWSFRAGMENT_CATEGORYACTIVITY_REQUESTCODE&&resultCode==Constant.NEWSFRAGMENT_CATEGORYACTIVITY_RESULTCODE){
            mNewsFragment.initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //b EventBus.getDefault().unregister(this);
        for (Fragment fragment:getBaseFragmentManager().getFragments()){
            getFragmentTransaction().remove(fragment);
        }
        MyApplication.setMainActivity(null);
        //todo 解决Android输入法造成的内存泄漏问题
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }

    private void refreshLogInfo() {
        String AllLog="";
        for (String log:logList){
            AllLog=AllLog+log+"\n\n";
        }
        Logger.e(AllLog);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mCurrentIndex",mCurrentIndex);
        Logger.e("保存状态");
    }

    /**
     * 监听手机的返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (mIsExit){
                this.finish();
            }else {
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                mIsExit=true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit=false;
                    }
                },2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 优雅的退出程序，当有其他地方退出应用时，会先返回到此页面在执行退出
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null){
            boolean isExit=intent.getBooleanExtra(Constant.TAG_EXIT,false);
            if (isExit){
                finish();
            }
        }
    }
}
