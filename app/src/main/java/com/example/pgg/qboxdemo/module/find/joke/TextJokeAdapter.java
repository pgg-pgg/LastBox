package com.example.pgg.qboxdemo.module.find.joke;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pgg.qboxdemo.R;

import java.util.List;

/**
 * Created by pgg on 2018/5/16.
 */

public class TextJokeAdapter  extends BaseMultiItemQuickAdapter<TextJokeBean,BaseViewHolder> {


    public TextJokeAdapter(List<TextJokeBean> data) {
        super(data);
        addItemType(TextJokeBean.JOKE, R.layout.item_textjoke_joke);
        addItemType(TextJokeBean.MORE,R.layout.item_textjoke_more);
    }

    @Override
    protected void convert(BaseViewHolder helper, TextJokeBean item) {
        if (helper.getItemViewType()==TextJokeBean.JOKE){
            helper.setText(R.id.tv_item_textjoke,item.getContent());
        }else if (TextJokeBean.MORE==helper.getItemViewType()){
            Glide.with(mContext).load(R.drawable.loadingjoke)
                    .into((ImageView) helper.getView(R.id.img_item_morejoke));
        }
    }
}
