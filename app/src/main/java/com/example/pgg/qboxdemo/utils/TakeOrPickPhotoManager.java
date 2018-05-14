package com.example.pgg.qboxdemo.utils;

/**
 * Created by pgg on 2018/5/14.
 */

import android.net.Uri;
import android.os.Environment;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * 拍照或选取照片的管理类
 */
public class TakeOrPickPhotoManager {

    private TakePhoto takePhoto;
    private boolean isCrop;//是否剪裁


    public TakePhoto getTakePhoto(){return takePhoto;}

    public void setTakePhoto(TakePhoto takePhoto){
        this.takePhoto=takePhoto;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public TakeOrPickPhotoManager(TakePhoto takePhoto){
        this.takePhoto=takePhoto;
        isCrop=true;
    }

    public void takeOrPickPhoto(boolean isTakePhoto){
        File file=new File(Environment.getExternalStorageDirectory(),"/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        Uri imageUri=Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        if (isTakePhoto){
            if (isCrop){
                takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
            }else {
                takePhoto.onPickFromCapture(imageUri);
            }
        }else {
            int limit=1;
            if (limit>1){
                if (isCrop){
                    takePhoto.onPickMultipleWithCrop(limit,getCropOptions());
                }else {
                    takePhoto.onPickMultiple(limit);
                }
                return;
            }
            if (false){
                if (isCrop){
                    takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions());
                }else {
                    takePhoto.onPickFromDocuments();
                }
                return;
            }else {
                if (isCrop){
                    takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
                }else {
                    takePhoto.onPickFromGallery();
                }
            }
        }
    }

    private CropOptions getCropOptions() {

        int height = 100;
        int width = 100;

        CropOptions.Builder builder = new CropOptions.Builder();

        //按照宽高比例裁剪
        builder.setAspectX(width).setAspectY(height);
        //是否使用Takephoto自带的裁剪工具
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    /**
     * 拍照相关的配置
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        if (false){
            builder.setWithOwnGallery(true);
        }
        //纠正拍照得分旋转角度
        if (true){
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 配置压缩选项
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        int maxSize=1024*100;
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
                    .setMaxPixel(width>height?width:height)
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
}
