package com.example.pgg.qboxdemo.me;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseCommonActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by pgg on 2018/5/3.
 */

@RuntimePermissions
public class FlashActivity extends BaseCommonActivity implements View.OnClickListener {
    RelativeLayout root;
    Camera camera;
    ImageButton flashLight;
    ImageButton sos;
    Drawable[] controlDrawable = null;
    Camera.Parameters parameters;
    volatile boolean continueSos;
    Handler sosHandler;
    final int FLASH_LIGHT_ON = 1;
    final int FLASH_LIGHT_OFF = -1;

    @Override
    public void onClick(View view) {
        judge(view);
    }

    private void judge(View view) {
        if (!(view instanceof ImageButton)) {
            return;
        }
        ImageButton controlTb = (ImageButton) view;

        continueSos = false;
        if (view.equals(flashLight)) {
            sos.setImageDrawable(controlDrawable[2]);
            sos.setTag("close");
            if (view.getTag().equals("close")) {
                turnOnFlashLight();
                view.setTag("open");
                controlTb.setImageDrawable(controlDrawable[1]);
                root.setBackground(controlDrawable[5]);
            } else {
                turnOffFlashLight();
                view.setTag("close");
                controlTb.setImageDrawable(controlDrawable[0]);
                root.setBackground(controlDrawable[4]);
            }
        } else if (view.equals(sos)) {
            flashLight.setImageDrawable(controlDrawable[0]);
            flashLight.setTag("close");
            if (view.getTag().equals("close")) {
                view.setTag("open");
                controlTb.setImageDrawable(controlDrawable[3]);
                continueSos = true;
                sosHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (!continueSos) {
                            return;
                        }
                        switch (msg.arg1) {
                            case FLASH_LIGHT_ON:
                                turnOnFlashLight();
                                root.setBackground(controlDrawable[5]);
                                break;
                            case FLASH_LIGHT_OFF:
                                turnOffFlashLight();
                                root.setBackground(controlDrawable[4]);
                                break;
                            default:
                                break;
                        }

                    }
                };

                new Thread(){
                    @Override
                    public void run() {
                        while (continueSos){
                            Message msg=Message.obtain();
                            msg.arg1=FLASH_LIGHT_ON;
                            sosHandler.sendMessage(msg);
                            try {
                                Thread.sleep(600);
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            Message message=Message.obtain();
                            message.arg1=FLASH_LIGHT_OFF;
                            sosHandler.sendMessage(message);
                            try{
                                Thread.sleep(300);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        Message message=Message.obtain();
                        message.arg1=FLASH_LIGHT_OFF;
                        sosHandler.sendMessage(message);
                    }
                }.start();
            }else {
                view.setTag("close");
                turnOffFlashLight();
                controlTb.setImageDrawable(controlDrawable[2]);
                root.setBackground(controlDrawable[4]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        //Auto-generated method stub
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }

    private void turnOnFlashLight() {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    private void turnOffFlashLight(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_flash);
    }

    @Override
    public void initView() {
        Resources resources = getResources();
        controlDrawable = new Drawable[]{
                resources.getDrawable(R.drawable.flash_light_off),
                resources.getDrawable(R.drawable.flash_light_on),
                resources.getDrawable(R.drawable.sos_off),
                resources.getDrawable(R.drawable.sos_on),
                resources.getDrawable(R.drawable.background),
                resources.getDrawable(R.drawable.background_on)
        };
        root = findViewById(R.id.root);
        flashLight = findViewById(R.id.flashLight);
        flashLight.setTag("open");
        sos = findViewById(R.id.sos);
        sos.setTag("close");

        FlashActivityPermissionsDispatcher.startFlashWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void startFlash() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenHeight = metrics.heightPixels;
        RelativeLayout.LayoutParams flashLightParams = (RelativeLayout.LayoutParams) flashLight.getLayoutParams();
        RelativeLayout.LayoutParams sosParams = (RelativeLayout.LayoutParams) sos.getLayoutParams();
        flashLightParams.setMargins(0, screenHeight * 1 / 2, 0, 0);
        sosParams.setMargins(0, screenHeight * 4 / 5, 0, 0);
        flashLight.setLayoutParams(flashLightParams);
        sos.setLayoutParams(sosParams);
        camera = Camera.open();
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        flashLight.setImageDrawable(controlDrawable[1]);
        root.setBackground(controlDrawable[5]);

        flashLight.setOnClickListener(this);
        sos.setOnClickListener(this);
    }

    /**
     * 为什么要获取这个权限给用户的说明
     *
     * @param request
     */
    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("你将使用的闪光灯功能，请你授权照相机权限!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.cancel();
            }
        }).show();
    }


    /**
     * 如果用户不授予权限调用的方法
     */
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showDeniedForCamera() {
        Toast.makeText(this, "没有授予照相机的权限", Toast.LENGTH_SHORT).show();
    }

    /**
     * 如果用户选择了让设备“不再询问”，而调用的方法
     */
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void showNeverAskForCamera() {
        Toast.makeText(this, "Don't ask again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FlashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void initPresenter() {

    }
}
