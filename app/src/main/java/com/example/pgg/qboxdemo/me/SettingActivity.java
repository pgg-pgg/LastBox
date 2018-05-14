package com.example.pgg.qboxdemo.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.WebViewActivity;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.module.main.MainActivity;
import com.example.pgg.qboxdemo.update.UpdateChecker;
import com.example.pgg.qboxdemo.utils.AppUtils;
import com.example.pgg.qboxdemo.utils.DataCleanManager;
import com.example.pgg.qboxdemo.utils.FileInfoUtils;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.mob.MobSDK;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.example.pgg.qboxdemo.utils.FileInfoUtils.getFileSize;

/**
 * Created by pgg on 2018/5/3.
 */

public class SettingActivity extends BaseCommonActivity implements CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.switch_sllms_setting)
    Switch mSwitchSllmsSetting;
    @BindView(R.id.sllms_setting)
    LinearLayout mSllmsSetting;
    @BindView(R.id.qchc_setting)
    LinearLayout mQchcSetting;
    @BindView(R.id.switch_xxts_setting)
    Switch mSwitchXxtsSetting;
    @BindView(R.id.xxts_setting)
    LinearLayout mXxtsSetting;
    @BindView(R.id.switch_zdyts_setting)
    Switch mSwitchZdytsSetting;
    @BindView(R.id.zdyts_setting)
    LinearLayout mZdytsSetting;
    @BindView(R.id.zhyaq_setting)
    LinearLayout mZhyaqSetting;
    @BindView(R.id.kyxk_setting)
    LinearLayout mKyxkSetting;
    @BindView(R.id.jcgx_setting)
    LinearLayout mJcgxSetting;
    @BindView(R.id.sm_setting)
    TextView mSmSetting;
    @BindView(R.id.yjfk_setting)
    TextView mYjfkSetting;
    @BindView(R.id.aboutefall_setting)
    TextView mAboutefallSetting;
    @BindView(R.id.share_setting)
    TextView mShareSetting;
    @BindView(R.id.exit_setting)
    TextView mExitSetting;
    @BindView(R.id.author_setting)
    TextView mAuthorSetting;
    @BindView(R.id.qlcz_setting)
    LinearLayout mQlczSetting;
    @BindView(R.id.text_qchc_setting)
    TextView mTextQchcSetting;
    @BindView(R.id.version_setting)
    TextView mVersionSetting;
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_sllms_setting:
                SPUtils.put(this, Constant.SLLMS, isChecked);
                break;
            case R.id.switch_xxts_setting:
                if (isChecked) {
                    SPUtils.put(this, Constant.XXTS, isChecked);
                } else {
                    closeNoti();
                }
                break;
            case R.id.switch_zdyts_setting:
                SPUtils.put(this, Constant.ZDYTS, isChecked);
                break;
            default:
                break;
        }
    }

    private void closeNoti() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setIcon(R.drawable.love_guide)
                .setMessage("胖哥哥飘过：\n “本软件是个人开发，不用于商业用途，绝不会向使用者推送广告等性质的消息。\n\n 本功能主要是为了作者和使用者之间沟通和共享一些有趣的信息，请大家尽量不要关闭。\n\n 如果你真的收到了烦扰的消息再关闭也不迟呀！”")
                .setNegativeButton("相信真爱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSwitchXxtsSetting.setChecked(true);
                    }
                })
                .setPositiveButton("确定关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(SettingActivity.this, Constant.XXTS, false);
                    }
                })
                .setCancelable(false)//点击其他地方不起作用，强制点击按钮
                .create();

        alertDialog.show();
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        MobSDK.init(this);
        initToolbar();
        initSwitch();
        initVersion();
    }

    private void initVersion() {
        mVersionSetting.setText(new StringBuffer("当前版本：")
                .append(AppUtils.getVersionName(getApplicationContext()))
                .append("（")
                .append(AppUtils.getVersionCode(getApplicationContext()))
                .append("）")
                .toString());
    }

    private void initSwitch() {
        showCacheSize();
        mSwitchSllmsSetting.setChecked((boolean) SPUtils.get(this, Constant.SLLMS, false));
        mSwitchXxtsSetting.setChecked((boolean) SPUtils.get(this, Constant.XXTS, true));
        mSwitchZdytsSetting.setChecked((boolean) SPUtils.get(this, Constant.ZDYTS, true));

        mSwitchZdytsSetting.setOnCheckedChangeListener(this);
        mSwitchXxtsSetting.setOnCheckedChangeListener(this);
        mSwitchSllmsSetting.setOnCheckedChangeListener(this);
    }

    private void showCacheSize() {
        long externalCacheSize = 0;
        long cacheSize = 0;
        long filesSize = 0;

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                externalCacheSize = getFileSize(getExternalCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            cacheSize = getFileSize(SettingActivity.this.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            filesSize = FileInfoUtils.getFileSize(getFilesDir());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTextQchcSetting.setText(
                FileInfoUtils.FormetFileSize(externalCacheSize + cacheSize + filesSize));
    }

    boolean magic_step1 = false;
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SettingActivity.this, "触发魔法A，请继续寻找魔法", Toast.LENGTH_SHORT).show();
                magic_step1 = true;
                return false;
            }
        });
        mAuthorSetting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (magic_step1) {
                    Toast.makeText(SettingActivity.this, "触发魔法B,魔法开启完成", Toast.LENGTH_SHORT).show();
                    SPUtils.put(SettingActivity.this, Constant.OPENNEWS,"magicopen");
                }
                return false;
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.sllms_setting, R.id.qchc_setting, R.id.xxts_setting, R.id.qlcz_setting,
            R.id.zdyts_setting, R.id.zhyaq_setting, R.id.kyxk_setting, R.id.jcgx_setting,
            R.id.sm_setting, R.id.yjfk_setting, R.id.aboutefall_setting, R.id.share_setting,
            R.id.exit_setting, R.id.author_setting,R.id.weatheraddress_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sllms_setting:
                mSwitchSllmsSetting.setChecked(!mSwitchSllmsSetting.isChecked());
                break;
            case R.id.qchc_setting://清除 内外 cache/、files/ 3个文件夹里的数据。
                DataCleanManager.cleanInternalCache(this);
                DataCleanManager.cleanExternalCache(this);
                DataCleanManager.cleanFiles(this);
                showCacheSize();
                break;
            case R.id.xxts_setting:
                mSwitchXxtsSetting.setChecked(!mSwitchXxtsSetting.isChecked());
                break;
            case R.id.zdyts_setting:
                mSwitchZdytsSetting.setChecked(!mSwitchZdytsSetting.isChecked());
                break;
            case R.id.zhyaq_setting:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.kyxk_setting:
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://github.com/pgg-pgg/LastBox/");
                bundle.putString("title", "开源许可");
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.jcgx_setting:
                UpdateChecker.checkForDialog(this);
                break;
            case R.id.sm_setting:
                startActivity(new Intent(this, StatementActivity.class));
                break;
            case R.id.yjfk_setting:
                Bundle bundle1 = new Bundle();
                bundle1.putString("url", "https://weibo.com/u/5715030564/home?wvr=5");
                bundle1.putString("title", "意见反馈（微博私信）");
                Intent intent1 = new Intent(getContext(), WebViewActivity.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.aboutefall_setting:
                startActivity(new Intent(this, AboutMeActivity.class));
                break;
            case R.id.share_setting:
                showShare();
                break;
            case R.id.exit_setting:
                exitApp();
                break;
            case R.id.author_setting:
                break;
            case R.id.qlcz_setting://强力清除
                Snackbar.make(mAppBar, "重置完成后，将会退出软件一次！", Snackbar.LENGTH_LONG)
                        .setAction("知道了，开始清理", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clearAllData();
                            }
                        }).show();
                break;
            case R.id.weatheraddress_setting:
                startActivity(new Intent(this,WeatherAddressActivity.class));
                break;
            default:
                break;
        }
    }

    private void clearAllData() {
        new AlertDialog.Builder(this)
                .setTitle("友情提示")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("此操作你将失去最后一盒中所有的数据和设置，回到初始安装状态，确定要这样做吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.cleanApplicationData(SettingActivity.this);
                        //关闭App
                        exitApp();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constant.TAG_EXIT, true);
        startActivity(intent);
    }

    private void showShare() {
        OnekeyShare oks=new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("最后一盒");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("https://github.com/pgg-pgg");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("给你推荐一个我淘到的很不错的App");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://github.com/pgg-pgg");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("功能很强大，设计的也很不错，也很纯净。");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://github.com/pgg-pgg");
        // 启动分享GUI
        oks.show(this);
    }
}
