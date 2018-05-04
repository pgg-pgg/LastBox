package com.example.pgg.qboxdemo.module.start.welcome;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.PageSupportFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.example.pgg.qboxdemo.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by pgg on 2018/5/3.
 */

public class FirstCustomPageSupportFragment extends PageSupportFragment{


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_page_first;
    }

    @NonNull
    @Override
    protected TransformItem[] getTransformItems() {
        return new TransformItem[]{
                TransformItem.create(R.id.ivFirstImage, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.ivSecondImage, Direction.RIGHT_TO_LEFT, 0.06f),
                TransformItem.create(R.id.ivThirdImage, Direction.LEFT_TO_RIGHT, 0.08f),
                TransformItem.create(R.id.ivFourthImage, Direction.RIGHT_TO_LEFT, 0.1f),
                TransformItem.create(R.id.ivFifthImage, Direction.RIGHT_TO_LEFT, 0.03f),
                TransformItem.create(R.id.ivSixthImage, Direction.RIGHT_TO_LEFT, 0.09f),
                TransformItem.create(R.id.ivSeventhImage, Direction.RIGHT_TO_LEFT, 0.14f),
                TransformItem.create(R.id.ivEighthImage, Direction.RIGHT_TO_LEFT, 0.07f)
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getUmengFragmentName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getUmengFragmentName());
    }

    public String getUmengFragmentName(){
        return this.getClass().getSimpleName()+"-"
                +this.getClass().getSimpleName();
    }
}
