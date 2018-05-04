package com.example.pgg.qboxdemo.me;

import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;

import android.view.MenuItem;
import android.widget.Toolbar;

/**
 * Created by pgg on 2018/5/3.
 */

public class UserInfoActivity extends BaseCommonActivity implements Toolbar.OnMenuItemClickListener,TakePhoto.TakeResultListener,InvokeListener {

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public void initContentView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void takeSuccess(TResult result) {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        return null;
    }
}
