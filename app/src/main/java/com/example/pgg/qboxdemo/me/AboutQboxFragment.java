package com.example.pgg.qboxdemo.me;


import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.base.BaseFragment;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time:2017/4/7 14:09.
 *    *     *   *         *   * *       Email address:ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public class AboutQboxFragment extends BaseFragment {


    public AboutQboxFragment() {
    }

    public static AboutQboxFragment newInstance() {
        AboutQboxFragment fragment = new AboutQboxFragment();
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_about_qbox;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return getContext().getClass().getSimpleName()+"-"
                +this.getClass().getSimpleName();
    }

}
