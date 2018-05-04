package com.example.pgg.qboxdemo.module.start.welcome;

import android.support.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.PageSupportFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.example.pgg.qboxdemo.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by pgg on 2018/5/3.
 */

public class ThridCustomPageSupportFragment extends PageSupportFragment {


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_page_third;
    }

    @NonNull
    @Override
    protected TransformItem[] getTransformItems() {
        return new TransformItem[]{
                TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f),
                TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.6f),
                TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.8f),
                TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.01f),
                TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f),
                TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f),
                TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f),
                TransformItem.create(R.id.ivEighthImage, Direction.LEFT_TO_RIGHT, 0.07f)
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getUmengClassName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getUmengClassName());
    }

    private String getUmengClassName(){
        return this.getClass().getSuperclass().getName()+
                this.getClass().getName();
    }
}
