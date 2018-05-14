package com.example.pgg.qboxdemo.module.find;

import android.content.Context;
import android.content.res.Resources;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.model.entities.FunctionBean;

import java.util.List;

/**
 * Created by pgg on 2018/5/15.
 */

public class FindMoreAdapter extends BaseItemDraggableAdapter<FunctionBean,BaseViewHolder> {


    public final int[] mColorArray;
    public final int mColorLength;

    public FindMoreAdapter(List<FunctionBean> data, Context context) {
        super(R.layout.item_findmore,data);
        Resources resources=context.getResources();
        mColorArray = resources.getIntArray(R.array.itemcolor);
        mColorLength=mColorArray.length;
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionBean item) {
        int layoutPosition=helper.getLayoutPosition();
        helper.setText(R.id.icon_item_findmore,String.valueOf(layoutPosition))
                .setBackgroundColor(R.id.icon_item_findmore,mColorArray[layoutPosition%mColorLength])
                .setBackgroundColor(R.id.flag_item_findmore,mContext.getResources().getColor(
                        (layoutPosition<=5)?R.color.colorAccent:R.color.colorPrimary))
                .setText(R.id.name_item_findmore,item.getName());
    }
}
