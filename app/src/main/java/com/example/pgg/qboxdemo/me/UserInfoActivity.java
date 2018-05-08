package com.example.pgg.qboxdemo.me;

import com.bumptech.glide.Glide;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.model.entities.City;
import com.example.pgg.qboxdemo.model.entities.RefreshMeFragmentEvent;
import com.example.pgg.qboxdemo.module.weather.AddressActivity;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orhanobut.logger.Logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.pgg.qboxdemo.module.weather.api.ApiManager.KEY_SELECTED_AREA;

/**
 * Created by pgg on 2018/5/3.
 */

public class UserInfoActivity extends BaseCommonActivity implements android.support.v7.widget.Toolbar.OnMenuItemClickListener, TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.toolbar_userinfo)
    android.support.v7.widget.Toolbar mToolbarUserinfo;
    @BindView(R.id.userhead_img_userinfo)
    CircleImageView mUserheadImgUserinfo;
    @BindView(R.id.userheader_userinfo)
    FrameLayout mUserheaderUserinfo;
    @BindView(R.id.user_name_userinfo)
    TextInputEditText mUserNameUserinfo;
    @BindView(R.id.radiobutton_man_userinfo)
    RadioButton mRadiobuttonManUserinfo;
    @BindView(R.id.radiobutton_woman_userinfo)
    RadioButton mRadiobuttonWomanUserinfo;
    @BindView(R.id.user_geyan_userinfo)
    TextInputEditText mUserGeyanUserinfo;
    @BindView(R.id.radiogroup_sex_userinfo)
    RadioGroup mRadiogroupSexUserinfo;
    @BindView(R.id.starspinner_userinfo)
    Spinner mStarspinnerUserinfo;
    @BindView(R.id.user_address_userinfo)
    TextInputEditText mUserAddressUserinfo;

    City.HeWeather5Bean.BasicBean cityBean = null;
    public String mHeaderAbsolutePath;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    public final static int mMessageFlag = 0x1010;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mMessageFlag:
                    Glide.with(UserInfoActivity.this).load(((File) msg.obj)).into(mUserheadImgUserinfo);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Constant.REQUEST_CODE_ADDRESS&&resultCode==Constant.RESULTES_CODE_ADDRESS){
            cityBean=data.getParcelableExtra("data");
            Logger.e(cityBean.getProv()+"-"+cityBean.getCity());
            mUserAddressUserinfo.setText(cityBean.getProv()+"-"+cityBean.getCity());
        }else {
            //添加头像
            getTakePhoto().onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Editable textName = mUserNameUserinfo.getText();
            if (TextUtils.isEmpty(textName)) {
                mUserNameUserinfo.setError("姓名不应为空");
                return false;
            }
            if (textName.length() >= 3) {
                SPUtils.put(this, Constant.USER_NAME, textName.toString());
            } else {
                mUserNameUserinfo.setError("长度不应小于3个字符");
                return false;
            }

            Editable textGeyan = mUserGeyanUserinfo.getText();
            if (!TextUtils.isEmpty(textGeyan)) {
                SPUtils.put(this, Constant.USER_GEYAN, textGeyan.toString());
            } else {
                SPUtils.remove(this, Constant.USER_GEYAN);
            }

            SPUtils.put(this, Constant.USER_SEX, mRadiogroupSexUserinfo.getCheckedRadioButtonId() == R.id.radiobutton_man_userinfo ? true : false);
            SPUtils.put(this, Constant.USER_STAR, getStarSelect());
            if (!TextUtils.isEmpty(mUserAddressUserinfo.getText())) {
                SPUtils.put(this, Constant.USER_ADDRESS, mUserAddressUserinfo.getText().toString());

                /**
                 * city : 北京
                 * cnty : 中国
                 * id : CN101010100
                 * lat : 39.904000
                 * lon : 116.391000
                 * prov : 直辖市
                 */
                if (cityBean != null) {
                    Logger.e("地点：" + cityBean.toString());

                    List<ApiManager.Area> areas=new ArrayList<>();
                    String s = (String) SPUtils.get(this, KEY_SELECTED_AREA, "");
                    if (!TextUtils.isEmpty(s)){
                        ApiManager.Area[] aa=new Gson().fromJson(s,ApiManager.Area[].class);
                        if (aa!=null){
                            Collections.addAll(areas,aa);
                        }
                    }
                    String nowAddress = (String) SPUtils.get(this, Constant.USER_ADDRESS_ID, "");
                    if (!TextUtils.isEmpty(nowAddress)) {
                        for (ApiManager.Area area : areas) {
                            if (nowAddress.equals(area.getId())) {
                                area.setId(cityBean.getId());
                                area.setCity(cityBean.getCity());
                                area.setName_cn(cityBean.getCity());
                                area.setProvince(cityBean.getProv());
                                break;
                            }
                        }
                    }else {
                        ApiManager.Area e = new ApiManager.Area();
                        e.setCity(cityBean.getCity());
                        e.setProvince(cityBean.getProv());
                        e.setId(cityBean.getId());
                        e.setName_cn(cityBean.getCity());
                        areas.add(e);
                    }

                    SPUtils.put(this,KEY_SELECTED_AREA,new Gson().toJson(areas));

                    SPUtils.put(this, Constant.USER_ADDRESS_CITY, cityBean.getCity());
                    SPUtils.put(this, Constant.USER_ADDRESS_CNTY, cityBean.getCnty());
                    SPUtils.put(this, Constant.USER_ADDRESS_ID, cityBean.getId());
                    SPUtils.put(this, Constant.USER_ADDRESS_LAT, cityBean.getLat());
                    SPUtils.put(this, Constant.USER_ADDRESS_LON, cityBean.getLon());
                    SPUtils.put(this, Constant.USER_ADDRESS_PROV, cityBean.getProv());
                }
            }else {
                SPUtils.remove(this, Constant.USER_ADDRESS);
                SPUtils.remove(this, Constant.USER_ADDRESS_CNTY);
                SPUtils.remove(this, Constant.USER_ADDRESS_CITY);
                SPUtils.remove(this, Constant.USER_ADDRESS_ID);
                SPUtils.remove(this, Constant.USER_ADDRESS_LAT);
                SPUtils.remove(this, Constant.USER_ADDRESS_LON);
                SPUtils.remove(this, Constant.USER_ADDRESS_PROV);
            }

            if (!TextUtils.isEmpty(mHeaderAbsolutePath)) {
                SPUtils.put(this, Constant.USER_HEADER, mHeaderAbsolutePath);
            }
            EventBus.getDefault().post(new RefreshMeFragmentEvent(0x1000));
            finish();
        }
        return false;
    }

    private String getStarSelect() {
        long selectedItemId = mStarspinnerUserinfo.getSelectedItemId();
        String[] stringArray = getResources().getStringArray(R.array.arrays_constellation);
        return stringArray[(int) selectedItemId];
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void initView() {
        initToolbar();
        initInfo();
    }

    private void initInfo() {
        String userName = (String) SPUtils.get(this, Constant.USER_NAME, "");
        boolean usergender = (boolean) SPUtils.get(this, Constant.USER_SEX, true);
        String usergeyan = (String) SPUtils.get(this, Constant.USER_GEYAN, "");
        String userstar = (String) SPUtils.get(this, Constant.USER_STAR, "");
        String useraddress = (String) SPUtils.get(this, Constant.USER_ADDRESS, "");
        String userheader = (String) SPUtils.get(this, Constant.USER_HEADER, "");

        if (!TextUtils.isEmpty(userName)) {
            mUserNameUserinfo.setText(userName);
        }
        mRadiogroupSexUserinfo.check(usergender == true ? R.id.radiobutton_man_userinfo : R.id.radiobutton_woman_userinfo);
        if (!TextUtils.isEmpty(usergeyan)) {
            mUserGeyanUserinfo.setText(usergeyan);
        }
        if (!TextUtils.isEmpty(useraddress)) {
            mUserAddressUserinfo.setText(useraddress);
        }
        if (!TextUtils.isEmpty(userstar)) {
            int indexOf = Arrays.asList(getResources().getStringArray(R.array.arrays_constellation)).indexOf(userstar);
            mStarspinnerUserinfo.setSelection(indexOf);
        }
        if (!TextUtils.isEmpty(userheader)) {
            File file = new File(userheader);
            Glide.with(this).load(file).into(mUserheadImgUserinfo);
        }

        mUserNameUserinfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 16) {
                    mUserNameUserinfo.setError("已达到最大长度");
                }
            }
        });

        mUserGeyanUserinfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 128) {
                    mUserGeyanUserinfo.setError("已达到最大长度");
                }
            }
        });

    }

    private void initToolbar() {
        mToolbarUserinfo.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbarUserinfo.inflateMenu(R.menu.userinfo_menu);
        mToolbarUserinfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbarUserinfo.setOnMenuItemClickListener(this);
    }


    @OnClick({R.id.userheader_userinfo, R.id.user_address_userinfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userheader_userinfo:
                showTakePhotoDialog();
                break;
            case R.id.user_address_userinfo:
                startActivityForResult(new Intent(UserInfoActivity.this, AddressActivity.class), Constant.REQUEST_CODE_ADDRESS);
                break;
            default:
                break;
        }
    }

    private void showTakePhotoDialog() {
        final String items[] ={"拍照","相册"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("选择头像");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                takeOrPickPhoto(i==0?true:false);
            }
        });
        builder.create().show();
    }

    private void takeOrPickPhoto(boolean isTakePhoto) {
        File file=new File(Environment.getExternalStorageDirectory(),"/temp/"+System.currentTimeMillis()+".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(file);
        TakePhoto takePhoto=getTakePhoto();

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        if (isTakePhoto){
            //如果选择了拍照
            if(true){
                //默认都裁剪
                takePhoto.onPickFromCaptureWithCrop(uri,getCropOption());
            }else {
                takePhoto.onPickFromCapture(uri);
            }
        }else {
            //如果选择了相册
            int limit=1;//选择图片个数
            if (limit>1){
                //当选择图片大于1时默认裁剪
                if (true){
                    takePhoto.onPickMultipleWithCrop(limit,getCropOption());
                }else {
                    takePhoto.onPickMultiple(limit);
                }
                return;
            }
            //是否从文件中选取图片
            if (false){
                if (true){
                    takePhoto.onPickFromDocumentsWithCrop(uri,getCropOption());
                }else {
                    takePhoto.onPickFromDocuments();
                }
                return;
            }else {
                if (true){
                    takePhoto.onPickFromGalleryWithCrop(uri,getCropOption());
                }else {
                    takePhoto.onPickFromGallery();
                }
            }
        }
    }

    /**
     * 配置剪裁选项
     * @return
     */
    private CropOptions getCropOption() {
        int height=100;
        int width=100;

        CropOptions.Builder builder=new CropOptions.Builder();

        //按照宽高比例裁剪
        builder.setAspectX(width).setAspectY(height);
        //是否使用Takephoto自带的剪裁工具
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    /**
     * 拍照相关配置
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //是否启用TakePhoto自带的相册
        builder.setWithOwnGallery(true);
        //纠正拍照的旋转角度
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 配置压缩选项
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        int maxSize=102400;
        int width=800;
        int height=800;
        //是否显示压缩进度条
        boolean showProgressBar=true;
        //压缩后是否保存原图
        boolean enableRawFile=true;
        CompressConfig config;
        if (false){
            //使用自带的压缩工具
            config=new CompressConfig.Builder()
                    .setMaxSize(maxSize)
                    .setMaxPixel(width>=height?width:height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        }else {
            //使用开源的鲁班压缩工具
            LubanOptions options=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config=CompressConfig.ofLuban(options);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config,showProgressBar);

    }

    /**
     * 获取TakePhoto的实例
     * @return
     */
    private TakePhoto getTakePhoto() {
        if(takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 选取照片或拍照成功
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {
        File file = new File(result.getImages().get(0).getCompressPath());
        mHeaderAbsolutePath=file.getAbsolutePath();

        Message msg=mHandler.obtainMessage();
        msg.what=mMessageFlag;
        msg.obj=file;
        mHandler.sendMessage(msg);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

}
