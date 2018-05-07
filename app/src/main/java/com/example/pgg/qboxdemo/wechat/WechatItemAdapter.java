package com.example.pgg.qboxdemo.wechat;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.model.entities.WechatItem;

import java.util.List;

/**
 * Created by pgg on 2018/5/6.
 */

public class WechatItemAdapter extends BaseMultiItemQuickAdapter<WechatItem.ResultBean.ListBean,BaseViewHolder> {
    public boolean isNotLoad;
    public int mImgWidth;
    public int mImgHeight;

    public WechatItemAdapter(List<WechatItem.ResultBean.ListBean> data) {
        super(data);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_BIG, R.layout.item_wechat_style1);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_SMALL, R.layout.item_wechat_style2);

    }

    public WechatItemAdapter(List<WechatItem.ResultBean.ListBean> data,boolean isNotLoad,int mImgWidth,int mImgHeight) {
        super(data);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_BIG, R.layout.item_wechat_style1);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_SMALL, R.layout.item_wechat_style2);
        this.isNotLoad=isNotLoad;
        this.mImgWidth=mImgWidth;
        this.mImgHeight=mImgHeight;
    }

    @Override
    protected void convert(BaseViewHolder helper, WechatItem.ResultBean.ListBean item) {
        switch (helper.getItemViewType()){
            case WechatItem.ResultBean.ListBean.STYLE_BIG:
                helper.setText(R.id.title_wechat_style1, TextUtils.isEmpty(item.getTitle())?"微信精选":item.getTitle());
                if (!isNotLoad){
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getThumbnails())
                            .override(mImgWidth,mImgHeight)
                            .centerCrop()
                            .error(R.drawable.errorview)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.img_wechat_style));
                }
                break;
                case WechatItem.ResultBean.ListBean.STYLE_SMALL:
                    helper.setText(R.id.title_wechat_style2, TextUtils.isEmpty(item.getTitle())?"微信精选":item.getTitle());
                    if (!isNotLoad){
                        Glide.with(mContext.getApplicationContext())
                                .load(item.getThumbnails())
                                .override(mImgWidth/2,mImgHeight/2)
                                .centerCrop()
                                .error(R.drawable.errorview)
                                .crossFade(1000)
                                .into((ImageView) helper.getView(R.id.img_wechat_style));
                    }
        }
    }
}
