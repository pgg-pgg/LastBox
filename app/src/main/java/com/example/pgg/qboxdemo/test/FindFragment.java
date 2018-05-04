package com.example.pgg.qboxdemo.test;


import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;
import com.example.pgg.qboxdemo.widget.slidinglayout.SlidingLayout;
import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.BindView;

/**
 * Created by pgg on 2018/5/4.
 */

public class FindFragment extends BaseFragment {

    @BindView(R.id.bg_find_find)
    KenBurnsView bg_find_find;

    @BindView(R.id.web_layout)
    SlidingLayout web_layout;

    @BindView(R.id.Recycler_find)
    RecyclerView Recycler_find;

    @BindView(R.id.xiaohua_find)
    TextView xiaohua_find;

    @BindView(R.id.year_calendar)
    TextView year_calendar;

    @BindView(R.id.years_calendar)
    TextView years_calendar;

    @BindView(R.id.day_clendar)
    TextView day_clendar;

    @BindView(R.id.nongli_calendar)
    TextView nongli_calendar;

    @BindView(R.id.jieri_calendar)
    TextView jieri_calendar;

    @BindView(R.id.week_calendar)
    TextView week_calendar;

    @BindView(R.id.yi_calendar)
    TextView yi_calendar;

    @BindView(R.id.ji_calendar)
    TextView ji_calendar;

    @BindView(R.id.qfriend_star_find)
    TextView qfriend_star_find;

    @BindView(R.id.xz_star_find)
    TextView xz_star_find;

    @BindView(R.id.yunshi_star_find)
    TextView yunshi_star_find;

    @BindView(R.id.bg_title_find)
    TextView bg_title_find;

    @BindView(R.id.before_find)
    ImageButton before_find;

    @BindView(R.id.next_find)
    ImageButton next_find;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }
}
