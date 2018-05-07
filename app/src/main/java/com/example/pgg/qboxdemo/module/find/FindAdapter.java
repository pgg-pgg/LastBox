package com.example.pgg.qboxdemo.module.find;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.model.entities.FunctionBean;

import java.util.List;

/**
 * Created by pgg on 2018/5/7.
 */

public class FindAdapter extends BaseItemDraggableAdapter<FunctionBean,BaseViewHolder>{


    public FindAdapter(List<FunctionBean> data) {
        super(R.layout.item_find,data);
    }
    public FindAdapter(int layoutResId, List<FunctionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionBean item) {
        helper.setText(R.id.name_item_find,item.getName());
        ImageView view = helper.getView(R.id.icon_item_find);
        try {
            int camera = (Integer) R.drawable.class.getField(item.getCode()).get(null);
            view.setImageResource(camera);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
