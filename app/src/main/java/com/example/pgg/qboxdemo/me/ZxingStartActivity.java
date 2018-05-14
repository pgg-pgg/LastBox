package com.example.pgg.qboxdemo.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.WebViewActivity;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.global.Constant;
import com.example.pgg.qboxdemo.utils.SPUtils;
import com.example.pgg.qboxdemo.utils.TakeOrPickPhotoManager;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by pgg on 2018/5/14.
 */

public class ZxingStartActivity extends BaseCommonActivity implements QRCodeView.Delegate, View.OnTouchListener, TakePhoto.TakeResultListener, InvokeListener {

    public static final int STYLE_ALL = 0;
    public static final String STYLE = "zxing_style";
    public static final int STYLE_TEXT = 1;
    public static final int STYLE_WEB = 2;
    public static final int STYLE_DOWNLOAD = 3;
    public static final int STYLE_IMG = 4;

    public final static int mMessageFlag = 0x1110;

    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    @BindView(R.id.toolbar_zxingstart)
    Toolbar mToolbarZxingstart;
    @BindView(R.id.FAB_left_zxingstart)
    FloatingActionButton mFABLeftZxingstart;
    @BindView(R.id.FAB_right_zxingstart)
    FloatingActionButton mFABRightZxingstart;

    boolean mNotShowRect = false;
    public int mZxingStyle;
    public AlertDialog.Builder mDialogBuilder;
    public TakeOrPickPhotoManager mTakeOrPickPhotoManager;
    TakePhoto takePhoto;
    private InvokeParam invokeParam;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mZxingview.showScanRect();

            switch (msg.what) {
                case mMessageFlag:
                    String result = (String) msg.obj;
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(ZxingStartActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        showSuccessDialog(result);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mZxingview.openFlashlight();//打开闪光灯
                break;
            case MotionEvent.ACTION_UP:
                mZxingview.closeFlashlight();//关闭闪光灯
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 扫描二维码成功的回调事件
     *
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        showSuccessDialog(result);
        vibrate();
    }

    /**
     * 震动手机
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

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
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startCamera();
        mZxingview.showScanRect();
    }

    @Override
    protected void onStop() {
        mZxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingview.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    RadioGroup radioGroup;

    private void showSuccessDialog(final String result) {
        mZxingview.stopSpot();
        mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mZxingview.startSpot();
            }
        });

        if (mZxingStyle == STYLE_ALL) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.selectspot_dialog_zxing, null);
            radioGroup = inflate.findViewById(R.id.radiogroup_dialog_zxing);
            radioGroup.check(R.id.rb_text);
            mDialogBuilder.setView(inflate);
        }

        mDialogBuilder.setTitle("扫取结果");
        mDialogBuilder.setMessage(result);

        View view = LayoutInflater.from(this).inflate(R.layout.headtitle_dialog_zxing, null);
        view.findViewById(R.id.copy_dialog_zxing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCopyTextToClipboard(result);
            }
        });
        mDialogBuilder.setCustomTitle(view);

        mDialogBuilder.setNegativeButton(R.string.cancel, null);
        mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (mZxingStyle) {
                    case STYLE_ALL:
                        actionAll(result, radioGroup);
                        break;
                    case STYLE_DOWNLOAD:
                        break;
                    case STYLE_IMG:
                        actionImg(result);
                        break;
                    case STYLE_TEXT:
                        actionText();
                        break;
                    case STYLE_WEB:
                        actionWeb(result);
                        break;
                    default:
                        break;
                }
            }
        });
        mDialogBuilder.show();
    }

    private void actionWeb(String result) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", result);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        finish();
    }

    private void actionText() {
        finish();
    }

    private void actionImg(String result) {
        Intent intent = new Intent(getContext(), PinImageActivity.class);
        intent.putExtra(PinImageActivity.IMG_NAME, result);
        intent.putExtra(PinImageActivity.IMG_URL, result);
        startActivity(intent);
        finish();
    }

    private void actionAll(String result, RadioGroup radioGroup) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_download:
                break;
            case R.id.rb_img:
                actionImg(result);
                break;
            case R.id.rb_text:
                actionText();
                break;
            case R.id.rb_web:
                actionWeb(result);
                break;
            default:
                break;
        }
    }

    /**
     * 复制到粘贴板
     *
     * @param result
     */
    private void onCopyTextToClipboard(String result) {
        if (!TextUtils.isEmpty(result)) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData website = ClipData.newPlainText("website", result);
            clipboardManager.setPrimaryClip(website);
            Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "复制地址出错", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Logger.e("打开相机出错");
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_zxing_start);
    }

    @Override
    public void initView() {
        getStyle();
        initToolBar();
        initFAB();
        initZxingView();
    }

    private void initZxingView() {
        mZxingview.setDelegate(this);
        mZxingview.startSpot();
    }

    private void initFAB() {
        mFABLeftZxingstart.setOnTouchListener(this);
        mFABRightZxingstart.setOnTouchListener(this);
        boolean fab_location = (boolean) SPUtils.get(this, Constant.FAB_LOCATION, false);
        changeFABLocation(fab_location);
    }

    private void initToolBar() {
        mToolbarZxingstart.inflateMenu(R.menu.zxing_start_menu);
        mToolbarZxingstart.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbarZxingstart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbarZxingstart.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.open_photos:
                        openPhotos();
                        break;
                    case R.id.fab_location:
                        changeFABLocation(!((Boolean) SPUtils.get(ZxingStartActivity.this, Constant.FAB_LOCATION, false)));
                        break;
                    case R.id.rect_mark:
                        changeRect(mNotShowRect = !mNotShowRect);
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 隐藏/显示扫描框
     *
     * @param b
     */
    private void changeRect(boolean b) {
        if (b) {
            mZxingview.hiddenScanRect();
        } else {
            mZxingview.showScanRect();
        }
    }

    /**
     * 改变浮动按钮的位置
     *
     * @param fab_location
     */
    private void changeFABLocation(boolean fab_location) {
        SPUtils.put(this, Constant.FAB_LOCATION, fab_location);
        if (!fab_location) {
            if (mFABLeftZxingstart.getVisibility() == View.GONE) {
                mFABLeftZxingstart.setVisibility(View.VISIBLE);
            }
            if (mFABRightZxingstart.getVisibility() == View.VISIBLE) {
                mFABRightZxingstart.setVisibility(View.GONE);
            }
        } else {
            if (mFABLeftZxingstart.getVisibility() == View.VISIBLE) {
                mFABLeftZxingstart.setVisibility(View.GONE);
            }
            if (mFABRightZxingstart.getVisibility() == View.GONE) {
                mFABRightZxingstart.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 打开相册
     */
    private void openPhotos() {
        mTakeOrPickPhotoManager = new TakeOrPickPhotoManager(getTakePhoto());

        //设置图片为不裁剪
        mTakeOrPickPhotoManager.setCrop(false);
        //选择图片
        mTakeOrPickPhotoManager.takeOrPickPhoto(false);
    }

    private void getStyle() {
        Intent intent = getIntent();
        mZxingStyle = intent.getIntExtra(STYLE, 0);
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this)
                    .bind(new TakePhotoImpl(this, this));

        }
        return takePhoto;
    }


    /**
     * 拍照成功
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {
        File file = new File(result.getImages().get(0).getCompressPath());
        String absolutePath = file.getAbsolutePath();

        //获取扫描图片结果的过程必须在异步线程中进行
        //在这里，因为获取图片的过程是在异步线程中进行的，所以上面不必再开新线程
        String spotResult = QRCodeDecoder.syncDecodeQRCode(absolutePath);

        //需要返回到UI线程刷新头像
        Message msg =mHandler.obtainMessage();
        msg.what=mMessageFlag;
        msg.obj=spotResult;
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
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
}
