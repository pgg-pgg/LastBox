package com.example.pgg.qboxdemo.me;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.utils.SPUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.RegisterView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by pgg on 2018/5/14.
 */

public class AccountActivity extends BaseCommonActivity implements CompoundButton.OnCheckedChangeListener, PlatformActionListener, Handler.Callback {

    private String[] sBindPhoneText;
    private Drawable[] sBindPhoneImg;
    private int[] sBindPhoneTextColor;

    public static final String QQ_NAME = "QQ";
    public static final String WECHAT_NAME = "Wechat";
    public static final String SINAWEIBO_NAME = "SinaWeibo";

    public static final int IS_BIND_PHONE = 1;
    public static final int NOT_BIND_PHONE = 0;

    @BindView(R.id.toolbar_account)
    Toolbar mToolbarAccount;
    @BindView(R.id.phone_account)
    LinearLayout mPhoneAccount;
    @BindView(R.id.changepassword_account)
    LinearLayout mChangepasswordAccount;
    @BindView(R.id.weiboname_account)
    TextView mWeibonameAccount;
    @BindView(R.id.switch_weibo_account)
    Switch mSwitchWeiboAccount;
    @BindView(R.id.weibo_account)
    LinearLayout mWeiboAccount;
    @BindView(R.id.wechatname_account)
    TextView mWechatnameAccount;
    @BindView(R.id.switch_wechat_account)
    Switch mSwitchWechatAccount;
    @BindView(R.id.wechat_account)
    LinearLayout mWechatAccount;
    @BindView(R.id.qqname_account)
    TextView mQqnameAccount;
    @BindView(R.id.switch_qq_account)
    Switch mSwitchQqAccount;
    @BindView(R.id.qq_account)
    LinearLayout mQqAccount;
    @BindView(R.id.phonebindtext_account)
    TextView mPhonebindtextAccount;
    @BindView(R.id.phonebindimg_account)
    ImageView mPhonebindimgAccount;
    @BindView(R.id.phonebindinfo_account)
    TextView mPhonebindinfoAccount;

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_account);
    }

    @Override
    public void initView() {
        sBindPhoneText=new String[]{"尚未验证","已验证"};
        sBindPhoneImg=new Drawable[]{getResources().getDrawable(R.drawable.certification_no), getResources().getDrawable(R.drawable.certification_yes)};
        sBindPhoneTextColor=new int[]{getResources().getColor(R.color.secondary_text), getResources().getColor(R.color.colorAccent)};
        initToolBar();
        initSwitch();
        initPhone();
        Toast.makeText(this, "账号系统未开通，本页面的所有信息只会保存在本地！", Toast.LENGTH_LONG).show();
    }

    private void initPhone() {
        String userPhone = (String) SPUtils.get(this, Constant.USER_PHONE, "");
        if (TextUtils.isEmpty(userPhone)){
        }else {
            showBindPhone(IS_BIND_PHONE,userPhone);
        }
    }

    private void showBindPhone(int isBindPhone, String userPhone) {
        mPhonebindtextAccount.setText(sBindPhoneText[isBindPhone]);
        mPhonebindimgAccount.setImageDrawable(sBindPhoneImg[isBindPhone]);
        mPhonebindtextAccount.setTextColor(sBindPhoneTextColor[isBindPhone]);

        if (TextUtils.isEmpty(userPhone)||userPhone.equals("绑定手机，账号更安全")){
            mPhonebindinfoAccount.setText("绑定手机，账号更安全");
        }else {
            StringBuffer formatPhone=new StringBuffer(userPhone.substring(0,3))
                    .append("****")
                    .append(userPhone.substring(userPhone.length()-4,userPhone.length()));
            mPhonebindinfoAccount.setText(formatPhone.toString());
        }
    }

    private void initSwitch() {
        if (!TextUtils.isEmpty((String) SPUtils.get(this, Constant.QQ_USERICON,""))){
            mQqnameAccount.setText((String)SPUtils.get(this,Constant.QQ_USERNAME,""));
            mSwitchQqAccount.setChecked(true);
        }
        if (!TextUtils.isEmpty((String) SPUtils.get(this, Constant.SINAWEIBO_USERID, ""))) {
            mWeibonameAccount.setText((String) SPUtils.get(this, Constant.SINAWEIBO_USERNAME, ""));
            mSwitchWeiboAccount.setChecked(true);
        }
        if (!TextUtils.isEmpty((String) SPUtils.get(this, Constant.WECHAT_USERID, ""))) {
            mWechatnameAccount.setText((String) SPUtils.get(this, Constant.WECHAT_USERNAME, ""));
            mSwitchWechatAccount.setChecked(true);
        }

        mSwitchQqAccount.setOnCheckedChangeListener(this);
        mSwitchWechatAccount.setOnCheckedChangeListener(this);
        mSwitchWeiboAccount.setOnCheckedChangeListener(this);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbarAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.phone_account, R.id.changepassword_account, R.id.weibo_account, R.id.wechat_account, R.id.qq_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_account:
                Toast.makeText(this, "目前账号系统未开通，有望在新的版本中加入！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changepassword_account:
                Toast.makeText(this, "目前账号系统未开通，有望在新的版本中加入！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.weibo_account:
                mSwitchWeiboAccount.setChecked(!mSwitchWeiboAccount.isChecked());
                break;
            case R.id.wechat_account:
                mSwitchWechatAccount.setChecked(!mSwitchWechatAccount.isChecked());
                break;
            case R.id.qq_account:
                mSwitchQqAccount.setChecked(!mSwitchQqAccount.isChecked());
                break;
        }
    }

}
